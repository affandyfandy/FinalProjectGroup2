package findo.booking.service.impl;

import findo.booking.client.UserClient;
import findo.booking.dto.BookingDetailDTO;
import findo.booking.dto.BookingResponseDTO;
import findo.booking.dto.CreateBookingDTO;
import findo.booking.dto.CustomerBookingHistoryDTO;
import findo.booking.dto.PrintTicketResponseDTO;
import findo.booking.entity.Booking;
import findo.booking.entity.BookingSeat;
import findo.booking.exception.TicketAlreadyPrintedException;
import findo.booking.mapper.BookingMapper;
import findo.booking.repository.BookingRepository;
import findo.booking.repository.BookingSeatRepository;
import findo.booking.service.BookingService;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserClient userClient;
    private final PdfGeneratorServiceImpl pdfGeneratorServiceImpl;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingSeatRepository bookingSeatRepository,
            UserClient userClient, PdfGeneratorServiceImpl pdfGeneratorServiceImpl) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.userClient = userClient;
        this.pdfGeneratorServiceImpl = pdfGeneratorServiceImpl;
    }

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    // Show All Booking History (Admin)
    @Override
    public Page<BookingResponseDTO> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(bookingMapper::toBookingResponseDTO);
    }

    // Show Booking History (Customer)
    @Override
    public Mono<Page<CustomerBookingHistoryDTO>> getBookingHistoryByUser(UUID userId, Pageable pageable) {
        return Mono.just(
                bookingRepository.findBookingsByUserIds(userId, pageable)
                        .map(bookingMapper::toCustomerBookingHistoryDTO));
    }

    // Show Booking Detail (Customer)
    @Override
    public BookingDetailDTO getBookingDetail(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingIdWithSeatIds(bookingId);
        return bookingMapper.toBookingDetailDTO(booking, bookingSeats);
    }

    // Create Booking (Customer)
    @Override
    public Mono<Booking> createBooking(CreateBookingDTO request, UUID userId, String token, String email) {
        return userClient.getUserBalance(userId, token)
                .flatMap(userBalanceDTO -> {
                    if (userBalanceDTO.getBalance() < request.getTotalAmount()) {
                        return Mono.error(new RuntimeException("Insufficient balance"));
                    }

                    // Proceed with booking creation
                    Booking booking = new Booking();
                    booking.setUserIds(userId);
                    booking.setTotalAmount(request.getTotalAmount());
                    booking.setScheduleIds(request.getScheduleIds());
                    booking.setIsPrinted(false);
                    booking.setCreatedBy(email);
                    booking.setUpdatedBy(email);

                    Timestamp now = Timestamp.from(Instant.now());
                    booking.setCreatedTime(now);
                    booking.setUpdatedTime(now);

                    Booking savedBooking = bookingRepository.save(booking);

                    BookingSeat bookingSeat = new BookingSeat();
                    bookingSeat.setBooking(savedBooking);
                    bookingSeat.setSeatIds(request.getSeatIds());
                    bookingSeatRepository.save(bookingSeat);

                    // Update user balance
                    double newBalance = userBalanceDTO.getBalance() - request.getTotalAmount();
                    return userClient.updateUserBalance(userId, newBalance, token)
                            .thenReturn(savedBooking);
                });
    }

    @Override
    public Mono<PrintTicketResponseDTO> printTicket(UUID bookingId, String email) {
        return Mono.justOrEmpty(bookingRepository.findById(bookingId))
                .switchIfEmpty(Mono.error(new RuntimeException("Booking not found")))
                .flatMap(booking -> {
                    Timestamp now = Timestamp.from(Instant.now());
                    booking.setUpdatedTime(now);
                    booking.setUpdatedBy(email);
                    if (Boolean.TRUE.equals(booking.getIsPrinted())) {
                        throw new TicketAlreadyPrintedException("Ticket has already been printed");
                    }

                    booking.setIsPrinted(true);
                    bookingRepository.save(booking);

                    // Construct BookingDetailDTO
                    List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingIdWithSeatIds(bookingId);
                    BookingDetailDTO bookingDetailDTO = bookingMapper.toBookingDetailDTO(booking, bookingSeats);

                    // Generate PDF
                    ByteArrayInputStream pdfInputStream;
                    try {
                        pdfInputStream = pdfGeneratorServiceImpl.generatePdf(bookingDetailDTO);
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Error generating PDF", e));
                    }

                    // Create PrintTicketResponseDTO with PDF
                    return Mono.just(new PrintTicketResponseDTO(booking.getId(), true, "Ticket successfully printed",
                            pdfInputStream));
                });
    }
}
