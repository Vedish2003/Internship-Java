package MEDISCAN.MEDI.controller;

import MEDISCAN.MEDI.model.Appointment;
import MEDISCAN.MEDI.service.AppointmentService;
import MEDISCAN.MEDI.service.MedicineService;
import MEDISCAN.MEDI.service.PharmacyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private PharmacyOrderService orderService;

    @Autowired
    private AppointmentService appointmentService;

    // ✅ Admin Dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("medicines", medicineService.all());
        model.addAttribute("orders", orderService.allOrders());
        return "admin/dashboard";
    }

    // ✅ View all appointments
    @GetMapping("/admin/appointments")
    public String viewAppointments(Model model,
                                   @ModelAttribute("message") String message,
                                   @ModelAttribute("error") String error) {

        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);

        if (message != null && !message.isEmpty())
            model.addAttribute("message", message);

        if (error != null && !error.isEmpty())
            model.addAttribute("error", error);

        return "admin/appointments";
    }

    // ✅ Cancel appointment (Admin)
    @PostMapping("/admin/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable("id") Integer id,
                                    RedirectAttributes redirectAttributes) {
        Appointment appointment = appointmentService.getAllAppointments()
                .stream()
                .filter(a -> a.getAppointmentId().equals(id))
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            redirectAttributes.addFlashAttribute("error", "❌ Appointment not found!");
        } else if ("cancelled".equalsIgnoreCase(appointment.getStatus())) {
            redirectAttributes.addFlashAttribute("message", "⚠️ Already cancelled!");
        } else {
            appointment.setStatus("cancelled");
            appointmentService.saveAppointment(appointment);
            redirectAttributes.addFlashAttribute("message", "✅ Appointment cancelled successfully!");
        }

        return "redirect:/admin/appointments";
    }
}
