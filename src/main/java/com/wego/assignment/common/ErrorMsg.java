package com.wego.assignment.common;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;

public class ErrorMsg implements Serializable {

    private static final long serialVersionUID = 7574103833700709620L;

    @Transient
    private String errorCode;

    @Transient
    private String filedName;

    @Transient
    private String fieldValue;

    public ErrorMsg(String errorCode) {
        super();
        this.errorCode = errorCode;
    }


    public ErrorMsg(String errorCode, String filedName) {
        super();
        this.errorCode = errorCode;
        this.filedName = filedName;
    }


    public ErrorMsg(String errorCode, String filedName, String fieldValue) {
        super();
        this.errorCode = errorCode;
        this.filedName = filedName;
        this.fieldValue = fieldValue;
    }


    public String getFiledName() {
        return filedName;
    }



    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getFieldValue() {
        return fieldValue;
    }


    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }



}
