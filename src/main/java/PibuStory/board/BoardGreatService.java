package PibuStory.board;

import PibuStory.board.Board;
import PibuStory.board.BoardGreat;
import PibuStory.board.BoardGreatRepository;
import PibuStory.board.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardGreatService {


    private final BoardGreatRepository boardGreatRepository;
    private final PibuStory.board.BoardRepository boardRepository;

    public BoardGreatService(BoardGreatRepository boardGreatRepository, BoardRepository boardRepository) {
        this.boardGreatRepository = boardGreatRepository;
        this.boardRepository = boardRepository;
    }

    public int toggleBoardGreat(String boardId, String nickname) {
        Optional<PibuStory.board.BoardGreat> existingBoardGreat = boardGreatRepository.findByBoardIdAndNickname(boardId, nickname);
        Optional<PibuStory.board.Board> optionalBoard = boardRepository.findById(boardId);

        if (optionalBoard.isEmpty()) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }

        Board board = optionalBoard.get();

        if (existingBoardGreat.isPresent()) {
            // 이미 좋아요가 눌린 경우 좋아요 취소
            boardGreatRepository.deleteByBoardIdAndNickname(boardId, nickname);
            board.setGreat(board.getGreat() - 1);
        } else {
            // 좋아요 추가
            PibuStory.board.BoardGreat newBoardGreat = new PibuStory.board.BoardGreat();
            newBoardGreat.setBoardId(boardId);
            newBoardGreat.setNickname(nickname);
            boardGreatRepository.save(newBoardGreat);
            board.setGreat(board.getGreat() + 1);
        }

        boardRepository.save(board);
        return board.getGreat();
    }

    // 특정 닉네임으로 사용자가 좋아요 누른 게시글 ID 목록 가져오기
    public List<String> getLikedBoardsByNickname(String nickname) {
        return boardGreatRepository.findByNickname(nickname)
                .stream()
                .map(BoardGreat::getBoardId)
                .collect(Collectors.toList());
    }

}
