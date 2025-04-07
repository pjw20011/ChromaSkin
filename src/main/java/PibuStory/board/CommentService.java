package PibuStory.board;

import PibuStory.board.Comment;
import PibuStory.board.CommentRepository;
import PibuStory.board.Reply;
import PibuStory.board.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository; // ReplyRepository 주입

    // 댓글 저장
    public PibuStory.board.Comment saveComment(PibuStory.board.Comment comment) {
        return commentRepository.save(comment);
    }

    // 부모 댓글에 자식 댓글 추가
    public void addReplyToComment(String parentCommentId, PibuStory.board.Reply reply) {
        PibuStory.board.Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow();
        parentComment.addChildCommentId(reply.getId()); // 대댓글 ID 추가
        commentRepository.save(parentComment);
    }

    public void deleteCommentById(String commentId) {
        commentRepository.deleteById(commentId);
    }


    // 댓글 목록을 boardId로 조회하는 메서드
    public List<PibuStory.board.Comment> getCommentsByBoardId(String boardId) {
        System.out.println("Fetching comments for boardId: " + boardId); // 로그 추가
        return commentRepository.findByBoardId(boardId);
    }

    // 특정 댓글의 자식 댓글을 childrenCommentIds를 통해 조회하는 메서드
    public List<Reply> getReplies(List<String> childrenCommentIds) {
        return replyRepository.findAllById(childrenCommentIds); // childrenCommentIds로 모든 대댓글 조회
    }

    // 특정 댓글 좋아요 조회 메소드
    public Optional<PibuStory.board.Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);
    }
    
    // 내가 댓글 단 글 조회 기능
    public List<Comment> getCommentsByAuthor(String author) {
        return commentRepository.findByAuthor(author);
    }
}
