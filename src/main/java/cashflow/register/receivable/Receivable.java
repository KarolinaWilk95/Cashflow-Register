package cashflow.register.receivable;

import cashflow.document.Document;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "receivables")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;


    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Column
    private String reminderNumber;

    @Column
    private String demandForPaymentNumber;
}


//(cascade = CascadeType.ALL, orphanRemoval = true)