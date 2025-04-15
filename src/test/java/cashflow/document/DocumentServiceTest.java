package cashflow.document;

import cashflow.document.currency_converter.CurrencyConverterService;
import cashflow.exception.ResourceNotFoundException;
import cashflow.register.payables.PayableService;
import cashflow.register.receivable.ReceivableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {


    @Mock
    DocumentRepository documentRepository;
    @Mock
    PayableService payableService;
    @Mock
    ReceivableService receivableService;
    @Mock
    CurrencyConverterService converterService;

    @InjectMocks
    DocumentService documentService;

    @Test
    void getAllDocuments() {
        //given
        Document document = new Document();
        document.setId(1L);
        List<Document> list = List.of(document);
        Page<Document> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 10);

        when(documentRepository.findAll(pageable)).thenReturn(page);

        //when
        var result = documentService.getAllDocuments(pageable);

        //then
        verify(documentRepository).findAll(pageable);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getId()).isEqualTo(1L);
    }

    @Test
    void getAllDocumentsByValue() {
        //given
        Document document = new Document();
        document.setId(1L);
        document.setContractorName("XYZ");
        String value = "xyz";
        List<Document> list = List.of(document);
        Page<Document> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 10);

        when(documentRepository.findAllDocuments(value, pageable)).thenReturn(page);

        //when
        var result = documentService.getAllDocumentsByValue(pageable, value);

        //then
        verify(documentRepository).findAllDocuments(value, pageable);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getContractorName()).isEqualTo("XYZ");
    }

    @Test
    void findByIdIfExist() {
        //given
        Long id = 1L;
        Document document = new Document();
        document.setId(id);

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));

        //when
        var result = documentService.findById(id);

        //then
        verify(documentRepository).findById(id);
        assertThat(result.getId()).isEqualTo(1);
        assertEquals(result, document);
    }

    @Test
    void findByIdIfNotExist() {
        //given
        Long id = 1L;

        doThrow(ResourceNotFoundException.class).when(documentRepository).findById(id);

        //when
        var result = assertThrows(ResourceNotFoundException.class, () -> documentService.findById(id));

        //then
        verify(documentRepository).findById(id);
    }

    @Test
    void addDocumentIfCurrencyCodePLN() {
        //given
        Document document = new Document();
        document.setTotalAmount(BigDecimal.valueOf(100));
        document.setTotalAmountInPln(BigDecimal.valueOf(100));
        document.setCurrencyCode(Currency.getInstance("PLN"));
        document.setPaymentAmount(BigDecimal.valueOf(0));
        document.setDocumentGroup(DocumentGroup.COST);
        Document savedDocument = new Document();
        savedDocument.setTotalAmount(BigDecimal.valueOf(100));
        savedDocument.setTotalAmountInPln(BigDecimal.valueOf(100));
        savedDocument.setCurrencyCode(Currency.getInstance("PLN"));
        savedDocument.setPaymentAmount(BigDecimal.valueOf(0));
        savedDocument.setDocumentGroup(DocumentGroup.COST);


        when(documentRepository.save(document)).thenReturn(savedDocument);

        //when
        var result = documentService.addDocument(document);

        //then
        verify(documentRepository).save(document);
        assertEquals(document, result);
    }

    @Test
    void addDocumentIfCurrencyCodeUSD() {
        //given
        Document document = new Document();
        document.setTotalAmount(BigDecimal.valueOf(100));
        document.setCurrencyCode(Currency.getInstance("USD"));
        document.setPaymentAmount(BigDecimal.valueOf(0));
        document.setDocumentGroup(DocumentGroup.COST);
        Document savedDocument = new Document();
        savedDocument.setTotalAmount(BigDecimal.valueOf(100));
        savedDocument.setCurrencyCode(Currency.getInstance("USD"));
        savedDocument.setPaymentAmount(BigDecimal.valueOf(0));
        savedDocument.setDocumentGroup(DocumentGroup.COST);
        BigDecimal totalValueInPln = document.getTotalAmount().multiply(BigDecimal.valueOf(3.78));
        savedDocument.setTotalAmountInPln(totalValueInPln);

        when(converterService.purchaseRate(document.getCurrencyCode(), document.getTotalAmount())).thenReturn(totalValueInPln);
        when(documentRepository.save(document)).thenReturn(savedDocument);

        //when
        var result = documentService.addDocument(document);

        //then
        verify(converterService).purchaseRate(document.getCurrencyCode(), document.getTotalAmount());
        verify(documentRepository).save(document);
        assertThat(result.getTotalAmountInPln()).isEqualTo(totalValueInPln);
        assertThat(result.getTotalAmount()).isEqualTo(document.getTotalAmount());
    }

    @Test
    void deleteDocumentIfExist() {
        //given
        Long id = 1L;
        Document document = new Document();
        document.setId(id);
        document.setDocumentGroup(DocumentGroup.COST);

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        doNothing().when(documentRepository).deleteById(id);

        //when
        documentService.deleteDocument(id);

        //then
        verify(documentRepository).findById(id);
        verify(documentRepository).deleteById(id);
    }

    @Test
    void deleteDocumentIfNotExist() {
        //given
        Long id = 1L;

        doThrow(ResourceNotFoundException.class).when(documentRepository).findById(id);

        //when
        var result = assertThrows(ResourceNotFoundException.class, () -> documentService.deleteDocument(id));

        //then
        verify(documentRepository).findById(id);
    }

    @Test
    void updateDocumentIfExist() {
        //given
        Long id = 1L;
        Document document = new Document();
        document.setId(id);
        document.setTotalAmount(BigDecimal.valueOf(100));


        Document updatedDocument = new Document();
        updatedDocument.setId(id);
        updatedDocument.setTotalAmount(BigDecimal.valueOf(5000));

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(documentRepository.save(updatedDocument)).thenReturn(updatedDocument);

        //when
        documentService.updateDocument(id, updatedDocument);

        //then
        verify(documentRepository).findById(id);
        verify(documentRepository).save(updatedDocument);
        assertThat(document.getTotalAmount()).isEqualTo(updatedDocument.getTotalAmount());
    }

    @Test
    void updateDocumentIfNotExist() {
        Long id = 1L;
        Document updatedDocument = new Document();

        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        //when
        var result = assertThrows(ResourceNotFoundException.class, () -> documentService.updateDocument(id, updatedDocument));

        //then
        verify(documentRepository).findById(id);
    }

}