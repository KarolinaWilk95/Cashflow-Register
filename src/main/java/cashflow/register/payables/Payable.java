package cashflow.register.payables;

import cashflow.document.Document;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payables")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

}

//(cascade = CascadeType.ALL, orphanRemoval = true)
