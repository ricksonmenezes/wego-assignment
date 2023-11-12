package com.wego.assignment.controller.carparks.view;

import java.io.Serializable;

public class OneMapTokenRequest implements Serializable {

    private String email;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private String password;

    public OneMapTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
