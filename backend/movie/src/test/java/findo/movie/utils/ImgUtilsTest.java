package findo.movie.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImgUtilsTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private ImgUtils imgUtils;

    @Test
    void uploadFile_withValidImageFile_shouldReturnUrl() throws IOException {
        MultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test image content".getBytes());
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "https://example.com/image.jpg");
        
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any())).thenReturn(uploadResult);

        String result = imgUtils.uploadFile(file);

        assertEquals("https://example.com/image.jpg", result);
        verify(uploader).upload(any(byte[].class), any());
    }

    @Test
    void uploadFile_withInvalidFileType_shouldThrowIllegalArgumentException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", "test content".getBytes());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> imgUtils.uploadFile(file));
        assertEquals("Only image files (JPEG, PNG, GIF) are allowed.", exception.getMessage());
    }

    @Test
    void uploadFile_withIOException_shouldThrowRuntimeException() throws IOException {
        MultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test image content".getBytes());
        
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any())).thenThrow(new IOException("Upload failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> imgUtils.uploadFile(file));
        assertEquals("Error uploading file to Cloudinary", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void isImageFile_withValidImageTypes_shouldReturnTrue() {
        MultipartFile jpegFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new byte[0]);
        MultipartFile pngFile = new MockMultipartFile("test.png", "test.png", "image/png", new byte[0]);
        MultipartFile gifFile = new MockMultipartFile("test.gif", "test.gif", "image/gif", new byte[0]);

        assertTrue(imgUtils.isImageFile(jpegFile));
        assertTrue(imgUtils.isImageFile(pngFile));
        assertTrue(imgUtils.isImageFile(gifFile));
    }

    @Test
    void isImageFile_withInvalidImageType_shouldReturnFalse() {
        MultipartFile textFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", new byte[0]);

        assertFalse(imgUtils.isImageFile(textFile));
    }
}
