package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.board.VoteDto;
import org.example.backend.model.entity.board.Vote;
import org.example.backend.repository.board.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * packageName : org.example.backend.service.board
 * fileName : VoteService
 * author : kimtaewan
 * date : 2024-05-28
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-28         kimtaewan          최초 생성
 */
@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final ModelMapper modelMapper;

    public void updateVoteCnt(Long voteId, String memberId, Long boardId) {
        voteRepository.updateVoteCnt(voteId, memberId, boardId);
    }

    public void saveVote(Long boardId, List<VoteDto> voteDtos) {
        // 리스트에 저장된 voteDto를 반복해서 저장
        for (VoteDto voteDto : voteDtos) {
            // Dto를 Vote 엔티티로 변환
            Vote vote = modelMapper.map(voteDto, Vote.class);
            vote.setBoardId(boardId);

            voteRepository.save(vote);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void voteScheduler() {
        String delDate = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd"));
        voteRepository.updateStatus(delDate);
    }
}
