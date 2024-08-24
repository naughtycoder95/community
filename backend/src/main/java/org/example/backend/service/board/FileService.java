package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.FileDto;
import org.example.backend.model.entity.board.BoardFile;
import org.example.backend.model.entity.board.File;
import org.example.backend.repository.board.BoardFileRepository;
import org.example.backend.repository.board.FileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final BoardFileRepository boardFileRepository;
    private final ModelMapper modelMapper;

    //    1개의 파일 저장
    public File saveFile(File file) {
        File file2 = fileRepository.save(file);
        return file2;
    }

    //    게시판 상세조회 파일(이미지) 조회
    public File getFile(String uuid) {
        Optional<File> file = fileRepository.findById(uuid);
        if (file.isPresent()) {
            return file.get();
        }
        return null;
    }

    @Transactional
    public void saveFiles(List<FileDto> fileDtos, Long boardId) {
        try {
            List<BoardFile> boardFiles = boardFileRepository.findByBoardId(boardId);

            // fileDtos의 UUID 목록을 Set<String>으로 변환
            Set<String> fileDtoUuids = fileDtos.stream()
                    .map(FileDto::getUuid)
                    .collect(Collectors.toSet());

            // fileDtos에 존재하지 않는 boardFiles의 데이터 추출
            List<BoardFile> unmatchedFiles = boardFiles.stream()
                    .filter(file -> !fileDtoUuids.contains(file.getUuid()))
                    .collect(Collectors.toList());

            unmatchedFiles.forEach(unmatchedFile -> {
                boardFileRepository.deleteByUuid(unmatchedFile.getUuid());
                fileRepository.deleteById(unmatchedFile.getUuid());
            });

            // 리스트에 저장된 voteDto를 반복해서 저장
            for (FileDto fileDto : fileDtos) {
//                String fileName = fileDto.getFileName(); // 파일 이름 가져오기
//                String[] parts = fileName.split("\\_"); // "."을 기준으로 분리
//// 마지막 부분(확장자)을 가져오기
//                String fileExtension = "_" + parts[parts.length - 1];
                if (fileDto.getUuid() == null) {

//              1-1) uuid 생성 : 자바생성함수 이용
                    fileDto.setUuid(UUID.randomUUID().toString()
                            .replace("-", ""));
                    fileDto.setFileUrl(ServletUriComponentsBuilder
                            .fromCurrentContextPath()   // spring 기본주소 : http://localhost:8000
                            .path("/api/board/file/upload2/")   // 추가 경로 넣기
                            .path(fileDto.getUuid()) // uuid 넣기 : xxxxxx...
//                            .path(fileExtension)
                            .toUriString()); // 합치기 함수 : http://localhost:8000/board/file/upload/xxxxxx);
                    File file = modelMapper.map(fileDto, File.class);
                    fileRepository.save(file);
                    BoardFile boardFile = new BoardFile();
                    boardFile.setUuid(file.getUuid());
                    boardFile.setBoardId(boardId);
                    boardFileRepository.save(boardFile);
                } else {
                    fileDto.setUuid(UUID.randomUUID().toString()
                            .replace("-", ""));
                    fileDto.setFileUrl(ServletUriComponentsBuilder
                            .fromCurrentContextPath()   // spring 기본주소 : http://localhost:8000
                            .path("/api/board/file/upload2/")   // 추가 경로 넣기
                            .path(fileDto.getUuid()) // uuid 넣기 : xxxxxx...
//                            .path(fileExtension)
                            .toUriString()); // 합치기 함수 : http://localhost:8000/board/file/upload/xxxxxx);
                }
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
}
