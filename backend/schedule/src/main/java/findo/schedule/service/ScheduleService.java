package findo.schedule.service;

import findo.schedule.dto.CreateScheduleDTO;
import findo.schedule.dto.ScheduleResponseDTO;

public interface ScheduleService {
    ScheduleResponseDTO createSchedule(CreateScheduleDTO dto, String email);
}
