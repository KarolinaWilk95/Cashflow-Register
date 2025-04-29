package cashflow.register.receivable;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Data
@AllArgsConstructor
public class ReceivableAPI {

    private Long id;

    private Long documentId;

    private LocalDate issueDate;

    private String documentNumber;

    private String contractorName;

    private LocalDate dueDate;

    private Long delayInDays;

    private BigDecimal totalAmount;

    private BigDecimal paymentAmount;

    private BigDecimal unpaidAmount;

    private BigDecimal totalAmountInPln;

    private Currency currencyCode;

    private BigDecimal amountDue;

    private String reminderNumber;

    private String demandForPaymentNumber;


}
