package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.BoardDto;
import org.example.backend.model.dto.board.BoardWriteDto;
import org.example.backend.model.dto.board.FileDto;
import org.example.backend.model.entity.board.Board;
import org.example.backend.model.entity.board.BoardFile;
import org.example.backend.repository.board.BoardFileRepository;
import org.example.backend.repository.board.BoardRepository;
import org.example.backend.repository.board.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class BoardEditService {
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final PlaceService placeService;
    private final BoardFileRepository boardFileRepository;
    private final FileRepository fileRepository;

    @Transactional
    public void update(BoardWriteDto boardWriteDto) {
        // BoardWriteDto에서 boardId를 가져옵니다.
        Long boardId = boardWriteDto.getBoardDto().getBoardId();

        // 수정할 기존 게시글 객체를 찾습니다.
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 기존 게시글 객체의 필드를 업데이트합니다.
        BoardDto boardDto = boardWriteDto.getBoardDto();
        board.setBoardContent(boardDto.getBoardContent());
        board.setBoardTitle(boardDto.getBoardTitle());
        board.setBocode(boardDto.getBocode());
        board.setSmcode(boardDto.getSmcode());
        board.setNoticeYn(boardDto.getNoticeYn());
        board.setMemberId(boardDto.getMemberId());

        // 저장
        boardRepository.save(board);

        // 파일, 장소 등의 저장 로직 추가
        List<FileDto> fileDtos = boardWriteDto.getFileDtos();
        log.info("@@File DTO = {}", fileDtos);
        if (fileDtos != null) {
            fileService.saveFiles(fileDtos, board.getBoardId());
        } else {
            List<BoardFile> boardFiles = boardFileRepository.findByBoardId(boardId);
            for (BoardFile boardFile : boardFiles) {
                boardFileRepository.deleteByUuid(boardFile.getUuid());
                fileRepository.deleteById(boardFile.getUuid());
            }
        }
        if (boardWriteDto.getPlaceDto() != null) {
            placeService.savePlace(board.getBoardId(), boardWriteDto.getPlaceDto());
        }
    }

//    public List<Vote> findAll(Long boardId) {
//        return voteRepository.findByBoardId(boardId);
//    }
}