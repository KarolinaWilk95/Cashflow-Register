package cashflow.register.payables;

import cashflow.document.Document;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private Document document;

}
