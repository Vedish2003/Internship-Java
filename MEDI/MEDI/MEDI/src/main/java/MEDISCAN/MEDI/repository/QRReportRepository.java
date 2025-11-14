package MEDISCAN.MEDI.repository;

import MEDISCAN.MEDI.model.QRReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRReportRepository extends JpaRepository<QRReport, Long> {
    Optional<QRReport> findByUserId(String userId);
}
