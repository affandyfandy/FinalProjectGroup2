package findo.studio.dto;

import findo.studio.data.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllSeatStudioDTO {
    private List<Seat> studioSeats;
}
