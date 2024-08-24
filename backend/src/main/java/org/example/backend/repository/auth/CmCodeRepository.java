package org.example.backend.repository.auth;

import org.example.backend.model.entity.CmCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName : org.example.backend.repository.auth
 * fileName : CmCodeRepository
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
@Repository
public interface CmCodeRepository extends JpaRepository<CmCode, String> {
    //    동호회게시판 중분류 코드
    @Query(value = "SELECT * FROM TB_CM_CODE\n" +
            "WHERE CM_CD LIKE 'SM%'",
            nativeQuery = true)
    List<CmCode> findByCmCdNameAndSmcode();


}
