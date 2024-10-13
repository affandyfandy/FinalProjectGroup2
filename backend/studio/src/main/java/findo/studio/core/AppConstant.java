package findo.studio.core;

import lombok.Getter;

@Getter
public enum AppConstant {

    StudioSeatNotFoundMsg("Seat not found!"),
    StudioSeatinStudioNotFoundMsg("Studio seat not found!"),
    StudioNotFound("Studio not found!"),
    StudioAlreadyExist("Studio with this name already exists!");

    private String value;

    private AppConstant(String value) {
        this.value = value;
    }
}
