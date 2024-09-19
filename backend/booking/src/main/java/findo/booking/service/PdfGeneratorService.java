package findo.booking.service;

import findo.booking.dto.BookingDetailDTO;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface PdfGeneratorService {
    ByteArrayInputStream generatePdf(BookingDetailDTO bookingDetailDTO) throws IOException;
}
