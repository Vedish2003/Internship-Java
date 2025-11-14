package MEDISCAN.MEDI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String homePage() {
        return "home";
    }

    @GetMapping("/pharmacy")
    public String pharmacyPage() {
        return "pharmacy";
    }

    @GetMapping("/remedy")
    public String remedyPage() {
        return "remedy";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}
