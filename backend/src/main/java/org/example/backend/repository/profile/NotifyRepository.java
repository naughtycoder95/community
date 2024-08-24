package org.example.backend.repository.profile;

import jakarta.transaction.Transactional;
import org.example.backend.model.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * packageName : org.example.backend.repository.profile
 * fileName : NotificationRepository
 * author : kimtaewan
 * date : 2024-05-22
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-22         kimtaewan          최초 생성
 */
@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE TB_NOTIFY\n" +
            "SET NOTI_CHECK = 'Y'\n" +
            "WHERE MEMBER_ID = :memberId\n" +
            "AND NOTI_CHECK = 'N'", nativeQuery = true)
    void updateNotiCheck(@Param("memberId") String memberId);
}