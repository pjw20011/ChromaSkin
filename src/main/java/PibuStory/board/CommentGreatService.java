package PibuStory.board;

import PibuStory.board.Comment;
import PibuStory.board.CommentGreat;
import PibuStory.board.CommentGreatRepository;
import PibuStory.board.CommentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentGreatService {

    private final CommentGreatRepository commentGreatRepository;
    private final PibuStory.board.CommentRepository commentRepository;

    public CommentGreatService(CommentGreatRepository commentGreatRepository, CommentRepository commentRepository) {
        this.commentGreatRepository = commentGreatRepository;
        this.commentRepository = commentRepository;
    }

    public int toggleCommentGreat(String commentId, String nickname) {
        Optional<PibuStory.board.CommentGreat> existingCommentGreat = commentGreatRepository.findByCommentIdAndNickname(commentId, nickname);
        Optional<PibuStory.board.Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isEmpty()) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }

        Comment comment = optionalComment.get();

        if (existingCommentGreat.isPresent()) {
            commentGreatRepository.deleteByCommentIdAndNickname(commentId, nickname);
            comment.setGreat(comment.getGreat() - 1);
        } else {
            PibuStory.board.CommentGreat newCommentGreat = new PibuStory.board.CommentGreat();
            newCommentGreat.setCommentId(commentId);
            newCommentGreat.setNickname(nickname);
            commentGreatRepository.save(newCommentGreat);
            comment.setGreat(comment.getGreat() + 1);
        }

        commentRepository.save(comment);

        return comment.getGreat();
    }

    // 특정 닉네임으로 사용자가 좋아요 누른 댓글 ID 목록 가져오기
    public List<String> getLikedCommentsByNickname(String nickname) {
        return commentGreatRepository.findByNickname(nickname)
                .stream()
                .map(CommentGreat::getCommentId)
                .collect(Collectors.toList());
    }
}
