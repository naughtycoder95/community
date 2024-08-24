package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.board.BoardFileDto;
import org.example.backend.model.entity.board.BoardFile;
import org.example.backend.repository.board.BoardFileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardFileService {

    private final BoardFileRepository boardFileRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void saveBoardFile(Long boardId, List<BoardFileDto> boardFileDtos) {
        // 리스트에 저장된 fileDto를 반복해서 저장
        for (BoardFileDto boardFileDto : boardFileDtos) {
            // 받아온 boardId Dto에 세팅
            boardFileDto.setBoardId(boardId);
            // Dto를 BoardFile 엔티티로 변환
            BoardFile boardFile = modelMapper.map(boardFileDto, BoardFile.class);
            boardFileRepository.save(boardFile);
        }
    }
}
