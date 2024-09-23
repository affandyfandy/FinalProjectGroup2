package findo.studio.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import findo.studio.service.StudioService;
import lombok.AllArgsConstructor;

import findo.studio.data.entity.Studio;
import findo.studio.data.repository.StudioRepository;
import findo.studio.dto.StudioSaveDTO;
import findo.studio.exception.DuplicateStudioException;
import findo.studio.exception.NotFoundException;
import findo.studio.mapper.StudioMapper;
import findo.studio.service.SeatService;

@Service
@AllArgsConstructor
public class StudioServiceImpl implements StudioService {

    private final StudioRepository studioRepository;
    private final StudioMapper studioMapper;
    private final SeatService seatService;

    @Override
    public Page<Studio> findAllStudio(Pageable pageable) {
        return studioRepository.findAll(pageable);
    }

    @Override
    public List<Studio> findAllActiveStudio() {
        return studioRepository.findByDeleted(false);
    }

    @Override
    public Studio findStudioById(Integer id) {
        return studioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Studio not found!"));
    }

    @Override
    public Studio createStudio(StudioSaveDTO studioSaveDTO) {
        if (studioRepository.existsByName(studioSaveDTO.getName())) {
            throw new DuplicateStudioException("Studio with this name already exists!");
        }

        Studio savedStudio = studioMapper.toStudio(studioSaveDTO);

        savedStudio.setCreatedBy("admin");
        savedStudio.setUpdatedBy("admin");
        savedStudio.setCreatedTime(LocalDate.now());
        savedStudio.setUpdatedTime(LocalDate.now());

        Studio createdStudio = studioRepository.save(savedStudio);

        // Generate Seat
        seatService.generateSeats(createdStudio);

        return createdStudio;
    }

    @Override
    public Studio updateStudio(Integer id, StudioSaveDTO studioSaveDTO) {
        Studio checkStudio = findStudioById(id);

        if (!checkStudio.getName().equals(studioSaveDTO.getName())
                && studioRepository.existsByName(studioSaveDTO.getName())) {
            throw new DuplicateStudioException("Studio with this name already exists!");
        }

        checkStudio.setName(studioSaveDTO.getName());
        checkStudio.setUpdatedBy("admin");
        checkStudio.setUpdatedTime(LocalDate.now());

        Studio updatedStudio = studioRepository.save(checkStudio);

        return updatedStudio;
    }

    @Override
    public Studio deleteStudio(Integer id) {
        Studio checkStudio = findStudioById(id);

        checkStudio.setDeleted(!checkStudio.isDeleted());

        Studio updatedStudio = studioRepository.save(checkStudio);

        return updatedStudio;
    }
}
