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
    private final PayableMapper payableMapper;


    public List<Payable> showAll() {
        return payableRepository.findAll();
    }

    public Payable addDocument(Document newDocument) {
        Payable payable = new Payable();
        payable.setDocument(newDocument);
        return payableRepository.save(payable);
    }

    public void deleteDocument(Long id) {
        payableRepository.deleteDocument(id);
    }
}
