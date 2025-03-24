package cashflow.document;

import cashflow.document.currency_converter.CurrencyConverterService;
import cashflow.exception.ResourceNotFoundException;
import cashflow.register.payables.PayableService;
import cashflow.register.receivable.ReceivableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final PayableService payableService;
    private final ReceivableService receivableService;
    private final DocumentMapper documentMapper;
    private final CurrencyConverterService converterService;


    public List<Document> showAll() {
        return documentRepository.findAll();
    }

    public Document findById(Long id) {
        var document = documentRepository.findById(id);
        if (document.isPresent()) {
            return document.get();
        } else {
            throw new ResourceNotFoundException("Selected document not found");
        }
    }


    public Document saveDocument(Document newDocument) {
        var savedDocument = documentRepository.save(newDocument);

        if (!newDocument.getPaymentAmount().equals(newDocument.getTotalAmount())) {
            if (newDocument.getDocumentGroup().equals(DocumentGroup.COST)) {
                payableService.addDocument(newDocument);
            }

            if (newDocument.getDocumentGroup().equals(DocumentGroup.SALE)) {
                receivableService.addDocument(newDocument);
            }

        }
        return savedDocument;
    }

    @Transactional
    public Document addDocument(Document newDocument) {

        Document savedDocument = new Document();

        if (newDocument.getCurrencyCode().equals("PLN")) {
            saveDocument(newDocument);

        } else {
            newDocument.setTotalAmountInPln(converterService.purchaseRate(newDocument.getCurrencyCode(), newDocument.getTotalAmount()));
            saveDocument(newDocument);
        }
        return savedDocument;
    }

    @Transactional
    public void deleteDocument(Long id) {
        Optional<Document> documentOptional = documentRepository.findById(id);

        if (documentOptional.isEmpty()) {
            throw new ResourceNotFoundException("Selected document not found");
        }

        if (documentOptional.get().getDocumentGroup().equals(DocumentGroup.COST)) {
            payableService.deleteDocument(id);
        }
        if (documentOptional.get().getDocumentGroup().equals(DocumentGroup.SALE)) {
            receivableService.deleteDocument(id);
        }

        documentRepository.deleteById(id);

    }

    public void updateDocument(Long id, Document document) {
        Optional<Document> documentOptional = documentRepository.findById(id);

        if (documentOptional.isEmpty()) {
            throw new ResourceNotFoundException("Selected document not found");
        }

        Document doc = documentOptional.get();
        doc.setId(id);
        doc.setDocumentGroup(document.getDocumentGroup());
        doc.setDocumentType(document.getDocumentType());
        doc.setDocumentNumber(document.getDocumentNumber());
        doc.setSaleDate(document.getSaleDate());
        doc.setIssueDate(document.getIssueDate());
        doc.setDueDate(document.getDueDate());
        doc.setContractorName(document.getContractorName());
        doc.setContractorVatNumber(document.getContractorVatNumber());
        doc.setAmount(document.getAmount());
        doc.setTaxAmount(document.getTaxAmount());
        doc.setTotalAmount(document.getTotalAmount());
        doc.setCurrencyCode(document.getCurrencyCode());
        doc.setOrderNumber(document.getOrderNumber());
        doc.setPaymentDueDate(document.getPaymentDueDate());
        doc.setPaymentAmount(document.getPaymentAmount());
        doc.setAmountDue(document.getAmountDue());

        documentRepository.save(doc);
    }

    public void partialUpdateDocument(Document document, Long id) {
        var doc = documentRepository.findById(id);
        if (doc.isPresent()) {
            documentRepository.save(document);
        }
    }
}

