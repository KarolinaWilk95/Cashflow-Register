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
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

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


    public HttpEntity<String> authorization() {
        String token = jwtService.generateToken("test", List.of("CONTROLLING"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>("body", headers);
    }

    public <T> HttpEntity<T> authorizationWithBody(T body) {
        String token = jwtService.generateToken("test", List.of("CONTROLLING"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.valueOf("application/json-patch+json"));
        return new HttpEntity<>(body, headers);
    }

    public <T> HttpEntity<T> authorizationWithBodyJson(T body) {
        String token = jwtService.generateToken("test", List.of("CONTROLLING"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.valueOf("application/json-patch+json"));
        return new HttpEntity<>(body, headers);
    }

    public HttpEntity<String> authorizationForManagement() {
        String token = jwtService.generateToken("test", List.of("MANAGEMENT"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>("body", headers);
    }

    public HttpEntity<DocumentAPI> authorizationForManagementWithBody(DocumentAPI documentAPI) {
        String token = jwtService.generateToken("test", List.of("MANAGEMENT"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(documentAPI, headers);
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

    @Test
    void deleteDocumentByValidIdValidRole() {
        //given
        Long id = documentRepository.findAll().getFirst().getId();

        //when
        var result = restTemplate.exchange("/api/register/documents/" + id, HttpMethod.DELETE, authorization(), Void.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(documentRepository.findById(id)).isEqualTo(Optional.empty());
    }

    @Test
    void deleteDocumentByInvalidIdValidRole() {
        //given
        int id = -1;

        //when
        var result = restTemplate.exchange("/api/register/documents/" + id, HttpMethod.DELETE, authorization(), Void.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteDocumentByValidIdInvalidRole() {
        //given
        Long id = documentRepository.findAll().getFirst().getId();

        //when
        var result = restTemplate.exchange("/api/register/documents/" + id, HttpMethod.DELETE, authorizationForManagement(), Void.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void partialUpdateDocumentValidRole() {
        //given
        Long id = documentRepository.findAll().getFirst().getId();
        String patchStr = """
                    [
                        { "op": "replace", "path": "/paymentAmount", "value": 200 }
                    ]
                """;
        HttpEntity<String> httpEntity = authorizationWithBodyJson(patchStr);

        //when
        var result = restTemplate.exchange("/api/register/documents/" + id, HttpMethod.PATCH, httpEntity, Void.class);

        var updatedDocument = restTemplate.exchange("/api/register/documents/" + id, HttpMethod.GET, authorization(), DocumentAPI.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(updatedDocument.getBody().getPaymentAmount()).isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void filterDocumentsValidRole() {
        //given
        String request = "contractorName=biedronka";
        Document document = new Document(null, DocumentGroup.COST, DocumentType.INVOICE, "fv uproszczona", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 11), "BIEDRONKA", 7791011327L, BigDecimal.valueOf(100), BigDecimal.valueOf(2), BigDecimal.valueOf(102), BigDecimal.valueOf(102), Currency.getInstance("PLN"), "", null, BigDecimal.valueOf(102));
        DocumentAPI documentAPI = new DocumentAPI(null, DocumentGroup.COST, DocumentType.INVOICE, "fv uproszczona", LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 11), LocalDate.of(2025, 2, 11), "BIEDRONKA", 7791011327L, BigDecimal.valueOf(100), BigDecimal.valueOf(2), BigDecimal.valueOf(102), BigDecimal.valueOf(102), Currency.getInstance("PLN"), "", null, BigDecimal.valueOf(102));
        documentRepository.save(document);
        List<DocumentAPI> list = new ArrayList<>();
        list.add(documentAPI);

        //when
        var result = restTemplate.exchange("/api/register/documents/filter?" + request, HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<DocumentAPI>>() {
        });

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(list.size());
        assertThat(result.getBody().getFirst().getContractorVatNumber()).isEqualTo(7791011327L);
    }

    @Test
    void filterNoMatch(){
        //given
        String request = "contractorName=biedronka";

        //when
        var result = restTemplate.exchange("/api/register/documents/filter?" + request, HttpMethod.GET, authorization(), new ParameterizedTypeReference<List<DocumentAPI>>() {
        });

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(0);

    }
}
