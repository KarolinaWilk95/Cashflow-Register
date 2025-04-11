package cashflow.document;

import cashflow.dao.DocumentFilterRequest;
import cashflow.dao.DocumentFilterRequestRepository;
import cashflow.document.currency_converter.CurrencyConverterService;
import cashflow.exception.ResourceNotFoundException;
import cashflow.register.payables.PayableService;
import cashflow.register.receivable.ReceivableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private static final String DEFAULT_SORTING_METHOD = "ASC";
    private static final String CURRENCY_CODE_PLN = "PLN";

    private final DocumentRepository documentRepository;
    private final PayableService payableService;
    private final ReceivableService receivableService;
    private final CurrencyConverterService converterService;
    private final DocumentFilterRequestRepository documentFilterRequestRepository;


    public Page<Document> getAllDocuments(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }


    public Page<Document> getAllDocumentsByValue(Pageable pageable, String search) {

        return documentRepository.findAllDocuments(search, pageable);

    }

    public Document findById(Long id) {
        var documentFromRepository = documentRepository.findById(id);
        if (documentFromRepository.isPresent()) {
            return documentFromRepository.get();
        } else {
            throw new ResourceNotFoundException("Selected document not found");
        }
    }


    @Transactional
    public Document addDocument(Document newDocument) {

        Document savedDocument = new Document();
        String currencyCode = String.valueOf(newDocument.getCurrencyCode());

        if (CURRENCY_CODE_PLN.equals(currencyCode)) {
            savedDocument = saveDocument(newDocument);

        } else {
            newDocument.setTotalAmountInPln(converterService.purchaseRate(newDocument.getCurrencyCode(), newDocument.getTotalAmount()));
            savedDocument = saveDocument(newDocument);
        }
        return savedDocument;
    }

    @Transactional
    public void deleteDocument(Long id) {
        Optional<Document> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isEmpty()) {
            throw new ResourceNotFoundException("Selected document not found");
        }

        if (optionalDocument.get().getDocumentGroup().equals(DocumentGroup.COST)) {
            payableService.deleteByDocumentId(id);
        }
        if (optionalDocument.get().getDocumentGroup().equals(DocumentGroup.SALE)) {
            receivableService.deleteByDocumentId(id);
        }

        documentRepository.deleteById(id);

    }

    @Transactional
    public void updateDocument(Long id, Document document) {
        Optional<Document> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isEmpty()) {
            throw new ResourceNotFoundException("Selected document not found");
        }

        Document updatedDocument = optionalDocument.get();
        updatedDocument.setDocumentGroup(document.getDocumentGroup());
        updatedDocument.setDocumentType(document.getDocumentType());
        updatedDocument.setDocumentNumber(document.getDocumentNumber());
        updatedDocument.setSaleDate(document.getSaleDate());
        updatedDocument.setIssueDate(document.getIssueDate());
        updatedDocument.setDueDate(document.getDueDate());
        updatedDocument.setContractorName(document.getContractorName());
        updatedDocument.setContractorVatNumber(document.getContractorVatNumber());
        updatedDocument.setAmount(document.getAmount());
        updatedDocument.setTaxAmount(document.getTaxAmount());
        updatedDocument.setTotalAmount(document.getTotalAmount());
        updatedDocument.setCurrencyCode(document.getCurrencyCode());
        updatedDocument.setOrderNumber(document.getOrderNumber());
        updatedDocument.setPaymentDueDate(document.getPaymentDueDate());
        updatedDocument.setPaymentAmount(document.getPaymentAmount());

        documentRepository.save(updatedDocument);
    }

    public List<Document> filter(DocumentFilterRequest documentFilterRequest) {

        return documentFilterRequestRepository.findByCriteria(documentFilterRequest);
    }

    @Transactional
    public void partialUpdateDocument(Document document, Long id) {
        var documentFromRepository = documentRepository.findById(id);
        if (documentFromRepository.isPresent()) {
            documentRepository.save(document);
        }
    }


    private Document saveDocument(Document newDocument) {

        var savedDocument = documentRepository.save(newDocument);

        if (!newDocument.getPaymentAmount().equals(newDocument.getTotalAmount())) {
            if (newDocument.getDocumentGroup().equals(DocumentGroup.COST)) {
                payableService.addDocument(savedDocument);
            }

            if (newDocument.getDocumentGroup().equals(DocumentGroup.SALE)) {
                receivableService.addDocument(savedDocument);
            }
        }
        return savedDocument;
    }


}

