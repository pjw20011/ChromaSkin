package PibuStory.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.ui.Model;

public class CsrfControllerAdvice{

    @ModelAttribute
    public void addCsrfToken(CsrfToken token, Model model) {
        if (token != null) {
            model.addAttribute("_csrf", token);
        }
    }
}
