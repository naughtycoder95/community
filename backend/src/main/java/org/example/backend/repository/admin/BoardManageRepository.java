package org.example.backend.repository.admin;

import org.example.backend.model.dto.BoardManageDto;
import org.example.backend.model.entity.CmCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName : org.example.backend.repository.board
 * fileName : BoardManagementRepositori
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
@Repository
public interface BoardManageRepository extends JpaRepository<CmCode , String> {
    @Query(value = "SELECT CM_CD AS cmCd, " +
            "UP_CM_CD AS upCmCd, " +
            "CM_CD_NAME AS cmCdName, " +
            "CM_CD_COMMENT AS cmCdComment, " +
            "STATUS AS status " +
            "FROM TB_CM_CODE " +
            "WHERE CM_CD LIKE 'BO%' " +
            "   OR CM_CD LIKE 'SM%' " +
            "ORDER BY NVL(CM_CD ,UP_CM_CD ),CM_CD, UP_CM_CD", nativeQuery = true)
    List<BoardManageDto> retrieveCmCode();

    void deleteByUpCmCd(String upCmCd);

    @Transactional
    @Modifying
    @Query(value = "UPDATE TB_CM_CODE SET STATUS = 'Y' WHERE CM_CD = :cmCd", nativeQuery = true)
    void reactivateBoard(@Param("cmCd") String cmCd);
}
