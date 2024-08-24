package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.board.BoardFileDto;
import org.example.backend.model.dto.board.FileDto;
import org.example.backend.model.dto.board.ISuggestBoardDto;
import org.example.backend.model.dto.board.VoteDto;
import org.example.backend.model.entity.board.Board;
import org.example.backend.model.entity.board.Place;
import org.example.backend.repository.board.SuggestBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * packageName : org.example.backend.service.board
 * fileName : SuggestBoardService
 * author : GGG
 * date : 2024-06-05
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-05         GGG          최초 생성
 */
@Service
@RequiredArgsConstructor
public class SuggestBoardService {
    private final SuggestBoardRepository suggestBoardRepository;
    private final VoteService voteService;
    private final BoardFileService boardFileService;

    //    TODO: 최신글 전체조회(read)
    public Page<ISuggestBoardDto> findAllByBoardTitleContaining(String boardTitle, Pageable pageable) {
        return suggestBoardRepository.findAllBySgBoardTitleContaining(boardTitle, pageable);
    }

    //    자유게시판 공지 조회
    public List<ISuggestBoardDto> findByCodeAndNotice() {
        List<ISuggestBoardDto> list = suggestBoardRepository.findBySuggestNotice();
        return list;
    }

    //    페이징 처리
    public Page<ISuggestBoardDto> findAllByFreeBoardTitleContaining(String boardTitle,
                                                                 Pageable pageable) {
        Page<ISuggestBoardDto> page
                = suggestBoardRepository
                .findAllBySgBoardTitleContaining(boardTitle, pageable);
        return page;
    }

    //    상세조회
    public Optional<Board> findById(Long boardId) {
        //    JPA 상세조회 함수 실행
        Optional<Board> optionalSuggestBoard
                = suggestBoardRepository.findById(boardId);
        return optionalSuggestBoard;
    }

    @Transactional(rollbackFor = Exception.class)
    //    TODO: 등록(insert),수정(update)
    public void save(Board board, List<VoteDto> voteDtos, Place place, List<FileDto> fileDtos, List<BoardFileDto> boardFileDtos) {
        // 분류코드를 설정
        board.setBocode("BO04");

        // JPA 저장 함수 실행 : return 값 : 저장된 객체
        Board board2 = suggestBoardRepository.save(board);

        // 저장된 board의 boardId를 객체로 변환
        Long boardId = board2.getBoardId();
        voteService.saveVote(boardId, voteDtos);

//        placeService.savePlace(boardId, place);

//        fileService.saveFiles(fileDtos);

        boardFileService.saveBoardFile(boardId, boardFileDtos);

        //        null 체크
        Optional.ofNullable(voteDtos).ifPresent(voteDtos2 -> voteService.saveVote(boardId, voteDtos2));
//        Optional.ofNullable(place).ifPresent(place2 -> placeService.savePlace(boardId, place2));
//        Optional.ofNullable(fileDtos).ifPresent(filedtos2 -> fileService.saveFiles(filedtos2));
        Optional.ofNullable(boardFileDtos).ifPresent(boardFileDtos2 -> boardFileService.saveBoardFile(boardId, boardFileDtos2));
    }

    public void update(Board board) {
        suggestBoardRepository.save(board);
    }

    // TODO: 삭제(delete)
    public boolean removeById(Long boardId) {
        // 해당 boardId에 해당하는 게시글 조회
        Optional<Board> optionalBoard = suggestBoardRepository.findById(boardId);

        // 게시글이 존재하는 경우
        if (optionalBoard.isPresent()) {
            // 조회된 게시글의 분류코드가 BO04인지 확인
            Board board = optionalBoard.get();
            if ("BO04".equals(board.getBocode())) {
                // BO04 분류코드에 해당하는 게시글이면 삭제
                suggestBoardRepository.deleteById(boardId);
                return true;
            } else {
                // BO03 분류코드가 아닌 경우 삭제하지 않고 false 반환
                return false;
            }
        } else {
            // 해당 boardId에 해당하는 게시글이 없는 경우 false 반환
            return false;
        }
    }
}
