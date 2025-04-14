package cashflow.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private Currency currencyCode;

    private String orderNumber;

    private LocalDate paymentDueDate;

    private BigDecimal paymentAmount;



}

