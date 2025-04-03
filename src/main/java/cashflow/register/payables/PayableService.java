package cashflow.register.payables;

import cashflow.document.Document;
import cashflow.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayableService {

    private final PayableRepository payableRepository;

    public List<Payable> showAll() {
        return payableRepository.findAll();
    }

    public Payable addDocument(Document newDocument) {
        Payable payable = new Payable();
        payable.setDocument(newDocument);
        return payableRepository.save(payable);
    }

    public void deleteByDocumentId(Long documentId) {
        payableRepository.deleteDocument(documentId);
    }

    public Payable findById(Long id) {
        var documentFromRepository = payableRepository.findById(id);

        if (documentFromRepository.isEmpty()) {
            throw new ResourceNotFoundException("Selected document not found");
        }
        return documentFromRepository.get();
    }

    public void debtEnforcement(Payable payable) {
        payableRepository.save(payable);
    }


    public List<Payable> createReportAboutOverduePayables() {
        return payableRepository.createReportAboutOverdue();
    }


    public List<String> createReportAboutOverduePayablesGrouped() {
        return payableRepository.createReportAboutOGrouped();
    }

    public List<Payable> createReportAboutPayablesAging() {

        return payableRepository.createReportAging();
    }
}
