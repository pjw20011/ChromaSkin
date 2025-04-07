package PibuStory.board;


import PibuStory.board.ReplyGreat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyGreatRepository extends MongoRepository<PibuStory.board.ReplyGreat, String> {

    // 특정 replyId와 nickname에 해당하는 좋아요 기록을 찾기 위한 메소드
    Optional<PibuStory.board.ReplyGreat> findByReplyIdAndNickname(String replyId, String nickname);

    // 특정 replyId와 nickname으로 좋아요 기록을 삭제하기 위한 메소드
    void deleteByReplyIdAndNickname(String replyId, String nickname);

    // 닉네임으로 좋아요 누른 대댓글 찾기
    List<ReplyGreat> findByNickname(String nickname);
}