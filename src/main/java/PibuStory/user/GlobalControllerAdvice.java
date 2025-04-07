package PibuStory.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        // 세션에서 닉네임 정보를 가져와 모델에 추가
        String nickname = (String) session.getAttribute("nickname");
        model.addAttribute("nickname", nickname);
    }
}
