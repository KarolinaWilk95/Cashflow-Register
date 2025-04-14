package cashflow.register.analysis;

import cashflow.register.payables.Payable;
import cashflow.register.receivable.Receivable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class AnalysisAPI {

    private BigDecimal receivablesSummary;
    private List<Receivable> receivablesTopValues;
    private List<String> receivableTopContractors;
    private BigDecimal payablesSummary;
    private List<Payable> payablesTopValues;
    private List<String> payablesTopContractors;
    private BigDecimal income;
}
