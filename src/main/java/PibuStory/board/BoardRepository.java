package PibuStory.board;


import PibuStory.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BoardRepository extends MongoRepository<PibuStory.board.Board, String> {

    @Query("{ 'subject': { $regex: ?0, $options: 'i' } }")
    Page<PibuStory.board.Board> findBySubjectContaining(String subject, Pageable pageable);

    // 마이페이지 내가 좋아요 누른 글 기능에 사용
    List<PibuStory.board.Board> findByIdIn(List<String> ids);

    // 내가 작성한 글 가져오기
    @Query("{ 'writer': ?0 }")
    List<Board> findByWriter(String writer);

}