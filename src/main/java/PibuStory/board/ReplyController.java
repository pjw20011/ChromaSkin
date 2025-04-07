package PibuStory.board;

import PibuStory.board.BoardService;
import PibuStory.board.Comment;
import PibuStory.board.CommentService;
import PibuStory.board.Reply;
import PibuStory.board.ReplyGreatService;
import PibuStory.board.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ReplyGreatService replyGreatService;

    // 대댓글 작성
    @PostMapping("/replies")
    public ResponseEntity<String> createReplies(
            @RequestParam("boardId") String boardId,
            @RequestParam("commentId") String commentId,
            @RequestParam("content") String content,
            @RequestParam("author") String author) {

        System.out.println("createReplies 호출 - boardId: " + boardId + " commentId: " + commentId + ", content: " + content + ", author: " + author);
        String date = new SimpleDateFormat("MM-dd HH:mm").format(new Date());

        try {
            PibuStory.board.Reply reply = new PibuStory.board.Reply();
            reply.setBoardId(boardId);
            reply.setCommentId(commentId);
            reply.setContent(content);
            reply.setAuthor(author);
            reply.setCreatedDate(date);

            replyService.saveReply(reply);
            boardService.updateCommentCount(boardId, 1);  // 대댓글 작성 시 댓글 수 증가

            System.out.println("대댓글 저장 완료");
            return ResponseEntity.ok("대댓글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대댓글 작성에 실패했습니다.");
        }
    }

    // 대댓글 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteReply(@RequestParam("replyId") String replyId) {
        try {
            System.out.println("deleteReply 호출 - replyId: " + replyId);

            // 대댓글 조회
            PibuStory.board.Reply reply = replyService.getReplyById(replyId)
                    .orElseThrow(() -> new IllegalArgumentException("대댓글을 찾을 수 없습니다. replyId: " + replyId));

            // 대댓글이 속한 댓글 ID 가져오기
            String commentId = reply.getCommentId();

            // 댓글이 속한 게시글 ID 가져오기
            Comment comment = commentService.getCommentById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. commentId: " + commentId));
            String boardId = comment.getBoardId();

            // 대댓글 삭제
            replyService.deleteReplyById(replyId);

            // 게시글의 댓글 수 감소
            boardService.decrementCommentCount(boardId);

            System.out.println("대댓글 삭제 및 게시글 댓글 수 감소 완료. boardId: " + boardId);

            return ResponseEntity.ok("대댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대댓글 삭제에 실패했습니다.");
        }
    }


    @PostMapping("/toggle-reply-great")
    public ResponseEntity<Map<String, Object>> toggleReplyGreat(
            @RequestParam("replyId") String replyId,
            @SessionAttribute("nickname") String nickname) {

        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Received replyId: " + replyId); // replyId 확인

            Optional<PibuStory.board.Reply> optionalReply = replyService.getReplyById(replyId);
            if (optionalReply.isEmpty()) {
                System.out.println("댓글을 찾을 수 없음: " + replyId); // 로그 추가
                throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
            }

            Reply reply = optionalReply.get();

            // 좋아요 상태 변경 및 업데이트된 좋아요 개수 가져오기
            int updatedGreatCount = replyGreatService.toggleReplyGreat(replyId, nickname);

            // 좋아요가 추가되었는지 여부 결정
            boolean liked = updatedGreatCount > reply.getGreat();

            response.put("liked", liked);
            response.put("great", updatedGreatCount);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);

        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
