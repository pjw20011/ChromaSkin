//package PibuStory.personalcolor; // 패키지 선언 추가
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class LibService {
//
//    private final LibRepository libRepository;
//
//    @Autowired
//    public LibService(LibRepository libRepository) {
//        this.libRepository = libRepository;
//    }
//
//    // 봄 웜톤 립 제품 추천 (peach, coral)
//    public List<Lib> getSpringLipProducts(int count) {
//        List<Lib> lipProducts = libRepository.findByColorTypeIn(List.of("peach", "coral"));
//        return lipProducts.stream().limit(count).collect(Collectors.toList());
//    }
//
//    // 여름 쿨톤 립 제품 추천 (rose,mob)
//    public List<Lib> getSummerLipProducts(int count) {
//        List<Lib> lipProducts = libRepository.findByColorTypeIn(List.of("rose","mob"));
//        return lipProducts.stream().limit(count).collect(Collectors.toList());
//    }
//
//    // 가을 웜톤 립 제품 추천 (brown, orange)
//    public List<Lib> getAutumnLipProducts(int count) {
//        List<Lib> lipProducts = libRepository.findByColorTypeIn(List.of("brown", "orange"));
//        return lipProducts.stream().limit(count).collect(Collectors.toList());
//    }
//
//    // 겨울 쿨톤 립 제품 추천 (red, mob)
//    public List<Lib> getWinterLipProducts(int count) {
//        List<Lib> lipProducts = libRepository.findByColorTypeIn(List.of("red", "mob"));
//        return lipProducts.stream().limit(count).collect(Collectors.toList());
//    }
//}
//

package PibuStory.personalcolor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LibService {

    private final LibRepository libRepository;

    // 색상 타입 맵핑
    private static final Map<String, List<String>> SEASON_COLOR_MAP = Map.of(
            "spring", List.of("peach", "coral"),
            "summer", List.of("rose", "mob"),
            "autumn", List.of("brown", "orange"),
            "winter", List.of("red", "mob")
    );

    @Autowired
    public LibService(LibRepository libRepository) {
        this.libRepository = libRepository;
    }

    // 특정 계절에 해당하는 립 제품 추천
    public List<Lib> getLipProductsBySeason(String season, int count) {
        // 계절에 해당하는 색상 타입 가져오기
        List<String> colorTypes = SEASON_COLOR_MAP.get(season.toLowerCase());
        if (colorTypes == null || colorTypes.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 계절 이름입니다: " + season);
        }
        // 색상 타입에 따라 제품 검색 및 제한
        List<Lib> lipProducts = libRepository.findByColorTypeIn(colorTypes);
        return lipProducts.stream().limit(count).collect(Collectors.toList());
    }

    // 봄 웜톤 립 제품 추천
    public List<Lib> getSpringLipProducts(int count) {
        return getLipProductsBySeason("spring", count);
    }

    // 여름 쿨톤 립 제품 추천
    public List<Lib> getSummerLipProducts(int count) {
        return getLipProductsBySeason("summer", count);
    }

    // 가을 웜톤 립 제품 추천
    public List<Lib> getAutumnLipProducts(int count) {
        return getLipProductsBySeason("autumn", count);
    }

    // 겨울 쿨톤 립 제품 추천
    public List<Lib> getWinterLipProducts(int count) {
        return getLipProductsBySeason("winter", count);
    }
}
