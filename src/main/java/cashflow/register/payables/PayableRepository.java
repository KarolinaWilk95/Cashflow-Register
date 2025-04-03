package cashflow.register.payables;

import cashflow.register.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayableRepository extends JpaRepository<Payable, Long>, FinancialReport<Payable> {

    @Modifying
    @Query("delete from Payable p where p.document.id = :id")
    void deleteDocument(@Param("id") Long id);

    @Query("select p from Payable p where p.document.id = :id")
    Optional<Payable> findDocument(@Param("id") Long id);

    @Query("select p from Payable p where (p.document.dueDate - CURRENT_DATE) <= 0 order by p.document.dueDate")
    List<Payable> createReportAboutOverdue();


    @Query("SELECT p.document.contractorName, sum(p.document.totalAmount) AS sum from Payable p where (p.document.dueDate - CURRENT_DATE) <= 0 group by p.document.contractorName order by sum")
    List<String> createReportAboutOGrouped();



    @Query("select p from Payable p order by p.document.dueDate")
    List<Payable> createReportAging();



}
