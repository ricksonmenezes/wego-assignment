package com.wego.assignment.controller.carparks.exception;

public class CarParkException extends Exception {

    public CarParkException(String msg, Throwable t) {
        super(msg, t);
    }

    public CarParkException(String s) {
        super(s);
    }
}
