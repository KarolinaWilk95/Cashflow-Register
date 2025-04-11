package cashflow.register.receivable;

import cashflow.document.Document;
import cashflow.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ReceivableService {

    private final ReceivableRepository receivableRepository;
    private final ReceivableMapper receivableMapper;


    public Receivable addDocument(Document newDocument) {
        Receivable receivable = new Receivable();
        receivable.setDocument(newDocument);
        return receivableRepository.save(receivable);
    }

    public List<Receivable> showAll() {
        return receivableRepository.findAll();
    }

    public Receivable findById(Long id) {
        var documentFromRepository = receivableRepository.findById(id);

        if (documentFromRepository.isEmpty()) {
            throw new ResourceNotFoundException("Selected document not found");
        }
        return documentFromRepository.get();
    }

    public void deleteByDocumentId(Long documentId) {
        receivableRepository.deleteDocument(documentId);
    }


    public void debtEnforcement(Receivable receivable) {
        receivableRepository.save(receivable);
    }


    public List<Receivable> createReportAboutOverdueReceivables() {
        return receivableRepository.createReportAboutOverdue();
    }


    public List<String> createReportAboutOverdueReceivablesGrouped() {
        return receivableRepository.createReportAboutOGrouped();
    }

    public List<Receivable> createReportAboutReceivablesAging() {

        return receivableRepository.createReportAging();
    }

    public BigDecimal receivablesSummary() {
        return receivableRepository.summary();
    }

    public List<Receivable> topValues() {
        return receivableRepository.topValues();
    }

    public List<String> topContractors() {
        return receivableRepository.topContractors();
    }
}
