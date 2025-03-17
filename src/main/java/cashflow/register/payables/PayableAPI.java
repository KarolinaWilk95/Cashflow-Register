package cashflow.register.payables;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PayableAPI {

    private Long id;
    private Long documentId;
    private String documentNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String contractorName;
    private BigDecimal totalAmount;
    private String currencyCode;
    private BigDecimal totalAmountInPln;


}
