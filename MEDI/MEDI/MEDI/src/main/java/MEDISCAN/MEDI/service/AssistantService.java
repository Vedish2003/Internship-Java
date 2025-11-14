package MEDISCAN.MEDI.service;

import MEDISCAN.MEDI.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {

    public ChatResponse getReply(String userMessage) {
        String reply;

        if (userMessage == null || userMessage.trim().isEmpty()) {
            reply = "Please say something so I can respond!";
        } else if (userMessage.toLowerCase().contains("hello")) {
            reply = "Hi there! How can I help you today?";
        } else if (userMessage.toLowerCase().contains("time")) {
            reply = "The current time is: " + java.time.LocalTime.now();
        } else if (userMessage.toLowerCase().contains("date")) {
            reply = "Today's date is: " + java.time.LocalDate.now();
        } else {
            reply = "I'm a simple AI Assistant. You said: '" + userMessage + "'";
        }

        return new ChatResponse(reply);
    }
}
