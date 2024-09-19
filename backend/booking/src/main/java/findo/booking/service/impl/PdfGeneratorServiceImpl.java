package findo.booking.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.thymeleaf.context.Context;
import findo.booking.dto.BookingDetailDTO;
import findo.booking.service.PdfGeneratorService;

import java.io.IOException;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final SpringTemplateEngine templateEngine;

    public PdfGeneratorServiceImpl(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public ByteArrayInputStream generatePdf(BookingDetailDTO bookingDetailDTO) throws IOException {
        Context context = new Context();
        context.setVariable("booking", bookingDetailDTO);

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
