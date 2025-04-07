package PibuStory.board;

import PibuStory.board.CommentGreat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentGreatRepository extends MongoRepository<PibuStory.board.CommentGreat, String> {

    // 특정 commentId와 nickname에 해당하는 좋아요 기록을 찾기 위한 메소드
    Optional<PibuStory.board.CommentGreat> findByCommentIdAndNickname(String commentId, String nickname);

    // 특정 commentId와 nickname으로 좋아요 기록을 삭제하기 위한 메소드
    void deleteByCommentIdAndNickname(String commentId, String nickname);

    // 닉네임으로 좋아요 누른 댓글 찾기
    List<CommentGreat> findByNickname(String nickname);
}
