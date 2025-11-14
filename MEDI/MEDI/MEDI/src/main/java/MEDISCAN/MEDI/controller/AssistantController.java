package MEDISCAN.MEDI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes("userLang") // Keeps selected language in session
public class AssistantController {

    private static final Map<String, String> remedies = new HashMap<>();

    static {
        // --- English Remedies ---
        remedies.put("cold_en", "For a cold: Stay hydrated, drink warm ginger tea, and take steam inhalation twice a day.");
        remedies.put("fever_en", "For fever: Rest well, drink plenty of fluids, and take paracetamol if your temperature is high.");
        remedies.put("headache_en", "For headache: Take rest, stay hydrated, and avoid bright light.");
        remedies.put("cough_en", "For cough: Drink warm water, honey with turmeric, and avoid cold drinks.");
        remedies.put("stomach pain_en", "For stomach pain: Eat light food, avoid spicy food, and take rest.");

        // --- Hindi Remedies ---
        remedies.put("cold_hi", "सर्दी के लिए: अदरक की चाय पिएं, भाप लें, और शरीर को गर्म रखें।");
        remedies.put("fever_hi", "बुखार के लिए: आराम करें, पर्याप्त पानी पिएं, और जरूरत पड़ने पर पैरासिटामोल लें।");
        remedies.put("headache_hi", "सिरदर्द के लिए: शांत जगह पर आराम करें और पर्याप्त पानी पिएं।");
        remedies.put("cough_hi", "खांसी के लिए: शहद और हल्दी लें, गर्म पानी पिएं, ठंडी चीज़ों से बचें।");
        remedies.put("stomach pain_hi", "पेट दर्द के लिए: हल्का खाना खाएं, मसालेदार भोजन से बचें और आराम करें।");

        // --- Kannada Remedies ---
        remedies.put("cold_kn", "ಜಲದೋಷಕ್ಕೆ: ಇಂಜಿನ ಕಷಾಯ ಕುಡಿಯಿರಿ, ಬಿಸಿ ನೀರಿನ ಆವಿಯನ್ನು ಎಳೆದುಕೊಳ್ಳಿ, ದೇಹವನ್ನು ಬಿಸಿ ಇರಿಸಿ.");
        remedies.put("fever_kn", "ಜ್ವರಕ್ಕೆ: ವಿಶ್ರಾಂತಿ ತೆಗೆದುಕೊಳ್ಳಿ, ಸಾಕಷ್ಟು ನೀರು ಕುಡಿಯಿರಿ, ಅಗತ್ಯವಿದ್ದರೆ ಪ್ಯಾರಾಸಿಟಮಾಲ್ ತೆಗೆದುಕೊಳ್ಳಿ.");
        remedies.put("headache_kn", "ತಲೆನೋವಿಗೆ: ಶಾಂತವಾದ ಸ್ಥಳದಲ್ಲಿ ವಿಶ್ರಾಂತಿ ತೆಗೆದುಕೊಳ್ಳಿ ಮತ್ತು ಸಾಕಷ್ಟು ನೀರು ಕುಡಿಯಿರಿ.");
        remedies.put("cough_kn", "ಕೆಮ್ಮಿಗೆ: ಬೆಚ್ಚಗಿನ ನೀರು ಕುಡಿಯಿರಿ, ತುಪ್ಪ ಮತ್ತು ಅರಿಶಿನ ಸೇವಿಸಿ, ತಂಪಾದ ಪಾನೀಯಗಳನ್ನು ತಪ್ಪಿಸಿ.");
        remedies.put("stomach pain_kn", "ಹೊಟ್ಟೆನೋವಿಗೆ: ಹಸಿವು ಕಡಿಮೆ ಆಹಾರ ಸೇವಿಸಿ, ಕಾರವಾದ ಆಹಾರದಿಂದ ದೂರವಿರಿ, ವಿಶ್ರಾಂತಿ ತೆಗೆದುಕೊಳ್ಳಿ.");
    }

    // ✅ Load Assistant Page
    @GetMapping("/ai-assistant")
    public String showAssistantPage(Model model) {
        if (!model.containsAttribute("userLang")) {
            model.addAttribute("userLang", "none");
        }
        return "ai-assistant"; // points to ai-assistant.html in templates/
    }

