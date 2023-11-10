package com.wego.assignment.common.resttemplate;

import java.io.IOException;

public class CarParkAPIException extends IOException {

    public CarParkAPIException(String message) {
        super(message);
    }

    public CarParkAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
