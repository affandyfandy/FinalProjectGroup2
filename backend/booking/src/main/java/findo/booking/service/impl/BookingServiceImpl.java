package findo.booking.service.impl;

import findo.booking.client.ScheduleClient;
import findo.booking.client.StudioClient;
import findo.booking.client.UserClient;
import findo.booking.dto.BookingDetailDTO;
import findo.booking.dto.BookingResponseDTO;
import findo.booking.dto.BookingSeatsDTO;
import findo.booking.dto.CreateBookingDTO;
import findo.booking.dto.PrintTicketResponseDTO;
import findo.booking.entity.Booking;
import findo.booking.entity.BookingSeat;
import findo.booking.exception.TicketAlreadyPrintedException;
import findo.booking.mapper.BookingMapper;
import findo.booking.repository.BookingRepository;
import findo.booking.repository.BookingSeatRepository;
import findo.booking.service.BookingService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserClient userClient;
    private final ScheduleClient scheduleClient;
    private final StudioClient studioClient;
    private final PdfGeneratorServiceImpl pdfGeneratorServiceImpl;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingSeatRepository bookingSeatRepository,
            StudioClient studioClient,
            UserClient userClient, ScheduleClient scheduleClient, PdfGeneratorServiceImpl pdfGeneratorServiceImpl) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.userClient = userClient;
        this.pdfGeneratorServiceImpl = pdfGeneratorServiceImpl;
        this.scheduleClient = scheduleClient;
        this.studioClient = studioClient;
    }

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    // Show All Booking History (Admin)
    @Override
    public Mono<Page<BookingResponseDTO>> getAllBookings(Pageable pageable, String token) {
        return Mono.fromCallable(() -> bookingRepository.findAll(pageable))
                .flatMap(bookingsPage -> {
                    // Process each booking reactively
                    return Flux.fromIterable(bookingsPage.getContent())
                            .flatMap(booking -> {
                                // Fetch and map each ScheduleId to ScheduleMovieDTO reactively
                                return Flux.fromIterable(booking.getScheduleIds())
                                        .flatMap(scheduleId -> scheduleClient.getScheduleById(scheduleId, token))
                                        .collectList()
                                        .map(scheduleMovies -> {
                                            BookingResponseDTO responseDTO = bookingMapper
                                                    .toBookingResponseDTO(booking);
                                            responseDTO.setScheduleIds(scheduleMovies);
                                            return responseDTO;
                                        });
                            })
                            .collectList() // Collect all BookingResponseDTO into a List
                            .map(bookingResponseDTOList -> new PageImpl<>(
                                    bookingResponseDTOList, pageable, bookingsPage.getTotalElements()));
                });
    }

    @Override
    public Mono<Page<BookingResponseDTO>> getBookingHistoryByUser(UUID userId, Pageable pageable, String token) {
        return Mono.fromCallable(() -> bookingRepository.findBookingsByUserIds(userId, pageable))
                .flatMap(bookingsPage -> {
                    // Process each booking reactively
                    return Flux.fromIterable(bookingsPage.getContent())
                            .flatMap(booking -> {
                                // Fetch the related schedule data
                                return Flux.fromIterable(booking.getScheduleIds())
                                        .flatMap(scheduleId -> scheduleClient.getScheduleById(scheduleId, token))
                                        .collectList()
                                        .map(scheduleMovies -> {
                                            BookingResponseDTO responseDTO = bookingMapper
                                                    .toBookingResponseDTO(booking);
                                            responseDTO.setScheduleIds(scheduleMovies);
                                            return responseDTO;
                                        });
                            })
                            .collectList() // Collect all BookingResponseDTO into a List
                            .map(bookingResponseDTOList -> new PageImpl<>(
                                    bookingResponseDTOList, pageable, bookingsPage.getTotalElements()));
                });
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

    @Override
    public BookingSeatsDTO getAllSeatIds(UUID scheduleId) {
        List<BookingSeat> seats = bookingSeatRepository.findByBooking_ScheduleIdsContaining(scheduleId);

        List<Integer> seatIds = seats.stream()
                .flatMap(seat -> seat.getSeatIds().stream()) // Flatten the stream of seat IDs
                .collect(Collectors.toList());

        BookingSeatsDTO bookSeatIds = new BookingSeatsDTO(seatIds);

        return bookSeatIds;
    }
}
