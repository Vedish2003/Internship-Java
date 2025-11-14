package MEDISCAN.MEDI.controller;

import MEDISCAN.MEDI.model.MediconnectBridge;
import MEDISCAN.MEDI.service.MediconnectBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bridge")
public class MediconnectBridgeController {

    @Autowired
    private MediconnectBridgeService bridgeService;

    // ✅ 1. View all hospitals
    @GetMapping("/all")
    public String viewAllHospitals(Model model) {
        List<MediconnectBridge> hospitals = bridgeService.getAllHospitals();
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("searchQuery", ""); // so page loads without error
        return "bridge"; // templates/bridge.html
    }

    // ✅ 2. View single hospital details by ID
    @GetMapping("/view/{id}")
    public String viewHospitalDetails(@PathVariable("id") int id, Model model) {
        Optional<MediconnectBridge> hospitalOpt = bridgeService.getHospitalById(id);

        if (hospitalOpt.isPresent()) {
            MediconnectBridge hospital = hospitalOpt.get();
            model.addAttribute("hospital", hospital);
            return "hospital_details"; // templates/hospital_details.html
        } else {
            // If not found, show error message
            model.addAttribute("error", "Hospital not found!");
            return "redirect:/bridge/all";
        }
    }

    // ✅ 3. Search hospitals by name
    @GetMapping("/search")
    public String searchHospitals(@RequestParam("name") String name, Model model) {
        if (name == null || name.trim().isEmpty()) {
            // Redirect to all hospitals if search is blank
            return "redirect:/bridge/all";
        }

        List<MediconnectBridge> hospitals = bridgeService.searchHospitalsByName(name.trim());
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("searchQuery", name);

        if (hospitals.isEmpty()) {
            model.addAttribute("message", "No hospitals found matching '" + name + "'.");
        }

        return "bridge"; // templates/bridge.html (shows filtered results)
    }
}
