package org.example.backend.model.entity.board;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_PLACE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    @Id
    private Long boardId;       // 게시글ID pk
    private String address;     // 주소
}
