package findo.booking.service;

import findo.booking.dto.ScheduleDetailsAdmin;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface PdfGeneratorService {
    ByteArrayInputStream generatePdf(ScheduleDetailsAdmin scheduleDetailsAdmin) throws IOException;
}
