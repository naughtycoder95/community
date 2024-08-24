package org.example.backend.service.board;

import org.example.backend.model.dto.MainPageDto;
import lombok.RequiredArgsConstructor;
import org.example.backend.repository.board.HotTopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName : org.example.backend.service.board
 * fileName : HotTopicService
 * author : GGG
 * date : 2024-06-03
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-03         GGG          최초 생성
 */
@Service
@RequiredArgsConstructor
public class HotTopicService {
    private final HotTopicRepository hotTopicRepository;

    public List<MainPageDto> getHotTopics() {
        return hotTopicRepository.findHotTopics();
    }
}
