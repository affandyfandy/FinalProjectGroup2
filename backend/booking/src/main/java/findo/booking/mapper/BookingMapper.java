package findo.booking.mapper;

import findo.booking.dto.BookingDetailDTO;
import findo.booking.dto.BookingResponseDTO;
import findo.booking.dto.CreateBookingDTO;
import findo.booking.dto.CustomerBookingHistoryDTO;
import findo.booking.entity.Booking;
import findo.booking.entity.BookingSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    // Convert Booking entity to BookingResponseDTO
    BookingResponseDTO toBookingResponseDTO(Booking booking);

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

    // Convert CreateBookingRequestDTO to Booking entity
    @Mapping(source = "userId", target = "userIds")
    Booking toBookingEntity(CreateBookingDTO request);
}