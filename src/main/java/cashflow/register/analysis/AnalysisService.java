package cashflow.register.analysis;

import cashflow.register.payables.PayableService;
import cashflow.register.receivable.ReceivableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnalysisService {

    private final PayableService payableService;
    private final ReceivableService receivableService;


    public Analysis analysis() {
        Analysis analysis = new Analysis();
        analysis.setReceivablesSummary(receivableService.receivablesSummary());
        analysis.setReceivablesTopValues(receivableService.topValues());
        analysis.setReceivableTopContractors(receivableService.topContractors());
        analysis.setPayablesSummary(payableService.payablesSummary());
        analysis.setPayablesTopValues(payableService.topValues());
        analysis.setPayablesTopContractors(payableService.topContractors());

        return analysis;
    }
}
