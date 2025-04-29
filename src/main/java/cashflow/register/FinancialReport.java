package cashflow.register;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialReport<T> {

    List<T> createReportAboutOverdue();
    List<String> createReportAboutOGrouped();
    List<T> createReportAging();
    BigDecimal summary();
    List<T> topValues();
    List<String> topContractors();
}
