package cashflow.register;

import cashflow.register.receivable.Receivable;

import java.util.List;

public interface FinancialReport<T> {

    List<T> createReportAboutOverdue();
    List<String> createReportAboutOGrouped();
    List<T> createReportAging();

}
