package cashflow.document;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Table(name = "documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "DocumentGroup is mandatory")
    private DocumentGroup documentGroup;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "DocumentType is mandatory")
    private DocumentType documentType;

    @Column
    @NotBlank(message = "Document number is mandatory")
    private String documentNumber;

    @Column
    @NotNull(message = "Sale date is mandatory")
    private LocalDate saleDate;

    @Column
    @NotNull(message = "Issue date is mandatory")
    private LocalDate issueDate;

    @Column
    private LocalDate dueDate;

    @Column
    private String contractorName;

    @Column
    @NotNull(message = "Contractor VAT number is mandatory")
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
    @NotNull(message = "Currency is mandatory")
    private Currency currencyCode;

    @Column
    private String orderNumber;

    @Column
    private LocalDate paymentDueDate;

    @Column
    private BigDecimal paymentAmount;


}

