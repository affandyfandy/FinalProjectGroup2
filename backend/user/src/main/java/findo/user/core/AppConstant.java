package findo.user.core;

import lombok.Getter;

@Getter
public enum AppConstant {
    UserNotFoundMsg("User not found with ID: "),
    UserPasswordChangeSuccess("Password Changed Successfully!"),
    UserPasswordIncorect("Old password is incorrect");

    private String value;

    private AppConstant(String value) {
        this.value = value;
    }
}
