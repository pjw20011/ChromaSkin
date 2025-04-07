package PibuStory.board;

import PibuStory.board.Reply;
import PibuStory.board.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    public PibuStory.board.Reply saveReply(PibuStory.board.Reply reply) {
        return replyRepository.save(reply);
    }

    // 특정 댓글의 대댓글과 관련된 모든 작업을 통합
    public int deleteRepliesAndCountByCommentId(String commentId) {
        List<PibuStory.board.Reply> replies = replyRepository.findByCommentId(commentId);
        if (replies.isEmpty()) {
            System.out.println("삭제할 대댓글이 없습니다. commentId: " + commentId);
            return 0;
        }
        System.out.println("삭제할 대댓글 목록: " + replies); // 로그로 확인
        int replyCount = replies.size();
        replyRepository.deleteAll(replies);
        return replyCount;
    }

    // 특정 대댓글 삭제
    public void deleteReplyById(String replyId) {
        PibuStory.board.Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다. replyId: " + replyId));
        replyRepository.delete(reply);
        System.out.println("삭제된 대댓글: " + reply);
    }

    // 특정 댓글의 대댓글 조회 (필요한 경우 그대로 유지)
    public List<PibuStory.board.Reply> getRepliesByCommentId(String commentId) {
        return replyRepository.findByCommentId(commentId);
    }

    // 특정 댓글의 대댓글 좋아요 조회
    public Optional<PibuStory.board.Reply> getReplyById(String replyId) {
        return replyRepository.findById(replyId);
    }

    // 좋아요 기능
    public boolean toggleLike(String replyId, String nickname) {
        PibuStory.board.Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다. replyId: " + replyId));

        Set<String> likedUsers = reply.getLikedUsers();
        boolean liked;

        if (likedUsers.contains(nickname)) {
            likedUsers.remove(nickname);
            reply.setGreat(reply.getGreat() - 1); // 좋아요 개수 감소
            liked = false;
        } else {
            likedUsers.add(nickname);
            reply.setGreat(reply.getGreat() + 1); // 좋아요 개수 증가
            liked = true;
        }

        // JPA 변경 감지로 인해 save 호출 없이 자동으로 반영
        return liked;
    }

    // 내가 대댓글 단 글 조회 기능
    public List<Reply> getRepliesByAuthor(String author) {
        return replyRepository.findByAuthor(author);
    }

}
