package cashflow.document;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Table(name = "documents")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private DocumentGroup documentGroup;

    @Column
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column
    private String documentNumber;

    @Column
    private LocalDate saleDate;

    @Column
    private LocalDate issueDate;

    @Column
    private LocalDate dueDate;

    @Column
    private String contractorName;

    @Column
    private Long contractorVatNumber;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal taxAmount;

    @Column
    private BigDecimal totalAmount;

    @Column
    private BigDecimal totalAmountInPln;

    @Column
    private Currency currencyCode;

    @Column
    private String orderNumber;

    @Column
    private LocalDate paymentDueDate;

    @Column
    private BigDecimal paymentAmount;




}

