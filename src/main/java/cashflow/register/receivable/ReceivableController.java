package cashflow.register.receivable;

import cashflow.register.payables.PayableAPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReceivableController {

    private final ReceivableService receivableService;
    private final ReceivableMapper receivableMapper;

    @GetMapping("api/receivables")
    public List<ReceivableAPI> showAll() {
        var list = receivableService.showAll();
        return list.stream().map(receivableMapper::modelToApi).toList();
    }
}
