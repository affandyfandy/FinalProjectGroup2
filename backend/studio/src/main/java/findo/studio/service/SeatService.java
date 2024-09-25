package findo.studio.service;

import findo.studio.data.entity.Seat;
import findo.studio.data.entity.Studio;
import findo.studio.dto.AllSeatStudioDTO;

public interface SeatService {
    Seat findSeatById(Integer id);

    AllSeatStudioDTO findAllSeatByStudioId(Integer studioId);

    void generateSeats(Studio studio);
}
