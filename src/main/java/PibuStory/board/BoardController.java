package PibuStory.board;

import PibuStory.board.*;
import PibuStory.board.Board;
import PibuStory.board.BoardService;
import PibuStory.board.Comment;
import PibuStory.board.CommentService;
import PibuStory.board.Reply;
import PibuStory.board.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/board")
public class BoardController {


    @GetMapping("/board-write")
    public String boardWrite() {
        return "board-write"; // board-write.html을 반환
    }


    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardGreatService boardGreatService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ReplyGreatService replyGreatService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentGreatService commentGreatService;


    @PostMapping("/process")
    public String createBoard(@RequestParam String subject, @RequestParam String content,
//                              @RequestParam(required = false) String[] images,
                              HttpSession session) {
        // 세션에서 username 속성 확인
        String writer = (String) session.getAttribute("nickname");

        if (writer == null) {
            return "redirect:/login"; // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉션
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        int view = 0;
        int great = 0;

        if (subject == null || subject.isEmpty() || content == null || content.isEmpty()) {
            return "제목과 내용이 필요합니다.";
        }

        PibuStory.board.Board board = new PibuStory.board.Board();
        board.setSubject(subject);
        board.setContent(content);
        board.setWriter(writer);
        board.setDate(date);
        board.setView(view);
        board.setGreat(great);

        boardService.saveBoard(board);
        return "redirect:/board"; // 저장 후 게시판 목록으로 리디렉션
    }

    // 게시글 삭제
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteBoard(
            @RequestParam("boardId") String boardId,
            @RequestParam("boardSubject") String boardSubject) {
        try {
            System.out.println("deleteBoard 호출 - boardId: " + boardId + ", boardSubject: " + boardSubject);
            boardService.deleteBoardAndComments(boardId); // 관련 댓글과 대댓글도 삭제

            System.out.println("boardId : " + boardId + " boardSubject : " + boardSubject + " 삭제 완료");
            System.out.println("게시글 삭제 완료");

            // 303 리디렉션으로 클라이언트를 /board로 이동
            return ResponseEntity.status(303).header("Location", "/board").build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 게시글 수정
    @GetMapping("/update")
    public String updateBoardForm(@RequestParam("id") String boardId, Model model) {
        PibuStory.board.Board board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
        return "board-write"; // 수정 페이지로 이동
    }

    // 수정된 게시글 저장
    @PostMapping("/update")
    public String updateBoard(
            @RequestParam("id") String boardId,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content) {
        boardService.updateBoard(boardId, subject, content);
        return "redirect:/board/board-detail/" + boardId; // 수정 완료 후 해당 게시글 보기 페이지로 이동
    }




    @GetMapping()
    public String boardList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<PibuStory.board.Board> page = boardService.findBySubjectContaining(search, pageable);

        model.addAttribute("board", page.getContent());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startPage", Math.max(1, pageNum - 2));
        model.addAttribute("endPage", Math.min(page.getTotalPages(), pageNum + 2));

        return "board"; // board.html을 반환
    }

    // 게시판 자세히 보기 (게시판, 댓글, 대댓글의 좋아요 및 조회수)
    @GetMapping("/board-detail/{id}")
    public String viewBoard(@PathVariable("id") String id,
                            @SessionAttribute("nickname") String nickname, // 세션에서 nickname을 가져옴
                            Model model) {
        PibuStory.board.Board board = boardService.getBoardById(id);
        if (board == null) {
            System.out.println("게시글을 찾을 수 없습니다: " + id);
            return "redirect:/board";
        }

        // 조회수 증가
        boardService.incrementViewCount(id);

        // 댓글 목록 조회
        List<PibuStory.board.Comment> comments = commentService.getCommentsByBoardId(id);

        // 대댓글을 commentId를 키로 하는 Map으로 변환
        Map<String, List<Reply>> repliesMap = comments.stream()
                .collect(Collectors.toMap(
                        Comment::getId,
                        comment -> replyService.getRepliesByCommentId(comment.getId())
                ));

        // 사용자가 좋아요 누른 대댓글 목록을 가져옴
        List<String> likedReplies = replyGreatService.getLikedRepliesByNickname(nickname);

        // 사용자가 좋아요 누른 댓글 목록을 가져옴
        List<String> likedComments = commentGreatService.getLikedCommentsByNickname(nickname);

        // 사용자가 좋아요 누른 게시글 목록을 가져옴
        List<String> likedBoards = boardGreatService.getLikedBoardsByNickname(nickname);

        // 모델에 필요한 데이터 추가
        model.addAttribute("board", board); // 업데이트된 조회수를 포함한 게시글 정보를 다시 가져오기
        model.addAttribute("comments", comments);
        model.addAttribute("repliesMap", repliesMap); // repliesMap 추가
        model.addAttribute("likedReplies", likedReplies); // 좋아요 누른 대댓글 목록 전달
        model.addAttribute("likedComments", likedComments); // 좋아요 누른 댓글 목록 전달
        model.addAttribute("likedBoards", likedBoards); // 좋아요 누른 게시글 목록 전달

        return "board-detail";
    }

    // 게시판 좋아요 기능 구현
    @PostMapping("/toggle-board-great")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleBoardGreat(@RequestParam String boardId,
                                                                @SessionAttribute("nickname") String nickname) {
        try {
            int updatedGreatCount = boardGreatService.toggleBoardGreat(boardId, nickname);
            boolean liked = boardGreatService.getLikedBoardsByNickname(nickname).contains(boardId);

            Map<String, Object> response = Map.of(
                    "liked", liked,
                    "great", updatedGreatCount
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "게시글을 찾을 수 없습니다."));
        }
    }

    // 마이페이지 내가 좋아요 누른 글 기능 구현
    @GetMapping("/liked")
    public ResponseEntity<List<PibuStory.board.Board>> getLikedBoards(HttpSession session) {
        // 세션에서 닉네임 가져오기
        String nickname = (String) session.getAttribute("nickname");

        if (nickname == null) {
            // 비로그인 상태일 경우 401 Unauthorized 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 닉네임을 기반으로 좋아요 누른 게시글 조회
        List<Board> likedBoards = boardService.getLikedBoards(nickname);

        return ResponseEntity.ok(likedBoards);
    }


}
