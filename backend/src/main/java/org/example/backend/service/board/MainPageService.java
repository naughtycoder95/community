package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.MainPageDto;
import org.example.backend.repository.board.MainPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName : org.example.backend.service.board
 * fileName : MainPageService
 * author : GGG
 * date : 2024-05-22
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 *
 * -----------------------------------------------------------
 * 2024-05-22         GGG          최초 생성
 */

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MainPageRepository mainPageRepository;

//    자유게시판
    public List<MainPageDto> getFreeBoardData() {
        return mainPageRepository.findFreeBoardData();
    }

//    건의게시판
    public List<MainPageDto> getSuggestionBoardData() {
        return mainPageRepository.findSuggestionBoardData();
    }

//    칭찬게시판
    public List<MainPageDto> getPraiseBoardData() {
        return mainPageRepository.findPraiseBoardData();
    }


    //    핫토픽 조회함수
    public List<MainPageDto> getHotTopics() {
        return mainPageRepository.findHotTopics();
    }
}
