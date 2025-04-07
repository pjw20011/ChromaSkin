package PibuStory.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CosmeticsGreatService {

    private final CosmeticsGreatRepository cosmeticsGreatRepository;
    private final CosmeticsRepository cosmeticsRepository;

    @Autowired
    public CosmeticsGreatService(CosmeticsGreatRepository cosmeticsGreatRepository, CosmeticsRepository cosmeticsRepository) {
        this.cosmeticsGreatRepository = cosmeticsGreatRepository;
        this.cosmeticsRepository = cosmeticsRepository;
    }

    public int toggleCosmeticsGreat(String cosmeticsId, String nickname) {
        Optional<CosmeticsGreat> existingGreat = cosmeticsGreatRepository.findByCosmeticsIdAndNickname(cosmeticsId, nickname);
        Optional<Cosmetics> optionalCosmetics = cosmeticsRepository.findById(cosmeticsId);

        if (optionalCosmetics.isEmpty()) {
            throw new IllegalArgumentException("제품을 찾을 수 없습니다.");
        }

        Cosmetics cosmetics = optionalCosmetics.get();

        if (existingGreat.isPresent()) {
            cosmeticsGreatRepository.deleteByCosmeticsIdAndNickname(cosmeticsId, nickname);
            cosmetics.setGreat(cosmetics.getGreat() - 1);
        } else {
            CosmeticsGreat newGreat = new CosmeticsGreat();
            newGreat.setCosmeticsId(cosmeticsId);
            newGreat.setNickname(nickname);
            cosmeticsGreatRepository.save(newGreat);
            cosmetics.setGreat(cosmetics.getGreat() + 1);
        }

        cosmeticsRepository.save(cosmetics);
        return cosmetics.getGreat();
    }

    // 좋아요한 화장품 목록 조회(마이페이지에서 사용)
    public List<Cosmetics> findLikedCosmeticsByNickname(String nickname) {
        List<String> likedCosmeticsIds = cosmeticsGreatRepository.findByNickname(nickname)
                .stream()
                .map(CosmeticsGreat::getCosmeticsId)
                .collect(Collectors.toList());
        return cosmeticsRepository.findAllById(likedCosmeticsIds);
    }

    public List<String> getLikedCosmeticsByNickname(String nickname) {
        return cosmeticsGreatRepository.findByNickname(nickname)
                .stream()
                .map(CosmeticsGreat::getCosmeticsId)
                .collect(Collectors.toList());
    }
}
