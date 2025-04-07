package PibuStory.board;

import PibuStory.board.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<PibuStory.board.Comment, String> {

    // 게시글에 따른 댓글 조회
    List<PibuStory.board.Comment> findByBoardId(String boardId);

    // 대댓글 조회
    List<PibuStory.board.Comment> findByParentCommentId(String parentCommentId);

    // 게시글 삭제 및 댓글 삭제
    void deleteByBoardId(String boardId);

    // 작성자에 따른 댓글 조회(내가 댓글 단 글 조회 기능 목적)
    List<Comment> findByAuthor(String author);

}
