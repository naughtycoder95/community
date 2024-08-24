package org.example.backend.repository.board;

import org.example.backend.model.entity.board.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    void deleteByBoardId(Long boardId);
}
