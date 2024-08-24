package org.example.backend.service.board;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.board.PlaceDto;
import org.example.backend.model.entity.board.Place;
import org.example.backend.repository.board.PlaceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;

    public void savePlace(Long boardId, PlaceDto placeDto) {
        Place place = modelMapper.map(placeDto, Place.class);
        place.setBoardId(boardId);

        placeRepository.save(place);
    }
}
