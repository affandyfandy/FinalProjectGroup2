package findo.schedule.mapper;

import findo.schedule.dto.CreateScheduleDTO;
import findo.schedule.dto.ScheduleResponseDTO;
import findo.schedule.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    // Map DTO to Schedule entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "price", source = "price")
    Schedule toEntity(CreateScheduleDTO dto);

    // Map Schedule entity to ScheduleResponseDTO
    ScheduleResponseDTO toResponseDto(Schedule schedule);

}
