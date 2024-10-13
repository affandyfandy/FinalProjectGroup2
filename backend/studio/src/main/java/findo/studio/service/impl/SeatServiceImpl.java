package findo.studio.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import findo.studio.core.AppConstant;
import findo.studio.data.entity.Seat;
import findo.studio.data.entity.Studio;
import findo.studio.data.repository.SeatRepository;
import findo.studio.dto.AllSeatStudioDTO;
import findo.studio.exception.NotFoundException;
import findo.studio.service.SeatService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public void generateSeats(Studio studio) {
        List<Seat> listSeats = new ArrayList<>();

        List<String> listSeatCode = new ArrayList<>(Arrays.asList(
                "A1", "A2", "A3", "A4", "A5", "A6",
                "B1", "B2", "B3", "B4", "B5", "B6",
                "C1", "C2", "C3", "C4", "C5", "C6",
                "D1", "D2", "D3", "D4", "D5", "D6",
                "E1", "E2", "E3", "E4", "E5", "E6"));

        for (int i = 0; i < listSeatCode.size(); i++) {
            Seat seat = new Seat();
            seat.setSeatCode(listSeatCode.get(i));
            seat.setCreatedBy("admin");
            seat.setUpdatedBy("admin");
            seat.setCreatedTime(LocalDate.now());
            seat.setUpdatedTime(LocalDate.now());
            seat.setStudio(studio);

            listSeats.add(seat);
        }

        seatRepository.saveAll(listSeats);
    }

    @Override
    public Seat findSeatById(Integer id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AppConstant.StudioSeatNotFoundMsg.getValue()));
    }

    @Override
    public AllSeatStudioDTO findAllSeatByStudioId(Integer studioId) {
        List<Seat> seatStudio = seatRepository.findByStudioId(studioId);

        if (seatStudio.isEmpty()) {
            throw new NotFoundException(AppConstant.StudioSeatinStudioNotFoundMsg.getValue());
        }

        AllSeatStudioDTO listStudioSeats = new AllSeatStudioDTO(seatStudio);

        return listStudioSeats;
    }
}
