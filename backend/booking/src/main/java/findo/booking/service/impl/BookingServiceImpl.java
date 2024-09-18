package findo.booking.service.impl;

import findo.booking.dto.BookingDetailDTO;
import findo.booking.dto.BookingResponseDTO;
import findo.booking.dto.CreateBookingDTO;
import findo.booking.dto.CustomerBookingHistoryDTO;
import findo.booking.entity.Booking;
import findo.booking.entity.BookingSeat;
import findo.booking.mapper.BookingMapper;
import findo.booking.repository.BookingRepository;
import findo.booking.repository.BookingSeatRepository;
import findo.booking.service.BookingService;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingSeatRepository bookingSeatRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    // Use MapStruct for conversion
    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    // Show All Booking History (Admin)
    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toBookingResponseDTO)
                .collect(Collectors.toList());
    }

    // Show Booking History (Customer)
    @Override
    public Mono<List<CustomerBookingHistoryDTO>> getBookingHistoryByUser(UUID userId) {
        List<CustomerBookingHistoryDTO> bookingHistory = bookingRepository.findBookingsByUserIds(userId).stream()
                .map(bookingMapper::toCustomerBookingHistoryDTO)
                .collect(Collectors.toList());

        return Mono.just(bookingHistory);
    }

    // Show Booking Detail (Customer)
    @Override
    public BookingDetailDTO getBookingDetail(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);
        return bookingMapper.toBookingDetailDTO(booking, bookingSeats);
    }

    // Create Booking (Customer)
    @Override
    public Booking createBooking(CreateBookingDTO request, UUID userId) {
        Booking booking = bookingMapper.toBookingEntity(request);
        booking.setUserIds(userId);
        booking.setIsPrinted(false);
        booking = bookingRepository.save(booking);

        BookingSeat bookingSeat = new BookingSeat();
        bookingSeat.setBooking(booking);
        bookingSeat.setSeatIds(request.getSeatIds());
        bookingSeatRepository.save(bookingSeat);

        return booking;
    }
}
