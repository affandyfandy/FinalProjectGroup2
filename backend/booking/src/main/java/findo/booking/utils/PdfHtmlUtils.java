package findo.booking.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class PdfHtmlUtils {
    public static ByteArrayInputStream convertHtmlToPdf(String html) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8))) {
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
        } catch (Exception e) {
            throw new IOException("Error while generating PDF", e);
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
