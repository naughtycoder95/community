package org.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * packageName : org.example.backend.model.entity
 * fileName : Notify
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
@Entity
@Table(name = "TB_NOTIFY")
@SequenceGenerator(
        name = "TB_NOTIFY_SEQ_GENERATOR"
        , sequenceName = "TB_NOTIFY_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "TB_NOTIFY_SEQ_GENERATOR")
    private Long notifyId;
    private String memberId;
    private String notiCheck = "N";
    private String notiContent;
    private String notiUrl;
    private String addDate;
    private String delDate;

    @PrePersist
    void OnPrePersist() {
        this.addDate = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
