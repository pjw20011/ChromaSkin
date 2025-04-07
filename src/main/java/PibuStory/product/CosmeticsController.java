package PibuStory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cosmetics")
public class CosmeticsController {

    private final CosmeticsService cosmeticsService;
    private final CosmeticsGreatService cosmeticsGreatService;

    public CosmeticsController(CosmeticsService cosmeticsService, CosmeticsGreatService cosmeticsGreatService) {
        this.cosmeticsService = cosmeticsService;
        this.cosmeticsGreatService = cosmeticsGreatService;
    }

    // 제품 목록 페이지 (기존 메서드)
    @GetMapping("/products")
    public String getProducts(
            @RequestParam(required = false) String cosmeticsType,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "price_after") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "1") int page,
            @SessionAttribute("nickname") String nickname,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, getSort(sort, order));

        if ("all".equals(cosmeticsType) || cosmeticsType == null) {
            cosmeticsType = null;
        }

        Page<Cosmetics> products = cosmeticsService.findProducts(cosmeticsType, search, pageable);

        List<String> likedCosmetics = cosmeticsGreatService.getLikedCosmeticsByNickname(nickname);

        model.addAttribute("products", products);
        model.addAttribute("totalProducts", products.getTotalElements());
        model.addAttribute("currentCosmeticsType", cosmeticsType);
        model.addAttribute("search", search);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("likedCosmetics", likedCosmetics); // 좋아요 누른 화장품 목록 전달

        return "product-list";
    }

    // 제품 상세 페이지 (추가된 메서드)
    @GetMapping("/product-detail/{id}")
    public String getProductDetail(@PathVariable String id, Model model) {
        Cosmetics product = cosmeticsService.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for ID: " + id);
        }
        model.addAttribute("product", product);
        return "product-detail";
    }

    // 좋아요 토글 API
    @PostMapping("/toggle-cosmetics-great")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleCosmeticsGreat(
            @RequestParam String cosmeticsId,
            @SessionAttribute(name = "nickname", required = false) String nickname) { // 세션값 없으면 null 처리

        System.out.println("화장품 좋아요 기능 호출");
        System.out.println("Received cosmeticsId: " + cosmeticsId);
        System.out.println("Received nickname: " + nickname);

        // nickname 값 확인
        if (nickname == null || nickname.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "닉네임 세션 값이 없습니다."));
        }

        // cosmeticsId 값 확인
        if (cosmeticsId == null || cosmeticsId.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "cosmeticsId가 유효하지 않습니다."));
        }

        try {
            // 좋아요 상태 업데이트
            int updatedGreatCount = cosmeticsGreatService.toggleCosmeticsGreat(cosmeticsId, nickname);

            // 좋아요 상태 확인
            boolean liked = cosmeticsGreatService.getLikedCosmeticsByNickname(nickname).contains(cosmeticsId);

            System.out.println("화장품 좋아요 기능 활성화 - CosmeticsId: " + cosmeticsId + ", Nickname: " + nickname);

            Map<String, Object> response = Map.of(
                    "liked", liked,
                    "great", updatedGreatCount
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.err.println("에러 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "제품을 찾을 수 없습니다."));
        }
    }

    // 좋아요한 화장품 목록 조회 API(마이페이지에서 사용)
    @GetMapping("/my-page/liked-cosmetics")
    @ResponseBody
    public List<Cosmetics> getLikedCosmeticsByNickname(@SessionAttribute(name = "nickname", required = false) String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("로그인된 사용자가 아닙니다.");
        }
        return cosmeticsGreatService.findLikedCosmeticsByNickname(nickname);
    }


    private Sort getSort(String sort, String order) {
        // "review_count", "rating_average", "great"가 들어오면 내림차순으로, 그 외에는 order 값에 따라 설정
        Sort.Direction direction = ("review_count".equalsIgnoreCase(sort) || "rating_average".equalsIgnoreCase(sort) || "great".equalsIgnoreCase(sort))
                ? Sort.Direction.DESC
                : ("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC);
        return Sort.by(direction, sort);
    }

}