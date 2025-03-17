package cashflow.register.receivable;

import cashflow.document.Document;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Receivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;


    @Column
    private String reminderNumber;

    @Column
    private String demandForPaymentNumber;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private Document document;
}
