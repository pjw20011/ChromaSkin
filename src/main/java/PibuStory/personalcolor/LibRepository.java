package PibuStory.personalcolor;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface LibRepository extends MongoRepository<Lib, String> {

    @Query("{ 'color_type' : { $in: ?0 } }")
    List<Lib> findByColorTypeIn(List<String> colorTypes);
}
