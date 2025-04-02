package cashflow.register.payables;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Data
public class PayableAPI {

    private Long id;
    private Long documentId;
    private LocalDate issueDate;
    private String documentNumber;
    private String contractorName;

    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private BigDecimal paymentAmount;
    private BigDecimal unpaidAmount;
    private BigDecimal totalAmountInPln;

    private Currency currencyCode;


}
