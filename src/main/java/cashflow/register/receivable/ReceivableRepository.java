package cashflow.register.receivable;

import cashflow.register.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Long>, JpaSpecificationExecutor<Receivable>, FinancialReport<Receivable> {

    @Transactional
    @Modifying
    @Query("delete from Receivable r where r.document.id = :id")
    void deleteDocument(@Param("id") Long id);

    @Query("select r from Receivable r where r.document.id = :id")
    Optional<Receivable> findDocument(@Param("id") Long id);

    @Query("select r from Receivable r where (r.document.dueDate - CURRENT_DATE) <= 0 order by r.document.dueDate")
    List<Receivable> createReportAboutOverdue();


    @Query("SELECT r.document.contractorName, sum(r.document.totalAmount) AS sum from Receivable r where (r.document.dueDate - CURRENT_DATE) <= 0 group by r.document.contractorName order by sum")
    List<String> createReportAboutOGrouped();



    @Query("select r from Receivable r order by r.document.dueDate")
    List<Receivable> createReportAging();
}
