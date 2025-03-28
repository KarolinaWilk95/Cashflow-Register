package cashflow.dao;

import cashflow.document.DocumentGroup;
import cashflow.document.DocumentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DocumentFilterRequest {

    private String documentGroup;

    private String documentType;

    private String documentNumber;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private String contractorName;

    private Long contractorVatNumber;

    private BigDecimal amount;

    private BigDecimal lessThanAmount;

    private BigDecimal greaterThanAmount;

    private BigDecimal totalAmount;

    private BigDecimal lessThanTotalAmount;

    private BigDecimal greaterThanTotalAmount;

    private String currencyCode;

    private String orderNumber;

}
