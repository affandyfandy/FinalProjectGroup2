package findo.movie.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import findo.movie.core.AppConstant;

@Component
public class ImgUtils {

    @Autowired
    private Cloudinary cloudinary;

    private static final List<String> ALLOWED_IMAGE_TYPE = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif");

    public String uploadFile(MultipartFile file) {
        if (!isImageFile(file)) {
            throw new IllegalArgumentException(AppConstant.MovieFileCriteriaMsg.getValue());
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException(AppConstant.MovieErrorUploadImageMsg.getValue(), e);
        }
    }

    boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return ALLOWED_IMAGE_TYPE.contains(contentType);
    }
}
