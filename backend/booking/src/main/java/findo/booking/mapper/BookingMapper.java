package findo.booking.mapper;

import findo.booking.dto.BookingDetailDTO;
import findo.booking.dto.BookingResponseDTO;
import findo.booking.dto.CreateBookingDTO;
import findo.booking.dto.CustomerBookingHistoryDTO;
import findo.booking.dto.ScheduleMovieDTO;
import findo.booking.entity.Booking;
import findo.booking.entity.BookingSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    // Convert Booking entity to BookingResponseDTO
    @Mapping(source = "scheduleIds", target = "scheduleIds")
    BookingResponseDTO toBookingResponseDTO(Booking booking);

    // Custom method to map List<UUID> to List<ScheduleMovieDTO>
    List<ScheduleMovieDTO> map(List<UUID> scheduleIds);

    // Custom method to map UUID to ScheduleMovieDTO
    default ScheduleMovieDTO map(UUID scheduleId) {
        ScheduleMovieDTO dto = new ScheduleMovieDTO();
        dto.setScheduleId(scheduleId);
        return dto;
    }

    // Convert Booking entity to CustomerBookingHistoryDTO
    @Mapping(source = "isPrinted", target = "printed")
    @Mapping(source = "id", target = "bookingId")
    CustomerBookingHistoryDTO toCustomerBookingHistoryDTO(Booking booking);

    // Convert Booking entity to BookingDetailDTO
    @Mapping(source = "booking.id", target = "bookingId")
    @Mapping(source = "booking.isPrinted", target = "printed")
    @Mapping(target = "seatIds", expression = "java(extractSeatIds(bookingSeats))")
    BookingDetailDTO toBookingDetailDTO(Booking booking, List<BookingSeat> bookingSeats);

    default List<Integer> extractSeatIds(List<BookingSeat> bookingSeats) {
        return bookingSeats.stream()
                .flatMap(seat -> seat.getSeatIds().stream())
                .collect(Collectors.toList());
    }

    default List<ScheduleMovieDTO> getScheduleMovieDTOs(List<UUID> scheduleIds, String token) {
        return scheduleIds.stream()
                .map(scheduleId -> {
                    ScheduleMovieDTO scheduleMovieDTO = new ScheduleMovieDTO();
                    scheduleMovieDTO.setScheduleId(scheduleId);
                    // Additional properties can be set here based on your logic and available data
                    return scheduleMovieDTO;
                })
                .collect(Collectors.toList());
    }

    // Convert CreateBookingRequestDTO to Booking entity
    @Mapping(source = "userId", target = "userIds")
    Booking toBookingEntity(CreateBookingDTO request);
}