package cashflow.register.payables;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Data
public class PayableAPI {

    private Long id;
    private Long documentId;
    private String documentNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String contractorName;
    private BigDecimal totalAmount;
    private BigDecimal paymentAmount;
    private BigDecimal unpaidAmount;
    private Currency currencyCode;
    private BigDecimal totalAmountInPln;


}
