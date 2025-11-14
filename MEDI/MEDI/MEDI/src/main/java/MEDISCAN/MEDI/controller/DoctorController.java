package MEDISCAN.MEDI.controller;

import MEDISCAN.MEDI.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    // ✅ Display all doctors
    @GetMapping("/doctors")
    public String showDoctors(Model model) {
        model.addAttribute("doctors", doctorRepository.findAll());
        return "doctors";  // Thymeleaf template name
    }
}
