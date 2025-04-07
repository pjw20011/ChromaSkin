//package PibuStory.board;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//@Service
//public class BoardService {
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    public Page<Board> findBySubjectContaining(String subject, Pageable pageable) {
//        return boardRepository.findBySubjectContaining(subject, pageable);
//    }
//    // 조회수
//    public void incrementViewCount(String id) {
//        updateFieldById(id, board -> board.setView(board.getView() + 1));
//    }
//    // 게시글 저장
//    public void saveBoard(Board board) {
//        boardRepository.save(board);
//    }
//    // 게시글 수정
//    public void updateBoard(String boardId, String subject, String content) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));
//
//        board.setSubject(subject);
//        board.setContent(content);
//        boardRepository.save(board); // 수정된 내용 저장
//    }
//
//    // 게시글 삭제 및 댓글, 대댓글 삭제
//    public void deleteBoardAndComments(String boardId) {
//        commentRepository.deleteByBoardId(boardId); // 댓글과 대댓글 삭제
//        boardRepository.deleteById(boardId); // 게시글 삭제
//    }
//
//    // 댓글 수 증가/감소 통합
//    public void updateCommentCount(String boardId, int delta) {
//        updateFieldById(boardId, board -> board.setCommentCount(board.getCommentCount() + delta));
//    }
//
//    public Board getBoardById(String id) {
//        return boardRepository.findById(id).orElse(null);
//    }
//
//    // 공통 로직: 특정 필드를 수정하는 메서드
//    private void updateFieldById(String boardId, java.util.function.Consumer<Board> updater) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//        updater.accept(board);
//        boardRepository.save(board);
//    }
//}
package PibuStory.board;

import PibuStory.board.Board;
import PibuStory.board.BoardGreatService;
import PibuStory.board.BoardRepository;
import PibuStory.board.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    // 마이페이지 내가 좋아요 누른 글 기능
    @Autowired
    private BoardGreatService boardGreatService;

    public Page<PibuStory.board.Board> findBySubjectContaining(String subject, Pageable pageable) {
        return boardRepository.findBySubjectContaining(subject, pageable);
    }

    // 작성자로 게시글 가져오기 (내가 작성한 글 기능)
    public List<PibuStory.board.Board> getBoardsByWriter(String writer) {
        return boardRepository.findByWriter(writer);
    }

    // 조회수 증가
    public void incrementViewCount(String id) {
        updateFieldById(id, board -> board.setView(board.getView() + 1));
    }

    // 게시글 저장
    public void saveBoard(PibuStory.board.Board board) {
        boardRepository.save(board);
    }

    // 게시글 수정
    public void updateBoard(String boardId, String subject, String content) {
        PibuStory.board.Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + boardId));

        board.setSubject(subject);
        board.setContent(content);
        boardRepository.save(board); // 수정된 내용 저장
    }

    // 게시글 삭제 및 댓글, 대댓글 삭제
    public void deleteBoardAndComments(String boardId) {
        commentRepository.deleteByBoardId(boardId); // 댓글과 대댓글 삭제
        boardRepository.deleteById(boardId); // 게시글 삭제
    }

    // 댓글 수 증가
    public void incrementCommentCount(String boardId) {
        updateCommentCount(boardId, 1);
    }

    // 댓글 수 감소
    public void decrementCommentCount(String boardId) {
        updateCommentCount(boardId, -1);
    }

    // 댓글 수 증가/감소 통합
    public void updateCommentCount(String boardId, int delta) {
        updateFieldById(boardId, board -> {
            int newCount = board.getCommentCount() + delta;
            board.setCommentCount(Math.max(newCount, 0)); // 음수 방지
        });
    }

    public PibuStory.board.Board getBoardById(String id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 공통 로직: 특정 필드를 수정하는 메서드
    private void updateFieldById(String boardId, java.util.function.Consumer<PibuStory.board.Board> updater) {
        PibuStory.board.Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        updater.accept(board);
        boardRepository.save(board);
    }

    // 마이페이지 내가 좋아요 누른 글 기능
    public List<Board> getLikedBoards(String nickname) {
        // 좋아요 누른 게시글 ID 조회
        List<String> likedBoardIds = boardGreatService.getLikedBoardsByNickname(nickname);

        // ID를 기준으로 게시글 정보 조회
        return boardRepository.findByIdIn(likedBoardIds);
    }
}
