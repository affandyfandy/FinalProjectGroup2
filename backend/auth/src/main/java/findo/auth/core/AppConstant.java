package findo.auth.core;

import lombok.Getter;

@Getter
public enum AppConstant {
    AuthRegisterSuccessMsg("User registered successfully: "),
    AuthRoleCustomerMsg("ROLE_CUSTOMER"),
    AuthUserNotFoundMsg("User not found");

    private String value;

    private AppConstant(String value) {
        this.value = value;
    }
}
