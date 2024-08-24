package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.MainPageSearchDto;
import org.example.backend.repository.board.MainSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * packageName : org.example.backend.service.board
 * fileName : MainSearchService
 * author : GGG
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         GGG          최초 생성
 */
@Service
@RequiredArgsConstructor
public class MainSearchService {

    private final MainSearchRepository mainSearchRepository;

    //    검색기능
    public Page<MainPageSearchDto> searchBoard(String boardtitle, Pageable pageable) {
        return mainSearchRepository.searchAllByBoard(boardtitle, pageable);
    }
}
