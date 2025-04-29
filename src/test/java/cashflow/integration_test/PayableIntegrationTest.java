package cashflow.integration_test;

import cashflow.config.JwtService;
import cashflow.document.Document;
import cashflow.document.DocumentGroup;
import cashflow.document.DocumentRepository;
import cashflow.document.DocumentType;
import cashflow.register.payables.Payable;
import cashflow.register.payables.PayableAPI;
import cashflow.register.payables.PayableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PayableIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PayableRepository payableRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    JwtService jwtService;

    public HttpEntity<String> authorization() {
        String token = jwtService.generateToken("test", List.of("CONTROLLING"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>("body", headers);
    }

    @BeforeEach
    void setUp() {
        payableRepository.deleteAll();
        documentRepository.deleteAll();

        List<Document> list = List.of(new Document(null, DocumentGroup.COST, DocumentType.DEBIT_NOTE, "244/F/2025", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 3, 28), LocalDate.of(2025, 4, 19), "Fundacja Pielka-Kość i syn s.c.", 9957058572L, BigDecimal.valueOf(4104.09), BigDecimal.valueOf(626.42), BigDecimal.valueOf(4730.51), BigDecimal.valueOf(20246.58), Currency.getInstance("EUR"), "", null, BigDecimal.ZERO),
                new Document(null, DocumentGroup.COST, DocumentType.INVOICE, "1/C/2025", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 20), "Fundacja Pielka-Kość i syn s.c.", 9957058572L, BigDecimal.valueOf(200), BigDecimal.valueOf(4.6), BigDecimal.valueOf(204.6), BigDecimal.valueOf(204.6), Currency.getInstance("USD"), "", null, BigDecimal.ZERO),
                new Document(null, DocumentGroup.COST, DocumentType.INVOICE, "1222/C/2025", LocalDate.of(2025, 2, 25), LocalDate.of(2025, 2, 26), LocalDate.of(2025, 2, 26), "Gabinety Tecław", 1380960558L, BigDecimal.valueOf(3551.01), BigDecimal.valueOf(779.83), BigDecimal.valueOf(4330.84), BigDecimal.valueOf(16370.57), Currency.getInstance("USD"), "", LocalDate.of(2025, 2, 26), BigDecimal.valueOf(4236.64)),
                new Document(null, DocumentGroup.SALE, DocumentType.INVOICE, "3/S/2025", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 2, 28), LocalDate.of(2025, 5, 10), "Stowarzyszenie Szkatuła Sp. z o.o.", 4923225484L, BigDecimal.valueOf(4672.89), BigDecimal.valueOf(324.89), BigDecimal.valueOf(4997.78), BigDecimal.valueOf(4997.78), Currency.getInstance("PLN"), "", LocalDate.of(2025, 4, 14), BigDecimal.valueOf(4997.78)),
                new Document(null, DocumentGroup.SALE, DocumentType.INTEREST_NOTE, "27/S/2025", LocalDate.of(2025, 3, 19), LocalDate.of(2025, 2, 19), LocalDate.of(2025, 4, 17), "Kunysz-Bugno Sp.j.", 8442067095L, BigDecimal.valueOf(2365.91), BigDecimal.valueOf(140.55), BigDecimal.valueOf(2506.46), BigDecimal.valueOf(2506.46), Currency.getInstance("PLN"), "UMOWA 18", null, BigDecimal.ZERO));

        documentRepository.saveAll(list);

        List<Payable> listOfPayable = new ArrayList<>();

        for (Document document : list) {
            if (document.getDocumentGroup().equals(DocumentGroup.COST) && (document.getTotalAmount().compareTo(document.getPaymentAmount())) > 0) {
                listOfPayable.add(new Payable(null, document));
            }
        }

        payableRepository.saveAll(listOfPayable);
    }

    @Test
    void showAllPayablesValidRole() {
        //given
        List<PayableAPI> list = List.of(
                new PayableAPI(null, null, LocalDate.of(2025, 3, 25), "244/F/2025", "Fundacja Pielka-Kość i syn s.c.", LocalDate.of(2025, 4, 19), BigDecimal.valueOf(20246.58), BigDecimal.ZERO, BigDecimal.valueOf(20246.58), BigDecimal.valueOf(20246.58), Currency.getInstance("EUR")),
                new PayableAPI(null, null, LocalDate.of(2025, 2, 11), "1/C/2025", "Fundacja Pielka-Kość i syn s.c.", LocalDate.of(2025, 2, 20), BigDecimal.valueOf(204.6), BigDecimal.ZERO, BigDecimal.valueOf(204.6), BigDecimal.valueOf(20246.58), Currency.getInstance("USD")),
                new PayableAPI(null, null, LocalDate.of(2025, 2, 25), "1222/C/2025", "Gabinety Tecław", LocalDate.of(2025, 2, 26), BigDecimal.valueOf(4330.84), BigDecimal.valueOf(4236.64), BigDecimal.valueOf(4330.84).subtract(BigDecimal.valueOf(4236.64)), BigDecimal.valueOf(16370.57), Currency.getInstance("USD")));

        //when
        var result = restTemplate.exchange("/api/register/payables", HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<PayableAPI>>() {
        });

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(list.size());
        assertThat(result.getBody().getFirst().getDocumentNumber()).isEqualTo(list.getFirst().getDocumentNumber());
        assertThat(result.getBody().get(1).getDocumentNumber()).isEqualTo(list.get(1).getDocumentNumber());
        assertThat(result.getBody().getLast().getDocumentNumber()).isEqualTo(list.getLast().getDocumentNumber());
    }

    @Test
    void showAllPayablesInvalidRole() {
        //given

        //when
        var result = restTemplate.exchange("/api/register/payables", HttpMethod.GET, null, String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void createReportsAboutPayables() {
        //given

        //when
        var reportsOverdue = restTemplate.exchange("/api/register/payables/overdue", HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<PayableAPI>>() {
        });
        var reportAboutOverdueGrouped = restTemplate.exchange("/api/register/payables/overdue/grouped", HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<String>>() {
        });
        var reportOverdueByDate = restTemplate.exchange("/api/register/payables/aging", HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<PayableAPI>>() {
        });

        //then
        assertThat(reportsOverdue.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reportAboutOverdueGrouped.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reportOverdueByDate.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
