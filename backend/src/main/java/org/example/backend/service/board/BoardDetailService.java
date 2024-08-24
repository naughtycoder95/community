package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.common.BoardIdMemberIdPk;
import org.example.backend.model.dto.NotifyDto;
import org.example.backend.model.dto.board.DelBoardDto;
import org.example.backend.model.dto.board.IBoardDetailDto;
import org.example.backend.model.dto.board.IBoardDto;
import org.example.backend.model.dto.board.IUserDto;
import org.example.backend.model.entity.board.*;
import org.example.backend.repository.board.*;
import org.example.backend.service.auth.NotifyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * packageName : org.example.backend.service.board
 * fileName : BoardDetailService
 * author : SAMSUNG
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         SAMSUNG          최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardDetailService {
    private final NotifyService notifyService;
    private final BoardDetailRepository boardDetailRepository;
    private final RecommendRepository recommendRepository;
    private final ReportRepository reportRepository;
    private final FileRepository fileRepository;
    private final BoardFileRepository boardFileRepository;
    private final PlaceRepository placeRepository;
    private final VoteMemberRepository voteMemberRepository;
    private final VoteRepository voteRepository;
    private final ReplyService replyService;
    ModelMapper modelMapper = new ModelMapper();

    // 로그인된 회원 정보 조회
    public Optional<IUserDto> findMember(String memberId) {
        Optional<IUserDto> optional = boardDetailRepository.findMember(memberId);
        return optional;
    }

    // 게시글, 작성자 정보 조회
    public Optional<IBoardDto> findBoardAndMember(Long boardId) {
        Optional<IBoardDto> optional = boardDetailRepository.findBoardAndMember(boardId);
        return optional;
    }

    // 코드번호로 코드명 조회
    public Optional<IBoardDetailDto> findCmCdName(String cmCd) {
        Optional<IBoardDetailDto> optional = boardDetailRepository.findCmCdName(cmCd);
        return optional;
    }

    // 글번호로 투표 조회
    public List<Vote> findVote(Long boardId) {
        return voteRepository.findByBoardId(boardId);
    }

    // 투표 회원 조회
    public Optional<VoteMember> findVoteMember(Long boardId, String memberId) {
        BoardIdMemberIdPk id = new BoardIdMemberIdPk(boardId, memberId);
        return voteMemberRepository.findById(id);
    }

    // 글번호로 장소 조회 (게시글 하나당 장소 하나)
    public Optional<Place> findPlace(Long boardId) {
        Optional<Place> optional = boardDetailRepository.findPlace(boardId);

        return optional;
    }

    // 글번호로 이미지 조회
    public List<IBoardDetailDto> findBoardImg(Long boardId) {
        List<IBoardDetailDto> list = boardDetailRepository.findBoardImg(boardId);
        return list;
    }

    // 첨부파일 다운로드
    public Optional<File> fileDownload(String uuid) {
        return fileRepository.findById(uuid);
    }

    // 추천 데이터 존재하는지 확인
    public Integer existsRecommend(Long boardId, String memberId) {
        Integer recommend = boardDetailRepository.existsRecommend(boardId, memberId);
        return recommend;
    }

    // 추천 저장
    public Recommend saveRecommend(Recommend recommend, String currentUrl) {
        Recommend recommend2 = recommendRepository.save(recommend);

        // 베스트 알림
        Long boardId = recommend2.getBoardId();
        Board board = boardDetailRepository.findById(boardId).orElse(null);
        int count = countRecommend(boardId);
        if (board.getBocode().equals("BO03") && count >= 10) {
            NotifyDto notifyDto = new NotifyDto();
            notifyDto.setNotiUrl(currentUrl);
            notifyService.createBestNotify(boardId, notifyDto);
        }

        return recommend2;
    }

    // 추천 수 카운트
    public Integer countRecommend(Long boardId) {
        Integer count = boardDetailRepository.countRecommend(boardId);
        return count;
    }

    // 글 신고 데이터 저장
    public Report saveReport(Report report) {
        Report report2 = reportRepository.save(report);
        return report2;
    }

    // 신고 데이터 존재하는지 확인
    public Integer existsReport(Long boardId, String memberId) {
        Integer report = boardDetailRepository.existsReport(boardId, memberId);
        return report;
    }

    // 댓글 신고 데이터 존재하는지 확인
    public Integer existsReplyReport(Long replyId, String memberId) {
        Integer replyReport = boardDetailRepository.existsReplyReport(replyId, memberId);
        return replyReport;
    }

    // 투표 저장
    @Transactional
    public void saveVoteAndCount(VoteMember voteMember) {
        Vote vote = voteRepository.findById(voteMember.getVoteId())
                .orElseThrow(() -> new RuntimeException("투표를 찾을 수 없습니다."));

        // Vote 투표수 + 1
        int count = vote.getVoteCnt() + 1;
        vote.setVoteCnt(count);

        voteRepository.save(vote);

        // VoteMember에 저장
        VoteMember voteMember1 = new VoteMember();
//        VoteMember voteMember1 = new VoteMember(MemberId, BoardId, voteId);

        voteMember1.setVoteId(voteMember.getVoteId());
        voteMember1.setBoardId(voteMember.getBoardId());
        voteMember1.setMemberId(voteMember.getMemberId());
        voteMemberRepository.save(voteMember1);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        List<DelBoardDto> delData = boardDetailRepository.findByBoardId(boardId);

        // 게시글 파일 삭제
        for (DelBoardDto item : delData) {
            String uuid = item.getUuid();
            log.debug("게시글 파일 삭제:::" + uuid);
            if (uuid != null) {
                boardFileRepository.deleteByUuid(uuid);
            }
        }

        // 파일 삭제
        for (DelBoardDto item : delData) {
            String uuid = item.getUuid();
            if (uuid != null) {
                boardFileRepository.deleteByUuid(uuid);
            }
        }


        // 댓글 삭제
        for (DelBoardDto item : delData) {
            Long replyId = item.getReplyId();
            log.debug("댓글 삭제:::" + replyId);
            if (replyId != null) {
                replyService.removeReply(replyId);
            }
        }

        // 장소 삭제
        placeRepository.deleteByBoardId(boardId);
        // 투표멤버 삭제
        voteMemberRepository.deleteByBoardId(boardId);
        // 투표 삭제
        voteRepository.deleteByBoardId(boardId);
        // 추천 삭제
        recommendRepository.deleteByBoardId(boardId);
        // 게시물 삭제
        boardDetailRepository.deleteById(boardId);
    }

    public void updateBoard(Long boardId, IBoardDto boardDto) {
        Board board2 = boardDetailRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        board2.setBoardContent(boardDto.getBoardContent());
        board2.setBoardTitle(boardDto.getBoardTitle());
        boardDetailRepository.save(board2);
    }
}
