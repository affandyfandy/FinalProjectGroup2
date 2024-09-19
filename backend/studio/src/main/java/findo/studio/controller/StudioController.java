package findo.studio.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import findo.studio.data.entity.Seat;
import findo.studio.data.entity.Studio;
import findo.studio.dto.StudioSaveDTO;
import findo.studio.service.SeatService;
import findo.studio.service.StudioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/studios")
@Validated
@AllArgsConstructor
public class StudioController {

    private final StudioService studioService;
    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<Page<Studio>> getAllStudios(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Studio> studioPage = studioService.findAllStudio(pageable);

        if(studioPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(studioPage);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<List<Studio>> getAllActiveStudios() {
        List<Studio> studios = studioService.findAllActiveStudio();

        if(studios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(studios);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Studio> getStudioById(@PathVariable("id") Integer id) {
        Studio studio = studioService.findStudioById(id);

        if(studio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(studio);
    }

    @GetMapping(value = "/{id}/seats")
    public ResponseEntity<List<Seat>> getSeatsByStudioId(@PathVariable("id") Integer studioId) {
        List<Seat> seats = seatService.findAllSeatByStudioId(studioId);

        if(seats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(seats);
    }

    @GetMapping(value = "/seats/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable("id") Integer id) {
        Seat seat = seatService.findSeatById(id);

        if(seat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(seat);
    }

    @PostMapping
    public ResponseEntity<Studio> createStudio(@Valid @RequestBody StudioSaveDTO studioSaveDTO) {
        Studio studio = studioService.createStudio(studioSaveDTO);

        if(studio == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(studio);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Studio> updateStudio(@PathVariable("id") Integer id, @Valid @RequestBody StudioSaveDTO studioSaveDTO) {
        Studio studio = studioService.updateStudio(id, studioSaveDTO);
        
        if(studio == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(studio);
    }

    @PatchMapping(value = "/{id}/change-status")
    public ResponseEntity<Studio> updateStatusStudio(@PathVariable("id") Integer id) {
        Studio studio = studioService.deleteStudio(id);
        
        if(studio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(studio);
    }
}
