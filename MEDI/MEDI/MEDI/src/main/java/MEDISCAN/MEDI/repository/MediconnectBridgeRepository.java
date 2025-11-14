package MEDISCAN.MEDI.repository;

import MEDISCAN.MEDI.model.MediconnectBridge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MediconnectBridgeRepository extends JpaRepository<MediconnectBridge, Integer> {
    List<MediconnectBridge> findByHospitalNameContainingIgnoreCase(String hospitalName);
}
