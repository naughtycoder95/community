package org.example.backend.model.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * packageName : org.example.security.model.common
 * fileName : BaseTimeEntity2
 * author : kimtaewan
 * date : 2024-04-18
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-18         kimtaewan          최초 생성
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity3 {

    private String addDate;
    private String delDate;
    private String status;

    @PrePersist
    void OnPrePersist() {
        this.addDate = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = "Y";
    }
}