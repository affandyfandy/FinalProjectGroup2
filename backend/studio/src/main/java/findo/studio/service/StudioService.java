package findo.studio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import findo.studio.data.entity.Studio;
import findo.studio.dto.StudioSaveDTO;

public interface StudioService {
    Page<Studio> findAllStudio(Pageable pageable);
    Studio findStudioById(Integer id);
    Studio createStudio(StudioSaveDTO studioSaveDTO);
    Studio updateStudio(Integer id, StudioSaveDTO studioSaveDTO);
    Studio deleteStudio(Integer id);
}
