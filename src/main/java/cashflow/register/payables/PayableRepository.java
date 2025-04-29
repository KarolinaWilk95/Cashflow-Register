package cashflow.register.payables;

import cashflow.register.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayableRepository extends JpaRepository<Payable, Long>, FinancialReport<Payable> {

    @Query("select p from Payable p where (p.document.totalAmount > p.document.paymentAmount)")
    List<Payable> findAllUnpaidDocuments();


    @Modifying
    @Query("delete from Payable p where p.document.id = :id")
    void deleteDocument(@Param("id") Long id);

    @Query("select p from Payable p where p.document.id = :id")
    Optional<Payable> findDocument(@Param("id") Long id);

    @Query("select p from Payable p where (p.document.dueDate - CURRENT_DATE) <= 0 order by p.document.dueDate")
    List<Payable> createReportAboutOverdue();


    @Query("SELECT p.document.contractorName, COUNT(p) as sale_count, SUM(p.document.totalAmount - p.document.paymentAmount) as unpaid from Payable p where (p.document.dueDate - CURRENT_DATE) <= 0 group by p.document.contractorName order by unpaid DESC")
    List<String> createReportAboutOGrouped();


    @Query("select p from Payable p order by p.document.dueDate")
    List<Payable> createReportAging();


    @Query("select sum(p.document.totalAmount - p.document.paymentAmount) from Payable p where (p.document.totalAmount - p.document.paymentAmount) > 0")
    BigDecimal summary();

    @Query("select p from Payable p order by p.document.totalAmount DESC limit 3")
    List<Payable> topValues();

    @Query("select p.document.contractorName, SUM(p.document.totalAmount - p.document.paymentAmount) as unpaid from Payable p group by p.document.contractorName order by unpaid DESC limit 3")
    List<String> topContractors();
}
