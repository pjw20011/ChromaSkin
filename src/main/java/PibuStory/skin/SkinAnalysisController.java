package PibuStory.skin;

import PibuStory.user.User;
import PibuStory.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class SkinAnalysisController {

    // Python 스크립트 상대 경로
    private static final String SKIN_PYTHON_SCRIPT_PATH = "Demo_ver/python_scripts/skin-analysis.py";

    // 결과 이미지 저장 디렉토리 (상대 경로)
    private static final String SKIN_OUTPUT_DIR = "Demo_ver/Demo_ver/output_images/";

    // Python 인터프리터 절대 경로
    private static final String PYTHON_INTERPRETER = "python";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    /**
     * 이미지 업로드 폼을 제공하는 GET 요청 매핑
     */
    @GetMapping("/skin-type")
    public String showUploadForm() {
        return "skin-type";
    }

    /**
     * 이미지 업로드 및 Python 스크립트 실행을 처리하는 POST 요청 매핑
     */
//    @PostMapping("/run-skin_model")
//    public String runSkinAnalysis(
//            @RequestParam("image") MultipartFile image,
//            Model model,
//            HttpServletResponse response
//    ) {
//        try {
//            // 1. 이미지 업로드 및 Python 스크립트 실행 (기존 로직 유지)
//            String originalFilename = image.getOriginalFilename();
//            String uniqueFilename = "skin_" + UUID.randomUUID().toString() + "_" + originalFilename;
//            Path imagePath = Paths.get(SKIN_OUTPUT_DIR + uniqueFilename);
//            Files.createDirectories(imagePath.getParent());
//            Files.write(imagePath, image.getBytes());
//
//            String outputFilename = "result_skin_" + uniqueFilename;
//            Path outputPath = Paths.get(SKIN_OUTPUT_DIR + outputFilename);
//
//            ProcessBuilder processBuilder = new ProcessBuilder(
//                    PYTHON_INTERPRETER,
//                    new File(SKIN_PYTHON_SCRIPT_PATH).getAbsolutePath(),
//                    imagePath.toString(),
//                    outputPath.toString()
//            );
//
//            processBuilder.directory(new File(System.getProperty("user.dir")));
//            processBuilder.redirectErrorStream(true);
//            Process process = processBuilder.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder scriptOutput = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                scriptOutput.append(line).append("\n");
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("Python 스크립트 종료 코드: " + exitCode);
//            System.out.println("Python 스크립트 출력: " + scriptOutput.toString());
//
//            if (exitCode == 0) {
//                // 2. 피부 타입 추출
//                String skinType = extractSkinType(scriptOutput.toString());
//
//                if (skinType.equals("COMBINATION")){
//                    skinType = "복합성";
//                }
//                else if (skinType.equals("DRY")){
//                    skinType = "건성";
//                }
//                else if (skinType.equals("OILY")){
//                    skinType = "지성";
//                }
//                else{
//                    skinType = "UNKNOWN";
//                }
//                // 3. 각각의 cosmetics_type에 따른 추천 화장품 검색
//                List<Cosmetics> recommendationsSkin = recommendCosmeticsByType(skinType, "skin");
//                List<Cosmetics> recommendationsCleansing = recommendCosmeticsByType(skinType, "cleansing");
//
//                // 4. 추천 화장품을 모델에 추가
//                model.addAttribute("recommendationsSkin", recommendationsSkin);
//                model.addAttribute("recommendationsCleansing", recommendationsCleansing);
//
//                // 5. 결과 이미지 경로 (웹 접근 가능하도록)
//                String resultImagePath = "/output_images/" + outputFilename;
//
//                // 6. 모델에 결과 추가
//                model.addAttribute("skinType", skinType);
//                model.addAttribute("resultImage", resultImagePath);
//
//                // 7. 캐시 비활성화
//                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//                response.setHeader("Pragma", "no-cache");
//                response.setDateHeader("Expires", 0);
//
//                return "skin-result";
//            } else {
//                throw new RuntimeException("Python 스크립트 실행 중 오류가 발생했습니다: " + scriptOutput.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("message", "피부 분석 중 오류가 발생했습니다: " + e.getMessage());
//            return "error";
//        }
//    }
    @PostMapping("/run-skin_model")
    public String runSkinAnalysis(
            @RequestParam("image") MultipartFile image,
            @RequestParam("nickname") String nickname, // nickname을 요청으로 받음
            Model model,
            HttpServletResponse response
    ) {
        try {
            // 1. 이미지 업로드 및 Python 스크립트 실행 (기존 로직 유지)
            String originalFilename = image.getOriginalFilename();
            String uniqueFilename = "skin_" + UUID.randomUUID().toString() + "_" + originalFilename;
            Path imagePath = Paths.get(SKIN_OUTPUT_DIR + uniqueFilename);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            String outputFilename = "result_skin_" + uniqueFilename;
            Path outputPath = Paths.get(SKIN_OUTPUT_DIR + outputFilename);

            ProcessBuilder processBuilder = new ProcessBuilder(
                    PYTHON_INTERPRETER,
                    new File(SKIN_PYTHON_SCRIPT_PATH).getAbsolutePath(),
                    imagePath.toString(),
                    outputPath.toString()
            );

            processBuilder.directory(new File(System.getProperty("user.dir")));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder scriptOutput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                scriptOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            System.out.println("Python 스크립트 종료 코드: " + exitCode);
            System.out.println("Python 스크립트 출력: " + scriptOutput.toString());

            if (exitCode == 0) {
                // 2. 피부 타입 추출
                String skinType = extractSkinType(scriptOutput.toString());

                if (skinType.equals("COMBINATION")) {
                    skinType = "복합성";
                } else if (skinType.equals("DRY")) {
                    skinType = "건성";
                } else if (skinType.equals("OILY")) {
                    skinType = "지성";
                } else {
                    skinType = "UNKNOWN";
                }

                // 3. 사용자 정보 업데이트
                User user = userRepository.findByNickname(nickname); // nickname을 기반으로 사용자 검색
                if (user != null) {
                    user.setSkinType(skinType); // 피부 타입 저장
                    user.setSkinLastTestDate(java.time.LocalDate.now().toString()); // 검사 날짜 저장
                    userRepository.save(user); // DB에 저장
                }

                // 4. 각각의 cosmetics_type에 따른 추천 화장품 검색
                List<Cosmetics> recommendationsSkin = recommendCosmeticsByType(skinType, "skin");
                List<Cosmetics> recommendationsCleansing = recommendCosmeticsByType(skinType, "cleansing");

                // 5. 추천 화장품을 모델에 추가
                model.addAttribute("recommendationsSkin", recommendationsSkin);
                model.addAttribute("recommendationsCleansing", recommendationsCleansing);

                // 6. 결과 이미지 경로 (웹 접근 가능하도록)
                String resultImagePath = "/output_images/" + outputFilename;

                // 7. 모델에 결과 추가
                model.addAttribute("skinType", skinType);
                model.addAttribute("resultImage", resultImagePath);

                // 8. 캐시 비활성화
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);

                return "skin-result";
            } else {
                throw new RuntimeException("Python 스크립트 실행 중 오류가 발생했습니다: " + scriptOutput.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "피부 분석 중 오류가 발생했습니다: " + e.getMessage());
            return "error";
        }
    }





    /**
     * Python 스크립트의 출력에서 피부 타입을 추출하는 메서드
     *
     * @param scriptOutput Python 스크립트의 콘솔 출력
     * @return 추출된 피부 타입 문자열
     */
    private String extractSkinType(String scriptOutput) {
        String skinType = "UNKNOWN";
        String[] lines = scriptOutput.split("\n");
        for (String line : lines) {
            if (line.startsWith("Skin Type:")) {
                skinType = line.replace("Skin Type:", "").trim();
                break;
            }
        }
        return skinType;
    }

    /**
     * 피부 타입에 따라 추천 화장품을 검색하는 메서드
     *
     * @param skinType 피부 타입 문자열
     * @return 추천 화장품 리스트
     */
    private List<Cosmetics> recommendCosmeticsByType(String skinType, String cosmeticsType) {
        List<String> goodIngredients = List.of();

        if ("건성".equalsIgnoreCase(skinType)) {
            goodIngredients = List.of(
                    "히아루론산", "글리세린", "프로필렌 글라이콜", "소디움PCA", "1,3-부틸렌 글라이콘", "비타민E", "비타민A",
                    "비타민C", "콜라겐", "엘라스틴", "아보카도 오일", "이브닝 프라임 로즈 오일", "오트밀 단백질", "콩 추출물",
                    "카모마일", "오이", "복숭아", "해조 추출물", "상백피 추출물", "코직산", "알부틴", "포토씨 추출물",
                    "베타카로틴", "시어버터", "파일워트 추출물", "비타민B 복합체", "판테놀"
            );
        } else if ("지성".equalsIgnoreCase(skinType)) {
            goodIngredients = List.of(
                    "글리콜린산", "살리실산", "난 옥시놀-9", "녹차", "위치 하젤", "레몬", "캄파", "멘톨", "클로로필",
                    "알라토인", "티트리", "감초", "징크 옥사이트", "칼렌듈라 추출물", "설퍼", "트리클로잔", "티타늄 옥사이드"
            );
        } else if ("복합성".equalsIgnoreCase(skinType)) {
            // 복합성의 경우 이마와 코, 볼 부분에 맞는 성분을 각각 설정
            List<String> oilyIngredients = List.of(
                    "글리콜린산", "살리실산", "난 옥시놀-9", "녹차", "위치 하젤", "레몬", "캄파", "멘톨", "클로로필",
                    "알라토인", "티트리", "감초", "징크 옥사이트", "칼렌듈라 추출물", "설퍼", "트리클로잔", "티타늄 옥사이드"
            );

            List<String> dryIngredients = List.of(
                    "히아루론산", "글리세린", "프로필렌 글라이콜", "소디움PCA", "1,3-부틸렌 글라이콘", "비타민E", "비타민A",
                    "비타민C", "콜라겐", "엘라스틴", "아보카도 오일", "이브닝 프라임 로즈 오일", "오트밀 단백질", "콩 추출물",
                    "카모마일", "오이", "복숭아", "해조 추출물", "상백피 추출물", "코직산", "알부틴", "포토씨 추출물",
                    "베타카로틴", "시어버터", "파일워트 추출물", "비타민B 복합체", "판테놀"
            );

            // 이마와 코 부분을 위한 지성 화장품 추천
            String oilyRegex = oilyIngredients.stream()
                    .map(Pattern::quote)
                    .collect(Collectors.joining("|"));
            Criteria oilyCriteria = Criteria.where("ingredient").regex(oilyRegex, "i")
                    .and("cosmetics_type").is(cosmeticsType)
                    .and("oily").gt(0.4);
            Query oilyQuery = new Query(oilyCriteria).limit(7).with(
                    org.springframework.data.domain.Sort.by(
                            org.springframework.data.domain.Sort.Order.desc("review_count"),
                            org.springframework.data.domain.Sort.Order.desc("rating_average")
                    )
            );
            List<Cosmetics> oilyCosmeticsList = mongoTemplate.find(oilyQuery, Cosmetics.class, "cosmetics");

            // 양쪽 볼 부분을 위한 건성 화장품 추천
            String dryRegex = dryIngredients.stream()
                    .map(Pattern::quote)
                    .collect(Collectors.joining("|"));
            Criteria dryCriteria = Criteria.where("ingredient").regex(dryRegex, "i")
                    .and("cosmetics_type").is(cosmeticsType)
                    .and("dry").gt(0.5);
            Query dryQuery = new Query(dryCriteria).limit(7).with(
                    org.springframework.data.domain.Sort.by(
                            org.springframework.data.domain.Sort.Order.desc("review_count"),
                            org.springframework.data.domain.Sort.Order.desc("rating_average")
                    )
            );
            List<Cosmetics> dryCosmeticsList = mongoTemplate.find(dryQuery, Cosmetics.class, "cosmetics");

            // 각각의 추천 리스트에서 랜덤으로 5개씩 선택
            Collections.shuffle(oilyCosmeticsList);
            Collections.shuffle(dryCosmeticsList);

            List<Cosmetics> recommendedCosmetics = new ArrayList<>();
            recommendedCosmetics.addAll(oilyCosmeticsList.stream().limit(5).collect(Collectors.toList()));
            recommendedCosmetics.addAll(dryCosmeticsList.stream().limit(5).collect(Collectors.toList()));

            return recommendedCosmetics;
        }

        // 복합성이 아닌 경우 기존 로직 수행
        String regex = goodIngredients.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));

        Criteria criteria = Criteria.where("ingredient").regex(regex, "i")
                .and("cosmetics_type").is(cosmeticsType);

        if ("건성".equalsIgnoreCase(skinType)) {
            criteria = criteria.and("dry").gt(0.5);
        } else if ("지성".equalsIgnoreCase(skinType)) {
            criteria = criteria.and("oily").gt(0.3);
        }

        Query query = new Query(criteria);
        query.limit(7).with(org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Order.desc("review_count"),
                org.springframework.data.domain.Sort.Order.desc("rating_average")
        ));

        List<Cosmetics> cosmeticsList = mongoTemplate.find(query, Cosmetics.class, "cosmetics");

        // 랜덤으로 5개 반환

        return cosmeticsList.stream().limit(5).collect(Collectors.toList());
    }

}