package cashflow.document;

import cashflow.dao.DocumentFilterRequest;
import cashflow.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;
    @Mock
    private DocumentMapper documentMapper;
    @InjectMocks
    private DocumentController documentController;

    @Test
    @WithMockUser
    void getAllDocumentsWithoutSearchingValue() {
        //given
        Document document = new Document();
        document.setId(1L);
        DocumentAPI documentAPI = new DocumentAPI();
        List<Document> list = List.of(document);

        int page = 1;
        int size = 30;
        String sortBy = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        when(documentService.getAllDocuments(pageable)).thenReturn(list);
        when(documentMapper.modelToApi(document)).thenReturn(documentAPI);

        //when
        var result = documentController.getAllDocuments(null, page, size, sortBy, "asc");

        //then
        verify(documentService).getAllDocuments(pageable);
        verify(documentMapper).modelToApi(document);
        assertEquals(1, result.size());
        assertEquals(documentAPI, result.getFirst());

    }

    @Test
    @WithMockUser
    void getAllDocumentsWithSearchingValue() {
        //given
        Document document = new Document(null, DocumentGroup.COST, DocumentType.DEBIT_NOTE, "1/C/2025", LocalDate.of(2025, 02, 11), LocalDate.of(2025, 02, 11), LocalDate.of(2025, 02, 20), "Fundacja Pielka-Kość i syn s.c.", null, BigDecimal.valueOf(200), BigDecimal.valueOf(4.6), BigDecimal.valueOf(204.6), BigDecimal.valueOf(204.6), Currency.getInstance("USD"), null, null, null);

        DocumentAPI documentAPI = new DocumentAPI(null, DocumentGroup.COST, DocumentType.DEBIT_NOTE, "1/C/2025", LocalDate.of(2025, 02, 11), LocalDate.of(2025, 02, 11), LocalDate.of(2025, 02, 20), "Fundacja Pielka-Kość i syn s.c.", null, BigDecimal.valueOf(200), BigDecimal.valueOf(4.6), BigDecimal.valueOf(204.6), BigDecimal.valueOf(204.6), Currency.getInstance("USD"), null, null, null);
        List<Document> list = List.of(document);

        int page = 1;
        int size = 30;
        String sortBy = "id";
        String value = "cost";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        when(documentService.getAllDocumentsByValue(pageable, value)).thenReturn(list);
        when(documentMapper.modelToApi(document)).thenReturn(documentAPI);

        //when
        var result = documentController.getAllDocuments(value, page, size, sortBy, "desc");

        //verify
        verify(documentService).getAllDocumentsByValue(pageable, value);
        verify(documentMapper).modelToApi(document);
        assertEquals(1, result.size());

    }

    @Test
    @WithMockUser
    void getAllDocumentsEmptyList() {
        //given
        List<Document> list = List.of();
        int page = 1;
        int size = 30;
        String sortBy = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        when(documentService.getAllDocuments(pageable)).thenReturn(list);

        //when
        var result = documentController.getAllDocuments(null, page, size, sortBy, "asc");

        //then
        verify(documentService).getAllDocuments(pageable);
        assertThat(result).isEqualTo(List.of());
        assertThat(result).isEqualTo(list);

    }


    @Test
    @WithMockUser
    void getDocumentByIdIfExist() {
        //given
        Long id = 1L;
        DocumentAPI documentAPI = new DocumentAPI();
        documentAPI.setId(id);
        Document document = new Document();
        document.setId(id);

        when(documentService.findById(id)).thenReturn(document);
        when(documentMapper.modelToApi(document)).thenReturn(documentAPI);


        //when
        var result = documentController.getDocumentById(id);

        //then
        verify(documentService).findById(id);
        assertThat(result).isEqualTo(documentAPI);
        assertThat(result.getId()).isEqualTo(documentAPI.getId());
    }

    @Test
    void getDocumentByIdIfNotExist() {
        //given
        Long id = -1L;
        DocumentAPI documentAPI = new DocumentAPI();

        doThrow(ResourceNotFoundException.class).when(documentService).findById(id);

        //when
        var result = assertThrows(ResourceNotFoundException.class, () -> documentController.getDocumentById(id));

        //then
        verify(documentService).findById(id);
    }

    @Test
    @WithMockUser
    void addDocument() {
        //given
        DocumentAPI documentAPI = new DocumentAPI();
        DocumentAPI savedDocumentAPI = new DocumentAPI();
        Document document = new Document();
        Document savedDocument = new Document();
        Long id = 1L;
        savedDocumentAPI.setId(id);
        savedDocument.setId(id);

        when(documentService.addDocument(document)).thenReturn(savedDocument);
        when(documentMapper.apiToModel(documentAPI)).thenReturn(document);
        when(documentMapper.modelToApi(savedDocument)).thenReturn(savedDocumentAPI);


        //when
        var result = documentController.addDocument(documentAPI);

        //then
        verify(documentService).addDocument(document);
        verify(documentMapper).apiToModel(documentAPI);
        assertThat(result.getId()).isEqualTo(savedDocumentAPI.getId());

    }

    @Test
    @WithMockUser
    void deleteDocumentByIdIfExist() {
        //given
        Document document = new Document();
        Long id = 1L;
        document.setId(id);

        doNothing().when(documentService).deleteDocument(id);

        //when
        var result = documentController.deleteDocumentById(id);

        //then
        verify(documentService).deleteDocument(id);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @WithMockUser
    void deleteDocumentByIdIfNotExist() {
        //given
        Long id = 1L;

        doThrow(ResourceNotFoundException.class).when(documentService).deleteDocument(id);

        //when
        var result = assertThrows(ResourceNotFoundException.class, () -> documentController.deleteDocumentById(id));

        //then
        verify(documentService).deleteDocument(id);
    }


    @Test
    @WithMockUser
    void updateDocumentByIdIfExist() {
        //given
        Document document = new Document();
        Long id = 1L;
        document.setId(id);

        doNothing().when(documentService).updateDocument(id, document);

        //when
        var result = documentController.updateDocumentById(id, document);

        //then
        verify(documentService).updateDocument(id, document);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @WithMockUser
    void updateDocumentByIdIfNotExist() {
        //given
        Document document = new Document();
        Long id = 1L;

        doThrow(ResourceNotFoundException.class).when(documentService).updateDocument(id, document);

        //when
        var result = assertThrows(ResourceNotFoundException.class, () -> documentController.updateDocumentById(id, document));

        //then
        verify(documentService).updateDocument(id, document);
    }


    @Test
    void filterFoundMatch() {
        //given
        DocumentFilterRequest documentFilterRequest = new DocumentFilterRequest();
        documentFilterRequest.setAmount(BigDecimal.valueOf(100));
        Document document = new Document();
        document.setAmount(BigDecimal.valueOf(100));
        document.setId(1L);
        DocumentAPI documentAPI = new DocumentAPI();
        documentAPI.setAmount(BigDecimal.valueOf(100));
        documentAPI.setId(1L);
        List<Document> list = List.of(document);
        List<DocumentAPI> listAPI = List.of(documentAPI);


        when(documentService.filter(documentFilterRequest)).thenReturn(list);
        when(documentMapper.modelToApi(document)).thenReturn(documentAPI);

        //when
        var result = documentController.filter(null, null, null, null, null, null, null, BigDecimal.valueOf(100), null, null, null, null, null, null, null);

        //then
        verify(documentService).filter(documentFilterRequest);
        assertThat(result.size()).isEqualTo(list.size());
        assertThat(result.getFirst().getId()).isEqualTo(1L);
    }
}