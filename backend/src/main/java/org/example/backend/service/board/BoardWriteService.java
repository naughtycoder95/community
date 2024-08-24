package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.BoardDto;
import org.example.backend.model.dto.board.BoardWriteDto;
import org.example.backend.model.entity.board.Board;
import org.example.backend.repository.board.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : org.example.backend.service.board
 * fileName : BoardWriteService
 * author : gumiji
 * date : 5/29/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/29/24         gumiji          최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardWriteService {
    private final BoardRepository boardRepository;
    private final VoteService voteService;
    private final FileService fileService;
    private final PlaceService placeService;

    //    글쓰기 등록 기능
    @Transactional
    public void save(BoardWriteDto boardWriteDto) {
        // JPA 저장 함수 실행 : return 값 : 저장된 객체

        BoardDto board2 = boardWriteDto.getBoardDto();
        Board board = new Board();
        log.debug("save : " + board2);
        log.debug("save 2 : " + board);

        board.setBoardContent(board2.getBoardContent());
        board.setBoardTitle(board2.getBoardTitle());
        board.setBocode(board2.getBocode());
        board.setSmcode(board2.getSmcode());
        board.setNoticeYn(board2.getNoticeYn());
        board.setMemberId(board2.getMemberId());
        boardRepository.save(board);

//        파일/게시판 파일관리 테이블 저장
//        null 체크
        if (boardWriteDto.getFileDtos() != null) {
            fileService.saveFiles(boardWriteDto.getFileDtos(), board.getBoardId());
        }
//        투표 저장
        if (boardWriteDto.getVoteDtos() != null) {
            voteService.saveVote(board.getBoardId(), boardWriteDto.getVoteDtos());
        }
//        장소 저장
        if (boardWriteDto.getPlaceDto() != null) {
            placeService.savePlace(board.getBoardId(), boardWriteDto.getPlaceDto());
        }
    }
}
