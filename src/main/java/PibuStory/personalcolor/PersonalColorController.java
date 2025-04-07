package PibuStory.personalcolor;

import PibuStory.user.User;
import PibuStory.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PersonalColorController {

    @GetMapping("/personal-color")
    public String showPersonalColorTest() {
        return "personal-color"; // personal-color.html 페이지로 연결
    }

    private static final String PYTHON_INTERPRETER = "python";

    @PostMapping("/test-color")
    public String testColor(@RequestParam("image") MultipartFile image, Model model, HttpServletResponse response) throws Exception {
        // Upload image to temporary file
        Path tempFile = Files.createTempFile("uploaded_", ".jpg");
        Files.write(tempFile, image.getBytes());

        List<String> resultImages = new ArrayList<>();
        String staticDir = "Demo_Ver/Demo_Ver/src/main/resources/static/output_image/";

        // Create output_images directory if it doesn't exist
        Path outputDirPath = Paths.get(staticDir);
        if (!Files.exists(outputDirPath)) {
            Files.createDirectories(outputDirPath);
        }

        String skinRecommendation = null;
        String hairRecommendation = null;
        String eyeRecommendation = null;

        for (int colorKey = 1; colorKey <= 4; colorKey++) {
            String uniqueFileName = "image_" + colorKey + ".jpg";
            Path outputPath = Paths.get(staticDir + uniqueFileName);

            try {
                // Log before running the Python script
                System.out.println("Running Python script for colorKey: " + colorKey);
                ProcessBuilder processBuilder = new ProcessBuilder(
                        PYTHON_INTERPRETER, "Demo_ver/python_scripts/personal_color_test.py", tempFile.toString(), String.valueOf(colorKey), outputPath.toString()
                );
                processBuilder.redirectErrorStream(true);

                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                    if (line.contains("Skin:")) {
                        skinRecommendation = line.split(": ")[1];
                    } else if (line.contains("Hair:")) {
                        hairRecommendation = line.split(": ")[1];
                    } else if (line.contains("Eye:")) {
                        eyeRecommendation = line.split(": ")[1];
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    // Add image path to result list
                    String imagePath = "output_image/" + uniqueFileName;
                    System.out.println("Static Directory Path: " + staticDir);
                    System.out.println("Output Path: " + outputPath.toString());
                    resultImages.add(imagePath);
                } else {
                    throw new RuntimeException("Error in Python script: " + result.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Python script execution failed for colorKey: " + colorKey, e);
            }
        }

        // **캐시 비활성화 설정**
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        model.addAttribute("resultImages", resultImages);
        model.addAttribute("skinRecommendation", skinRecommendation);
        model.addAttribute("hairRecommendation", hairRecommendation);
        model.addAttribute("eyeRecommendation", eyeRecommendation);

        return "personal-color-question"; // Direct to 'personal-color-question.html' page to show the results
    }

//    @PostMapping("/submit-answers")
//    public String submitAnswers(
//            @RequestParam("q1") String q1,
//            @RequestParam("q2") String q2,
//            @RequestParam("q3") String q3,
//            @RequestParam("q4") String q4,
//            @RequestParam("q5") String q5,
//            @RequestParam("q6") String q6,
//            @RequestParam("q7") String q7,
//            @RequestParam("q8") String q8,
//            @RequestParam("q9") String q9,
//            Model model) {
//
//        // A, B, C, D 선택 횟수
//        int aCount = 0, bCount = 0, cCount = 0, dCount = 0;
//
//        // 질문에 대한 응답을 배열로 저장
//        String[] answers = {q1, q2, q3, q4, q5, q6, q7, q8, q9};
//        for (String answer : answers) {
//            String[] selectedValues = answer.split(",");  // 쉼표로 값을 분리
//            for (String value : selectedValues) {
//                switch (value.trim()) {  // 공백 제거 후 값에 따라 카운트 증가
//                    case "A": aCount++; break;
//                    case "B": bCount++; break;
//                    case "C": cCount++; break;
//                    case "D": dCount++; break;
//                }
//            }
//        }
//
//        // 선택된 값에 따라 결과 결정
//        String finalResult;
//        if (aCount >= bCount && aCount >= cCount && aCount >= dCount) {
//            finalResult = "spring";
//        } else if (bCount >= aCount && bCount >= cCount && bCount >= dCount) {
//            finalResult = "summer";
//        } else if (cCount >= aCount && cCount >= bCount && cCount >= dCount) {
//            finalResult = "autumn";
//        } else {
//            finalResult = "winter";
//        }
//
//        // 결과 페이지로 리다이렉트
//        return "redirect:/" + finalResult + "-result";  // 예: /spring-result
//    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/submit-answers")
    public String submitAnswers(
            @RequestParam("q1") String q1,
            @RequestParam("q2") String q2,
            @RequestParam("q3") String q3,
            @RequestParam("q4") String q4,
            @RequestParam("q5") String q5,
            @RequestParam("q6") String q6,
            @RequestParam("q7") String q7,
            @RequestParam("q8") String q8,
            @RequestParam("q9") String q9,
            @SessionAttribute("nickname") String nickname, // 세션에서 nickname 가져옴
            Model model
    ) {
        // A, B, C, D 선택 횟수
        int aCount = 0, bCount = 0, cCount = 0, dCount = 0;

        // 질문에 대한 응답을 배열로 저장
        String[] answers = {q1, q2, q3, q4, q5, q6, q7, q8, q9};
        for (String answer : answers) {
            String[] selectedValues = answer.split(","); // 쉼표로 값을 분리
            for (String value : selectedValues) {
                switch (value.trim()) { // 공백 제거 후 값에 따라 카운트 증가
                    case "A": aCount++; break;
                    case "B": bCount++; break;
                    case "C": cCount++; break;
                    case "D": dCount++; break;
                }
            }
        }

        // 선택된 값에 따라 결과 결정
        String finalResult;
        String finalResultSave;
        if (aCount >= bCount && aCount >= cCount && aCount >= dCount) {
            finalResult = "spring";
            finalResultSave = "봄 웜톤";
        } else if (bCount >= aCount && bCount >= cCount && bCount >= dCount) {
            finalResult = "summer";
            finalResultSave = "여름 쿨톤";
        } else if (cCount >= aCount && cCount >= bCount && cCount >= dCount) {
            finalResult = "autumn";
            finalResultSave = "가을 웜톤";
        } else {
            finalResult = "winter";
            finalResultSave = "겨울 쿨톤";
        }

        // 사용자 정보 업데이트
        User user = userRepository.findByNickname(nickname); // nickname으로 사용자 검색
        if (user != null) {
            user.setPersonalColor(finalResultSave); // 퍼스널 컬러 저장
            user.setPersonalLastTestDate(java.time.LocalDate.now().toString()); // 검사 날짜 저장
            userRepository.save(user); // DB에 저장
        }

        // 결과 페이지로 리다이렉트
        return "redirect:/" + finalResult + "-result"; // 예: /spring-result
    }

    /**
     * 퍼스널 컬러 계산 로직
     */
    private String calculatePersonalColor(String... answers) {
        int aCount = 0, bCount = 0, cCount = 0, dCount = 0;

        for (String answer : answers) {
            String[] selectedValues = answer.split(","); // 쉼표로 분리
            for (String value : selectedValues) {
                switch (value.trim()) {
                    case "A": aCount++; break;
                    case "B": bCount++; break;
                    case "C": cCount++; break;
                    case "D": dCount++; break;
                }
            }
        }

        // 최다 선택 항목에 따른 결과 결정
        if (aCount >= bCount && aCount >= cCount && aCount >= dCount) {
            return "spring";
        } else if (bCount >= aCount && bCount >= cCount && bCount >= dCount) {
            return "summer";
        } else if (cCount >= aCount && cCount >= bCount && cCount >= dCount) {
            return "autumn";
        } else {
            return "winter";
        }
    }


    @Autowired
    private LibService libService;

    @Autowired
    private LibRepository libRepository;


    @GetMapping("/spring-result")
    public String getSpringLipProducts(Model model) {
        // 4개의 봄 웜톤 립 제품을 가져옴
        List<Lib> lipProducts = libService.getSpringLipProducts(6);
        model.addAttribute("lipProducts", lipProducts);
        return "spring-result";
    }

    @GetMapping("/summer-result")
    public String getSummerLipProducts(Model model) {
        // 4개의 여름 쿨톤 립 제품을 가져옴
        List<Lib> lipProducts = libService.getSummerLipProducts(6);
        model.addAttribute("lipProducts", lipProducts);
        return "summer-result";
    }


    @GetMapping("/autumn-result")
    public String getAutumnLipProducts(Model model) {
        // 4개의 가을 웜톤 립 제품을 가져옴
        List<Lib> lipProducts = libService.getAutumnLipProducts(6);
        model.addAttribute("lipProducts", lipProducts);
        return "autumn-result";
    }

    @GetMapping("/winter-result")
    public String getWinterLipProducts(Model model) {
        // 4개의 겨울 쿨톤 립 제품을 가져옴
        List<Lib> lipProducts = libService.getWinterLipProducts(6);
        model.addAttribute("lipProducts", lipProducts);
        return "winter-result";
    }


}