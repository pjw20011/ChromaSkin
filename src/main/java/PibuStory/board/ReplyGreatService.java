package PibuStory.board;


import PibuStory.board.Reply;
import PibuStory.board.ReplyGreat;
import PibuStory.board.ReplyGreatRepository;
import PibuStory.board.ReplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReplyGreatService {

    private final ReplyGreatRepository replyGreatRepository;
    private final PibuStory.board.ReplyRepository replyRepository;

    public ReplyGreatService(ReplyGreatRepository replyGreatRepository, ReplyRepository replyRepository) {
        this.replyGreatRepository = replyGreatRepository;
        this.replyRepository = replyRepository;
    }

    public int toggleReplyGreat(String replyId, String nickname) {
        Optional<PibuStory.board.ReplyGreat> existingReplyGreat = replyGreatRepository.findByReplyIdAndNickname(replyId, nickname);
        Optional<PibuStory.board.Reply> optionalReply = replyRepository.findById(replyId);

        if (optionalReply.isEmpty()) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }

        Reply reply = optionalReply.get();

        if (existingReplyGreat.isPresent()) {
            replyGreatRepository.deleteByReplyIdAndNickname(replyId, nickname);
            reply.setGreat(reply.getGreat() - 1);
        } else {
            PibuStory.board.ReplyGreat newReplyGreat = new PibuStory.board.ReplyGreat();
            newReplyGreat.setReplyId(replyId);
            newReplyGreat.setNickname(nickname);
            replyGreatRepository.save(newReplyGreat);
            reply.setGreat(reply.getGreat() + 1);
        }

        replyRepository.save(reply);

        return reply.getGreat();
    }

    // 특정 닉네임으로 사용자가 좋아요 누른 대댓글 ID 목록 가져오기
    public List<String> getLikedRepliesByNickname(String nickname) {
        return replyGreatRepository.findByNickname(nickname)
                .stream()
                .map(ReplyGreat::getReplyId)
                .collect(Collectors.toList());
    }
}
