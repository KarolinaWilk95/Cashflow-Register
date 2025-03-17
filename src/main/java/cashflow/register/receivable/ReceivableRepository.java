package cashflow.register.receivable;

import cashflow.document.Document;
import cashflow.register.payables.Payable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Long> {

    @Transactional
    @Modifying
    @Query("delete from Receivable r where r.document.id = :id")
    void deleteDocument(@Param("id") Long id);

}
