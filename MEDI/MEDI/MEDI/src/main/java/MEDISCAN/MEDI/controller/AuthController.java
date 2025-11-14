package MEDISCAN.MEDI.controller;

import MEDISCAN.MEDI.model.User;
import MEDISCAN.MEDI.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ✅ Process login
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !user.getPasswordHash().equals(password)) {
            model.addAttribute("error", "Invalid Email or Password");
            return "login";
        }

        // ✅ Save user and role in session
        session.setAttribute("loggedInUser", user);
        session.setAttribute("role", user.getRole().name());

        // ✅ Redirect based on role
        if (user.getRole() == User.Role.admin) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/home";
        }
    }

    // ✅ Show register page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // ✅ Process registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // default role = patient (as set in your entity)
        userRepository.save(user);
        return "redirect:/login";
    }

    // ✅ Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
