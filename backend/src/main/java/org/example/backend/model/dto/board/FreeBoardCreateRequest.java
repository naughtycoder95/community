package org.example.backend.model.dto.board;

import lombok.*;
import org.example.backend.model.entity.board.Board;
import org.example.backend.model.entity.board.Place;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FreeBoardCreateRequest {
    private Board board;
    private List<VoteDto> voteDtos;
    private Place place;
    private List<FileDto> fileDtos;
    private List<BoardFileDto> boardFileDtos;
}
