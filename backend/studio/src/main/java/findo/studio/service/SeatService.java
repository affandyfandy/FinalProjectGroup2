package findo.studio.service;

import java.util.List;

import findo.studio.data.entity.Seat;
import findo.studio.data.entity.Studio;

public interface SeatService {
    Seat findSeatById(Integer id);
    List<Seat> findAllSeatByStudioId(Integer studioId);
    void generateSeats(Studio studio);
}
