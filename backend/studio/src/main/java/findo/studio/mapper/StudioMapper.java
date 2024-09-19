package findo.studio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import findo.studio.data.entity.Studio;
import findo.studio.dto.StudioSaveDTO;
import findo.studio.dto.StudioShowDTO;

@Mapper(componentModel = "spring")
public interface StudioMapper {
    StudioMapper INSTANCE = Mappers.getMapper(StudioMapper.class);

    // Studio - StudioSaveDTO
    StudioSaveDTO toStudioSaveDTO(Studio studio);

    // StudioSaveDTO - Studio
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "seat", ignore = true)
    Studio toStudio(StudioSaveDTO studioSaveDTO);

    // Studio - StudioShowDTO
    StudioShowDTO toStudioShowDTO(Studio studio);

    // StudioShowDTO - Studio
    @Mapping(target = "seat", ignore = true)
    Studio toStudio(StudioShowDTO studioShowDTO);
}
