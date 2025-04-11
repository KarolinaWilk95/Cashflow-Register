package cashflow.register.receivable;

import cashflow.register.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Long>, JpaSpecificationExecutor<Receivable>, FinancialReport<Receivable> {

    @Modifying
    @Query("delete from Receivable r where r.document.id = :id")
    void deleteDocument(@Param("id") Long id);

    @Query("select r from Receivable r where r.document.id = :id")
    Optional<Receivable> findDocument(@Param("id") Long id);

    @Query("select r from Receivable r where (r.document.dueDate - CURRENT_DATE) <= 0 order by r.document.dueDate")
    List<Receivable> createReportAboutOverdue();


    @Query("SELECT r.document.contractorName, COUNT(r) as sale_count, SUM(r.document.totalAmount - r.document.paymentAmount) as unpaid from Receivable r where (r.document.dueDate - CURRENT_DATE) <= 0 group by r.document.contractorName order by unpaid DESC")
    List<String> createReportAboutOGrouped();


    @Query("select r from Receivable r order by r.document.dueDate")
    List<Receivable> createReportAging();

    @Query("select sum(r.document.totalAmount - r.document.paymentAmount) from Receivable r where (r.document.totalAmount - r.document.paymentAmount) > 0")
    BigDecimal summary();

    @Query("select r from Receivable r order by r.document.totalAmount DESC limit 3")
    List<Receivable> topValues();

    @Query("select r.document.contractorName, SUM(r.document.totalAmount - r.document.paymentAmount) as unpaid from Receivable r group by r.document.contractorName order by unpaid DESC limit 3")
    List<String> topContractors();
}
