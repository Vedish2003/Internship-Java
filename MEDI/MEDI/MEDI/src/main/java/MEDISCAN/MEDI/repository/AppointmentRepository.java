package MEDISCAN.MEDI.repository;

import MEDISCAN.MEDI.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // ✅ For users: find appointments by patient name
    List<Appointment> findByPatientName(String patientName);

    // ✅ Optional (for doctor dashboard): find appointments by doctor name
    List<Appointment> findByDoctorName(String doctorName);

    // ✅ Optional (for admin filters): find appointments by status (BOOKED, CANCELLED, etc.)
    List<Appointment> findByStatus(String status);
}
