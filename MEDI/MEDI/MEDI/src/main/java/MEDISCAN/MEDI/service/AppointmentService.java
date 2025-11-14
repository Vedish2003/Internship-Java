package MEDISCAN.MEDI.service;

import MEDISCAN.MEDI.model.Appointment;
import MEDISCAN.MEDI.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // ✅ Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // ✅ Get appointments by patient name
    public List<Appointment> getAppointmentsByPatient(String patientName) {
        return appointmentRepository.findByPatientName(patientName);
    }

    // ✅ Save or update appointment
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    // ✅ Find appointment by ID
    public Appointment getAppointmentById(Integer id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    // ✅ Cancel appointment
    public void cancelAppointment(Integer id) {
        Appointment appointment = getAppointmentById(id);
        if (appointment != null && !"cancelled".equalsIgnoreCase(appointment.getStatus())) {
            appointment.setStatus("cancelled");
            appointmentRepository.save(appointment);
        }
    }
}
