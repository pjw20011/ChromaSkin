package PibuStory.product;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CosmeticsGreatRepository extends MongoRepository<CosmeticsGreat, String> {

    // 특정 화장품에 대해 특정 닉네임으로 좋아요를 눌렀는지 확인
    Optional<CosmeticsGreat> findByCosmeticsIdAndNickname(String cosmeticsId, String nickname);

    void deleteByCosmeticsIdAndNickname(String cosmeticsId, String nickname);

    List<CosmeticsGreat> findByNickname(String nickname);
}
