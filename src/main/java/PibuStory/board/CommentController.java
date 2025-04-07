package PibuStory.board;

import PibuStory.board.BoardService;
import PibuStory.board.Comment;
import PibuStory.board.CommentGreatService;
import PibuStory.board.CommentService;
import PibuStory.board.Reply;
import PibuStory.board.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentGreatService commentGreatService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private BoardService boardService;


    // 댓글 작성
    @PostMapping("/comment")
    public ResponseEntity<String> createComment(
            @RequestParam("boardId") String boardId,
            @RequestParam("content") String content,
            @RequestParam("author") String author) {

        System.out.println("createComment 호출 - boardId: " + boardId + ", content: " + content + ", author: " + author);
        String date = new SimpleDateFormat("MM-dd HH:mm").format(new Date());

        try {
            PibuStory.board.Comment comment = new PibuStory.board.Comment();
            comment.setBoardId(boardId);
            comment.setCreatedDate(date);
            comment.setContent(content);
            comment.setAuthor(author);

            commentService.saveComment(comment);
            boardService.updateCommentCount(boardId, 1);  // 댓글 수 증가

            System.out.println("댓글 저장 완료");
            return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성에 실패했습니다.");
        }
    }

    // 댓글 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("comment_id") String commentId) {
        try {
            // 댓글 조회
            PibuStory.board.Comment comment = commentService.getCommentById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

            // 대댓글 삭제 및 개수 반환
            int replyCount = replyService.deleteRepliesAndCountByCommentId(commentId);

            // 댓글 삭제
            commentService.deleteCommentById(commentId);

            // Board의 commentCount 감소 (댓글 1개 + 대댓글 개수)
            int totalDecreaseCount = 1 + replyCount;
            boardService.updateCommentCount(comment.getBoardId(), -totalDecreaseCount);
            System.out.println("commentId : " + commentId + " 삭제 완료");
            System.out.println("삭제된 댓글 갯수 : " + totalDecreaseCount);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제에 실패했습니다.");
        }
    }

    // 특정 댓글에 대한 대댓글 조회
    @GetMapping("/replies/{commentId}")
    public List<PibuStory.board.Reply> getReplies(@PathVariable String commentId) {
        return replyService.getRepliesByCommentId(commentId);
    }


    // 댓글
    @GetMapping("/view/{boardId}")
    public String viewBoard(@PathVariable String boardId, Model model) {
        List<PibuStory.board.Comment> comments = commentService.getCommentsByBoardId(boardId);
        Map<String, List<PibuStory.board.Reply>> repliesMap = new HashMap<>();

        for (PibuStory.board.Comment comment : comments) {
            List<Reply> replies = replyService.getRepliesByCommentId(comment.getId());
            repliesMap.put(comment.getId(), replies);
        }

        model.addAttribute("comments", comments);
        model.addAttribute("repliesMap", repliesMap);
        return "board-detail";
    }


    @PostMapping("/toggle-comment-great")
    public ResponseEntity<Map<String, Object>> toggleCommentGreat(
            @RequestParam("commentId") String commentId,
            @SessionAttribute("nickname") String nickname) {

        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("댓글 좋아요 호출 - commentId " + commentId + ", user : " + nickname); // commentId 확인

            Optional<PibuStory.board.Comment> optionalComment = commentService.getCommentById(commentId);
            if (optionalComment.isEmpty()) {
                System.out.println("댓글을 찾을 수 없음: " + commentId); // 로그 추가
                throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
            }

            Comment comment = optionalComment.get();

            // 좋아요 상태 변경 및 업데이트된 좋아요 개수 가져오기
            int updatedGreatCount = commentGreatService.toggleCommentGreat(commentId, nickname);

            // 좋아요가 추가되었는지 여부 결정
            boolean liked = updatedGreatCount > comment.getGreat();

            response.put("liked", liked);
            response.put("great", updatedGreatCount);

            System.out.println("댓글 좋아요 성공");

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);

        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
