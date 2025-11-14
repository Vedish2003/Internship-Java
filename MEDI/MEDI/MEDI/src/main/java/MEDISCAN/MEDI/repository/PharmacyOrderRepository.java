package MEDISCAN.MEDI.repository;

import MEDISCAN.MEDI.model.PharmacyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyOrderRepository extends JpaRepository<PharmacyOrder, Integer> {}
