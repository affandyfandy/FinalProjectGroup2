package findo.movie.core;

import lombok.Getter;

@Getter
public enum AppConstant {

    MovieNotFoundMsg("Movie not found!"),
    MovieAlreadyExistMsg("Movie with this title already exists!"),
    MovieFileCriteriaMsg("Only image files (JPEG, PNG, GIF) are allowed."),
    MovieErrorUploadImageMsg("Error uploading file to Cloudinary");

    private String value;

    private AppConstant(String value) {
        this.value = value;
    }
}
