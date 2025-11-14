package MEDISCAN.MEDI.repository;

import MEDISCAN.MEDI.model.Remedy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RemedyRepository extends JpaRepository<Remedy, Long> {
    Optional<Remedy> findBySymptomIgnoreCase(String symptom);
}
