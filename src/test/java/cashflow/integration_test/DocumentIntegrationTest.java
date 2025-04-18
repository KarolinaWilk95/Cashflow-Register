package cashflow.integration_test;

import cashflow.config.JwtService;
import cashflow.document.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class DocumentIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    DocumentMapper documentMapper;


    public HttpEntity<String> authorization() {
        String token = jwtService.generateToken("test", List.of("CONTROLLING"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return entity;
    }
    public HttpEntity<DocumentAPI> authorizationWithBody(DocumentAPI documentAPI) {
        String token = jwtService.generateToken("test", List.of("CONTROLLING"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<DocumentAPI> entity = new HttpEntity<>(documentAPI, headers);
        return entity;
    }
    public HttpEntity<String> authorizationForManagement() {
        String token = jwtService.generateToken("test", List.of("MANAGEMENT"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return entity;
    }
    public HttpEntity<DocumentAPI> authorizationForManagementWithBody(DocumentAPI documentAPI) {
        String token = jwtService.generateToken("test", List.of("MANAGEMENT"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<DocumentAPI> entity = new HttpEntity<>(documentAPI, headers);
        return entity;
    }

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();

        List<Document> list = List.of(new Document(null, DocumentGroup.COST, DocumentType.DEBIT_NOTE, "244/F/2025", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 3, 28), LocalDate.of(2025, 4, 19), "Fundacja Pielka-Kość i syn s.c.", 9957058572L, BigDecimal.valueOf(4104.09), BigDecimal.valueOf(626.42), BigDecimal.valueOf(4730.51), BigDecimal.valueOf(20246.58), Currency.getInstance("EUR"), "", null, BigDecimal.ZERO),
                new Document(null, DocumentGroup.COST, DocumentType.INVOICE, "1/C/2025", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 20), "Fundacja Pielka-Kość i syn s.c.", 9957058572L, BigDecimal.valueOf(200), BigDecimal.valueOf(4.6), BigDecimal.valueOf(204.6), BigDecimal.valueOf(204.6), Currency.getInstance("USD"), "", null, BigDecimal.ZERO),
                new Document(null, DocumentGroup.COST, DocumentType.INVOICE, "1222/C/2025", LocalDate.of(2025, 2, 25), LocalDate.of(2025, 2, 26), LocalDate.of(2025, 2, 26), "Gabinety Tecław", 1380960558L, BigDecimal.valueOf(3551.01), BigDecimal.valueOf(779.83), BigDecimal.valueOf(4330.84), BigDecimal.valueOf(16370.57), Currency.getInstance("USD"), "", LocalDate.of(2025, 2, 26), BigDecimal.valueOf(4236.64)),
                new Document(null, DocumentGroup.SALE, DocumentType.INVOICE, "3/S/2025", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 2, 28), LocalDate.of(2025, 5, 10), "Stowarzyszenie Szkatuła Sp. z o.o.", 4923225484L, BigDecimal.valueOf(4672.89), BigDecimal.valueOf(324.89), BigDecimal.valueOf(4997.78), BigDecimal.valueOf(4997.78), Currency.getInstance("PLN"), "", LocalDate.of(2025, 4, 14), BigDecimal.valueOf(4997.78)),
                new Document(null, DocumentGroup.SALE, DocumentType.INTEREST_NOTE, "27/S/2025", LocalDate.of(2025, 3, 19), LocalDate.of(2025, 2, 19), LocalDate.of(2025, 4, 17), "Kunysz-Bugno Sp.j.", 8442067095L, BigDecimal.valueOf(2365.91), BigDecimal.valueOf(140.55), BigDecimal.valueOf(2506.46), BigDecimal.valueOf(2506.46), Currency.getInstance("PLN"), "UMOWA 18", null, BigDecimal.ZERO));

        documentRepository.saveAll(list);
    }


    @Test
    void getAllDocumentsWithoutAuthorization() {
        //given

        //when
        var result = restTemplate.exchange("/api/register/documents", HttpMethod.GET, null, new ParameterizedTypeReference<List<Document>>() {
        });

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getAllDocuments() {
        //given
        List<Document> list = new ArrayList<>();

        //when
        var result = restTemplate.exchange("/api/register/documents", HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<Document>>() {
        });
        list = documentRepository.findAll();

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(list.size());
    }

    @Test
    void getAllDocumentsBySpecificValue() {
        //given
        String value = "cost";
        List<Document> list = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 30, Sort.by("id").ascending());

        //when
        var result = restTemplate.exchange("/api/register/documents?search=" + value, HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<Document>>() {
        });
        list = documentRepository.findAllDocuments(value, pageable).stream().toList();

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(3);
    }

    @Test
    void getAllDocumentsBySpecificValueNoMatch() {
        //given
        String value = "BIEDRONKA";
        List<Document> list = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 30, Sort.by("id").ascending());

        //when
        var result = restTemplate.exchange("/api/register/documents?search=" + value, HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<Document>>() {
        });
        list = documentRepository.findAllDocuments(value, pageable).stream().toList();

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(list.size());
        assertThat(result.getBody().size()).isEqualTo(0);
    }

    @Test
    void getDocumentById() {
        //given
        Long id = documentRepository.findAll().getFirst().getId();
        Document document = new Document();

        //when
        var result = restTemplate.exchange("/api/register/documents/" + id, HttpMethod.GET, authorization(), Document.class);
        document = documentRepository.findById(id).get();

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(id);
        assertThat(result.getBody().getDocumentNumber()).isEqualTo("244/F/2025");
    }
    @Test
    void getDocumentByIdIfNotExist() {
        //given

        //when
        var result = restTemplate.exchange("/api/register/documents/-1", HttpMethod.GET, authorization(), String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void addDocumentValidRoleValidData() {
        //given
        DocumentAPI document = new DocumentAPI(null, DocumentGroup.SALE, DocumentType.INTEREST_NOTE, "27/S/2025", LocalDate.of(2025, 3, 19), LocalDate.of(2025, 2, 19), LocalDate.of(2025, 4, 17), "Kunysz-Bugno Sp.j.", 8442067095L, BigDecimal.valueOf(2365.91), BigDecimal.valueOf(140.55), BigDecimal.valueOf(2506.46), BigDecimal.valueOf(2506.46), Currency.getInstance("PLN"), "UMOWA 18", null, BigDecimal.ZERO);

        //when
        var result = restTemplate.exchange("/api/register/documents", HttpMethod.POST, authorizationWithBody(document), DocumentAPI.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getContractorVatNumber()).isEqualTo(8442067095L);
    }

    @Test
    void addDocumentValidRoleInvalidData() {
        //given
        DocumentAPI document = new DocumentAPI(null, DocumentGroup.SALE, DocumentType.INTEREST_NOTE, "27/S/2025", LocalDate.of(2025, 3, 19), LocalDate.of(2025, 2, 19), LocalDate.of(2025, 4, 17), "Kunysz-Bugno Sp.j.", null, BigDecimal.valueOf(2365.91), BigDecimal.valueOf(140.55), BigDecimal.valueOf(2506.46), BigDecimal.valueOf(2506.46), Currency.getInstance("PLN"), "UMOWA 18", null, BigDecimal.ZERO);

        //when
        var result = restTemplate.exchange("/api/register/documents", HttpMethod.POST, authorizationWithBody(document), String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void addDocumentInvalidRoleValidData() {
        //given
        DocumentAPI document = new DocumentAPI(null, DocumentGroup.SALE, DocumentType.INTEREST_NOTE, "27/S/2025", LocalDate.of(2025, 3, 19), LocalDate.of(2025, 2, 19), LocalDate.of(2025, 4, 17), "Kunysz-Bugno Sp.j.", 8442067095L, BigDecimal.valueOf(2365.91), BigDecimal.valueOf(140.55), BigDecimal.valueOf(2506.46), BigDecimal.valueOf(2506.46), Currency.getInstance("PLN"), "UMOWA 18", null, BigDecimal.ZERO);

        //when
        var result = restTemplate.exchange("/api/register/documents", HttpMethod.POST, authorizationForManagementWithBody(document), String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
