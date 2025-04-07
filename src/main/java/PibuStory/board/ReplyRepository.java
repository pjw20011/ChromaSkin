package PibuStory.board;

import PibuStory.board.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReplyRepository extends MongoRepository<PibuStory.board.Reply, String> {

    // 대댓글 조회
    List<PibuStory.board.Reply> findByCommentId(String commentId);

    // 특정 commentId의 모든 대댓글을 삭제
    void deleteByCommentId(String commentId);

    // 작성자에 따른 대댓글 조회(내가 대댓글 단 글 조회 기능 목적)
    List<Reply> findByAuthor(String author);

}
