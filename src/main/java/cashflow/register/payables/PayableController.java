package cashflow.register.payables;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class PayableController {

    private final PayableService payableService;
    private final PayableMapper payableMapper;


    @GetMapping("api/payables")
    public List<PayableAPI> showAll() {
        var list = payableService.showAll();
        return list.stream().map(payableMapper::modelToApi).toList();
    }




}
