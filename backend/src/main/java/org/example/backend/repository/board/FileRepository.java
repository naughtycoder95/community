package org.example.backend.repository.board;

import jakarta.transaction.Transactional;
import org.example.backend.model.entity.board.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
    // 파일 데이터 삭제 (hard delete)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TB_FILE WHERE UUID = :uuid"
            , nativeQuery = true)
    void deleteById2 (@Param("uuid") String uuid);
}
