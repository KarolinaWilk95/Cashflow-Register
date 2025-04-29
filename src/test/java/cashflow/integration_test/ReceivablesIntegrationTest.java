package cashflow.integration_test;


import cashflow.config.JwtService;
import cashflow.document.Document;
import cashflow.document.DocumentGroup;
import cashflow.document.DocumentRepository;
import cashflow.document.DocumentType;
import cashflow.register.receivable.Receivable;
import cashflow.register.receivable.ReceivableAPI;
import cashflow.register.receivable.ReceivableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReceivablesIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ReceivableRepository receivableRepository;

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

    public <T> HttpEntity<T> authorizationWithBodyJson(T patch) {
        String token = jwtService.generateToken("test", List.of("CONTROLLING"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.valueOf("application/json-patch+json"));
        return new HttpEntity<>(patch, headers);
    }


    @BeforeEach
    void setUp() {
        receivableRepository.deleteAll();
        documentRepository.deleteAll();

        List<Document> list = List.of(new Document(null, DocumentGroup.COST, DocumentType.DEBIT_NOTE, "244/F/2025", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 3, 28), LocalDate.of(2025, 4, 19), "Fundacja Pielka-Kość i syn s.c.", 9957058572L, BigDecimal.valueOf(4104.09), BigDecimal.valueOf(626.42), BigDecimal.valueOf(4730.51), BigDecimal.valueOf(20246.58), Currency.getInstance("EUR"), "", null, BigDecimal.ZERO),
                new Document(null, DocumentGroup.COST, DocumentType.INVOICE, "1/C/2025", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 20), "Fundacja Pielka-Kość i syn s.c.", 9957058572L, BigDecimal.valueOf(200), BigDecimal.valueOf(4.6), BigDecimal.valueOf(204.6), BigDecimal.valueOf(204.6), Currency.getInstance("USD"), "", null, BigDecimal.ZERO),
                new Document(null, DocumentGroup.COST, DocumentType.INVOICE, "1222/C/2025", LocalDate.of(2025, 2, 25), LocalDate.of(2025, 2, 26), LocalDate.of(2025, 2, 26), "Gabinety Tecław", 1380960558L, BigDecimal.valueOf(3551.01), BigDecimal.valueOf(779.83), BigDecimal.valueOf(4330.84), BigDecimal.valueOf(16370.57), Currency.getInstance("USD"), "", LocalDate.of(2025, 2, 26), BigDecimal.valueOf(4236.64)),
                new Document(null, DocumentGroup.SALE, DocumentType.INVOICE, "3/S/2025", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 2, 28), LocalDate.of(2025, 5, 10), "Stowarzyszenie Szkatuła Sp. z o.o.", 4923225484L, BigDecimal.valueOf(4672.89), BigDecimal.valueOf(324.89), BigDecimal.valueOf(4997.78), BigDecimal.valueOf(4997.78), Currency.getInstance("PLN"), "", LocalDate.of(2025, 4, 14), BigDecimal.valueOf(4997.78)),
                new Document(null, DocumentGroup.SALE, DocumentType.INTEREST_NOTE, "27/S/2025", LocalDate.of(2025, 3, 19), LocalDate.of(2025, 2, 19), LocalDate.of(2025, 4, 17), "Kunysz-Bugno Sp.j.", 8442067095L, BigDecimal.valueOf(2365.91), BigDecimal.valueOf(140.55), BigDecimal.valueOf(2506.46), BigDecimal.valueOf(2506.46), Currency.getInstance("PLN"), "UMOWA 18", null, BigDecimal.ZERO));

        documentRepository.saveAll(list);

        List<Receivable> receivableList = new ArrayList<>();

        for (Document document : list) {
            if (document.getDocumentGroup().equals(DocumentGroup.SALE) && (document.getTotalAmount().compareTo(document.getPaymentAmount())) > 0) {
                receivableList.add(new Receivable(null, document, null, null));
            }
        }

        receivableRepository.saveAll(receivableList);
    }

    @Test
    void showAllReceivablesValidRole() {
        //given
        Long delay = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.of(2025, 4, 17));


        List<ReceivableAPI> list = List.of(new ReceivableAPI(null, null, LocalDate.of(2025, 3, 19), "27/S/2025", "Kunysz-Bugno Sp.j.", LocalDate.of(2025, 4, 17), delay, BigDecimal.valueOf(2506.46), BigDecimal.ZERO, BigDecimal.valueOf(2506.46), BigDecimal.valueOf(2506.46), Currency.getInstance("PLN"), BigDecimal.ZERO, null, null));

        //when
        var result = restTemplate.exchange("/api/register/receivables", HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<ReceivableAPI>>() {
        });

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(list.size());
    }

    @Test
    void getAllReceivablesWithoutAuthorization() {
        //given

        //when
        var result = restTemplate.exchange("/api/register/receivables", HttpMethod.GET, null, new ParameterizedTypeReference<List<Receivable>>() {
        });

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void addPartialPaymentAmountValidRole() {
        //given
        BigDecimal partialAmount = BigDecimal.valueOf(200);
        Long id = receivableRepository.findAll().getFirst().getId();
        String patchStr = """
                    [
                        { "op": "replace", "path": "/paymentAmount", "value": 200 }
                    ]
                """;
        HttpEntity<String> httpEntity = authorizationWithBodyJson(patchStr);

        //when
        var result = restTemplate.exchange("/api/register/receivables/" + id, HttpMethod.PATCH, httpEntity, Void.class);

        var receivableAfterUpdate = restTemplate.exchange("/api/register/receivables/" + id, HttpMethod.GET, authorization(), ReceivableAPI.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}
