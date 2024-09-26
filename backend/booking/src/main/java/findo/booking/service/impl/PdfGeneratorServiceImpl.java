package findo.booking.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.thymeleaf.context.Context;
import findo.booking.dto.ScheduleDetailsAdmin;
import findo.booking.service.PdfGeneratorService;
import findo.booking.service.impl.QRCodeGenerator;

import java.io.IOException;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final SpringTemplateEngine templateEngine;

    public PdfGeneratorServiceImpl(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public ByteArrayInputStream generatePdf(ScheduleDetailsAdmin scheduleDetailsAdmin) throws IOException {
        // Generate QR code for bookingId
        String qrCodeBase64;
        try {
            qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(scheduleDetailsAdmin.getBookingId().toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }

        // Set variables for Thymeleaf template
        Context context = new Context();
        context.setVariable("booking", scheduleDetailsAdmin);
        context.setVariable("qrCode", qrCodeBase64);

        // Process the Thymeleaf template
        String html = templateEngine.process("pdf-template", context);

        // Convert HTML to PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, "");
        builder.toStream(outputStream);
        builder.run();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
