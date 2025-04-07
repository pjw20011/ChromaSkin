package PibuStory.board;


import PibuStory.board.BoardGreat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoardGreatRepository extends MongoRepository<PibuStory.board.BoardGreat, String> {

    // 특정 boardId와 nickname에 해당하는 좋아요 기록을 찾기 위한 메소드
    Optional<PibuStory.board.BoardGreat> findByBoardIdAndNickname(String boardId, String nickname);

    // 특정 boardId와 nickname으로 좋아요 기록을 삭제하기 위한 메소드
    void deleteByBoardIdAndNickname(String boardId, String nickname);

    // 닉네임으로 좋아요 누른 대댓글 찾기
    List<BoardGreat> findByNickname(String nickname);


}
