package PibuStory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CosmeticsRepository extends MongoRepository<Cosmetics, String> {

    // 특정 cosmeticsType과 product로 검색 (페이징 지원)
    Page<Cosmetics> findByCosmeticsTypeAndProductContaining(String cosmeticsType, String product, Pageable pageable);

    // 특정 cosmeticsType으로 검색 (페이징 지원)
    Page<Cosmetics> findByCosmeticsType(String cosmeticsType, Pageable pageable);

    // 제품 이름으로만 검색 (페이징 지원)
    Page<Cosmetics> findByProductContaining(String product, Pageable pageable);

    // 전체 제품 조회 (페이징 및 정렬 지원)
    Page<Cosmetics> findAll(Pageable pageable);
}




