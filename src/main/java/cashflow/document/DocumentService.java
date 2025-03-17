package cashflow.document;

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

    public List<Document> showAll() {
        return documentRepository.findAll();
    }

    @Transactional
    public Document addDocument(Document newDocument) {
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

    }
}
