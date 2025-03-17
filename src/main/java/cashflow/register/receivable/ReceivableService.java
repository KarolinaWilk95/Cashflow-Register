package cashflow.register.receivable;

import cashflow.document.Document;
import cashflow.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReceivableService {

    private final ReceivableRepository receivableRepository;
    private final ReceivableMapper receivableMapper;


    public Receivable addDocument(Document newDocument) {
        return receivableRepository.save(receivableMapper.documentToReceivable(newDocument));
    }

    public List<Receivable> showAll() {
        return receivableRepository.findAll();
    }

    public void deleteDocument(Long id) {
        receivableRepository.deleteDocument(id);
    }
}