    // ✅ Default language
    @ModelAttribute("userLang")
    public String setDefaultLanguage() {
        return "none";
    }

    // ✅ Handle Chat Messages
    @PostMapping("/ask")
    @ResponseBody
    public String handleUserMessage(
            @RequestParam("message") String message,
            @ModelAttribute("userLang") String userLang,
            Model model
    ) {
        String lower = message.toLowerCase().trim();

        // 🟩 Step 1 — Language Selection
        if ("none".equals(userLang)) {
            if (lower.contains("english") || lower.contains("hindi") || lower.contains("kannada")
                    || lower.contains("ಹಿಂದಿ") || lower.contains("ಕನ್ನಡ")) {

                String selectedLang = "en";
                if (lower.contains("hindi") || lower.contains("हिंदी")) selectedLang = "hi";
                else if (lower.contains("kannada") || lower.contains("ಕನ್ನಡ")) selectedLang = "kn";

                model.addAttribute("userLang", selectedLang);

                return switch (selectedLang) {
                    case "hi" -> "भाषा चुनी गई: हिंदी 🇮🇳। अब बताइए, क्या परेशानी है?";
                    case "kn" -> "ಭಾಷೆ ಆಯ್ಕೆ ಮಾಡಲಾಗಿದೆ: ಕನ್ನಡ 🇮🇳. ನಿಮಗೆ ಏನು ಸಮಸ್ಯೆ ಇದೆ?";
                    default -> "Language set to English 🇬🇧. Please tell me your problem.";
                };
            }
            return "Hello 👋! I’m MediScan AI Assistant. Which language would you like to talk in — English, Hindi, or Kannada?";
        }

        // 🟩 Step 2 — Greetings
        if (lower.matches("^(hi|hello|hey|नमस्ते|ಹಲೋ|ಹಾಯ್|ನಮಸ್ಕಾರ).*")) {
            return switch (userLang) {
                case "hi" -> "नमस्ते 👋! मैं MediScan AI सहायक हूँ। कृपया बताइए कि आपको क्या परेशानी है?";
                case "kn" -> "ನಮಸ್ಕಾರ 👋! ನಾನು MediScan AI ಸಹಾಯಕ. ನಿಮಗೆ ಏನು ಸಮಸ್ಯೆ ಇದೆ?";
                default -> "Hello 👋! I’m MediScan AI Assistant. How are you feeling today?";
            };
        }

        // 🟩 Step 3 — Remedies
        if (lower.contains("cold") || lower.contains("सर्दी") || lower.contains("ಜಲದೋಷ"))
            return remedies.get("cold_" + userLang);
        else if (lower.contains("fever") || lower.contains("बुखार") || lower.contains("ಜ್ವರ"))
            return remedies.get("fever_" + userLang);
        else if (lower.contains("headache") || lower.contains("सिरदर्द") || lower.contains("ತಲೆನೋವು"))
            return remedies.get("headache_" + userLang);
        else if (lower.contains("cough") || lower.contains("खांसी") || lower.contains("ಕೆಮ್ಮು"))
            return remedies.get("cough_" + userLang);
        else if (lower.contains("stomach") || lower.contains("पेट") || lower.contains("ಹೊಟ್ಟೆ"))
            return remedies.get("stomach pain_" + userLang);

        // 🟩 Step 4 — Default reply
        return switch (userLang) {
            case "hi" -> "मुझे आपकी बात समझ नहीं आई। कृपया अपने लक्षण बताइए।";
            case "kn" -> "ಕ್ಷಮಿಸಿ, ನನಗೆ ಅರ್ಥವಾಗಲಿಲ್ಲ. ದಯವಿಟ್ಟು ನಿಮ್ಮ ಸಮಸ್ಯೆಯನ್ನು ವಿವರಿಸಿ.";
            default -> "I’m here to help! Could you please describe your symptoms?";
        };
    }

    // ✅ Reset Session (Language Reset)
    @GetMapping("/reset")
    @ResponseBody
    public String resetLanguage(SessionStatus status) {
        status.setComplete();
        return "Session reset ✅. Please choose your language again.";
    }
}
