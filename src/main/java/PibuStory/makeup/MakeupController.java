package PibuStory.makeup;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MakeupController {

    @Autowired
    private LipstickRepository lipstickRepository;

    @Autowired
    private MakeupService makeupService;

    /**
     * 립스틱 데이터를 그룹화하여 뷰로 전달하며 세션 닉네임도 추가.
     */
    @GetMapping("/makeup")
    public String getMakeupPage(HttpSession session, Model model) {
        // 세션에서 닉네임 가져오기
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            nickname = "Guest"; // 닉네임이 없으면 기본값 설정
        }

        // 립스틱 데이터를 가져와 브랜드별로 그룹화
        List<Lipstick> lipsticks = lipstickRepository.findAll();
        Map<String, List<Lipstick>> groupedLipsticks = lipsticks.stream()
                .collect(Collectors.groupingBy(Lipstick::getBrand));

        // 모델에 데이터 추가
        model.addAttribute("nickname", nickname);
        model.addAttribute("groupedLipsticks", groupedLipsticks);

        return "makeup"; // makeup.html로 연결
    }

    /**
     * 파운데이션 저장 기능
     */
    @PostMapping("/save-foundation")
    public String saveFoundation(@RequestParam String foundationLabel,
                                 @RequestParam String color,
                                 HttpSession session) {
        // 세션에서 닉네임 가져오기
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            nickname = "Guest"; // 닉네임이 없으면 기본값 설정
        }

        // 파운데이션 정보 저장
        makeupService.saveFoundationSelection(foundationLabel, color, nickname);
        return "redirect:/makeup"; // 저장 후 페이지 새로고침
    }

    /**
     * 립스틱 저장 기능
     */
    @PostMapping("/save-lipstick")
    public String saveLipstick(@RequestParam String lipstickId,
                               @RequestParam String colorImage,
                               @RequestParam String lipName,
                               @RequestParam String lipBrand,
                               @RequestParam String lipColorCode,
                               HttpSession session) {
        // 세션에서 닉네임 가져오기
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            nickname = "Guest"; // 닉네임이 없으면 기본값 설정
        }

        // 립스틱 정보 저장
        makeupService.saveLipstickSelection(lipstickId, colorImage, lipName, lipBrand, lipColorCode, nickname);
        return "redirect:/makeup"; // 저장 후 페이지 새로고침
    }
}
