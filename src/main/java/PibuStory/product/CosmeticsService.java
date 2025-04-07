package PibuStory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CosmeticsService {

    private final CosmeticsRepository cosmeticsRepository;
    private final CosmeticsGreatRepository cosmeticsGreatRepository;

    public CosmeticsService(CosmeticsRepository cosmeticsRepository, CosmeticsGreatRepository cosmeticsGreatRepository) {
        this.cosmeticsRepository = cosmeticsRepository;
        this.cosmeticsGreatRepository = cosmeticsGreatRepository;
    }

    public Page<Cosmetics> findProducts(String cosmeticsType, String search, Pageable pageable) {
        if (cosmeticsType != null && !cosmeticsType.isEmpty() && search != null && !search.isEmpty()) {
            return cosmeticsRepository.findByCosmeticsTypeAndProductContaining(cosmeticsType, search, pageable);
        } else if (cosmeticsType != null && !cosmeticsType.isEmpty()) {
            return cosmeticsRepository.findByCosmeticsType(cosmeticsType, pageable);
        } else if (search != null && !search.isEmpty()) {
            return cosmeticsRepository.findByProductContaining(search, pageable);
        }
        return cosmeticsRepository.findAll(pageable);
    }

    // 좋아요 토글 기능
    public List<String> getLikedCosmeticsByNickname(String nickname) {
        return cosmeticsGreatRepository.findByNickname(nickname)
                .stream()
                .map(CosmeticsGreat::getCosmeticsId)
                .collect(Collectors.toList());
    }

    // ID로 제품 찾기 메서드 추가
    public Cosmetics findById(String id) {
        return cosmeticsRepository.findById(id).orElse(null);
    }
}
