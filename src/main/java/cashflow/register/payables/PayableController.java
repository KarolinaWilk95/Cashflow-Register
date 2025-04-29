package cashflow.register.payables;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class PayableController {

    private final PayableService payableService;
    private final PayableMapper payableMapper;
    private final ObjectMapper objectMapper;


    @GetMapping("api/register/payables")
    public List<PayableAPI> showAll() {
        var list = payableService.showAll();
        return list.stream().map(payableMapper::modelToApi).toList();
    }

    @GetMapping("api/register/payables/{id}")
    public PayableAPI findById(@PathVariable Long id) {
        var payable = payableService.findById(id);
        return payableMapper.modelToApi(payable);
    }

    @GetMapping("api/register/payables/overdue")
    public List<PayableAPI> createReportAboutOverduePayables() {

        var result = payableService.createReportAboutOverduePayables();

        return result.stream().map(payableMapper::modelToApi).toList();
    }

    @GetMapping("api/register/payables/overdue/grouped")
    public List<String> createReportAboutOverduePayablesGrouped() {

        return payableService.createReportAboutOverduePayablesGrouped();
    }

    @GetMapping("api/register/payables/aging")
    public List<PayableAPI> createReportAboutOverduePayablesAging() {

        return payableService.createReportAboutPayablesAging().stream().map(payableMapper::modelToApi).toList();
    }


}
