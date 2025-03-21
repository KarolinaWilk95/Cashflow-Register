package cashflow.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentAPI {

    private Long id;

    private DocumentGroup documentGroup;

    private DocumentType documentType;

    private String documentNumber;

    private LocalDate saleDate;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private String contractorName;

    private Long contractorVATNumber;

    private BigDecimal amount;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountInPln;

    private String currencyCode;

    private String orderNumber;

    private LocalDate paymentDueDate;

    private BigDecimal paymentAmount;

    private BigDecimal amountDue;

}

