package MEDISCAN.MEDI.controller;

import MEDISCAN.MEDI.model.Appointment;
import MEDISCAN.MEDI.model.User;
import MEDISCAN.MEDI.repository.AppointmentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // -------------------------------
    // 1️⃣ Show Appointment Booking Form
    // -------------------------------
    @GetMapping("/book-appointment")
    public String showBookingForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "book-appointment";
    }

    // -------------------------------
    // 2️⃣ Handle Appointment Booking Submission
    // -------------------------------
    @PostMapping("/book-appointment")
    public String saveAppointment(@ModelAttribute Appointment appointment,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        // ✅ Automatically assign logged-in user's name
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in first!");
            return "redirect:/login";
        }

        appointment.setPatientName(loggedInUser.getName());
        appointment.setStatus("BOOKED");
        appointmentRepository.save(appointment);

        redirectAttributes.addFlashAttribute("message", "✅ Appointment booked successfully!");
        return "redirect:/appointments";
    }

    // -------------------------------
    // 3️⃣ View Only Logged-in User's Appointments
    // -------------------------------
    @GetMapping("/appointments")
    public String viewAppointments(HttpSession session,
                                   Model model,
                                   @ModelAttribute("message") String message,
                                   @ModelAttribute("error") String error) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            model.addAttribute("error", "⚠️ Please log in to view your appointments.");
            return "login";
        }

        // ✅ Fetch only this user's appointments
        List<Appointment> userAppointments = appointmentRepository.findByPatientName(loggedInUser.getName());
        model.addAttribute("appointments", userAppointments);

        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }

        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }

        return "user/appointments"; // Thymeleaf page for user
    }

    // -------------------------------
    // 4️⃣ Cancel Appointment by ID (Only Own Appointments)
    // -------------------------------
    @PostMapping("/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable("id") Integer id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Please log in first!");
            return "redirect:/login";
        }

        Appointment appointment = appointmentRepository.findById(id).orElse(null);

        if (appointment == null || !appointment.getPatientName().equals(loggedInUser.getName())) {
            redirectAttributes.addFlashAttribute("error", "❌ You can only cancel your own appointments!");
            return "redirect:/appointments";
        }

        if ("cancelled".equalsIgnoreCase(appointment.getStatus())) {
            redirectAttributes.addFlashAttribute("message", "⚠️ Appointment already cancelled!");
        } else {
            appointment.setStatus("cancelled");
            appointmentRepository.save(appointment);
            redirectAttributes.addFlashAttribute("message", "✅ Appointment cancelled successfully!");
        }

        return "redirect:/appointments";
    }
}
