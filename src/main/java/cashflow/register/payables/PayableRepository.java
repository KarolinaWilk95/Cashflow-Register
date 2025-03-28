package cashflow.register.payables;

import cashflow.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PayableRepository extends JpaRepository<Payable, Long> {

    @Modifying
    @Query("delete from Payable p where p.document.id = :id")
    void deleteDocument(@Param("id") Long id);

    @Query("select p from Payable p where p.document.id = :id")
    Optional<Payable> findDocument(@Param("id") Long id);



}
