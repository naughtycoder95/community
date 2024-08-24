package org.example.backend.repository.board;

import org.example.backend.model.entity.board.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    void deleteByUuid(String uuid);

    List<BoardFile> findByBoardId(Long boardId);
}
