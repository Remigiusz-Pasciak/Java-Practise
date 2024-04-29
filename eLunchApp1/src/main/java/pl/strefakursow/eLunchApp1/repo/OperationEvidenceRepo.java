package pl.strefakursow.eLunchApp1.repo;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.strefakursow.eLunchApp1.model.OperationEvidence;
import pl.strefakursow.eLunchApp1.model.User;

import java.math.BigDecimal;


@Repository
public interface OperationEvidenceRepo extends JpaRepository<OperationEvidence, Long> {

    @Query("SELECT COALESCE(SUM(" +
            "CASE " +
            " WHEN e.type = pl.strefakursow.eLunchApp1.model.enums.EvidenceType.DEPOSIT THEN e.amount " +
            " WHEN e.type = pl.strefakursow.eLunchApp1.model.enums.EvidenceType.WITHDRAW " +
            " or e.type = pl.strefakursow.eLunchApp1.model.enums.EvidenceType.PAYMENT THEN -e.amount" +
            " ELSE 0 " +
            " END" +
            "), 0 ) FROM OperationEvidence e where e.user = :user")
    BigDecimal getUserAccountBalance(@Param("user") User user);

}
