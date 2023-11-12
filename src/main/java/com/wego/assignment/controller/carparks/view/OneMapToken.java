package com.wego.assignment.controller.carparks.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OneMapToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("entry_timestamp")
    private String entryTimeStamp;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEntryTimeStamp() {
        return entryTimeStamp;
    }

    public void setEntryTimeStamp(String entryTimeStamp) {
        this.entryTimeStamp = entryTimeStamp;
    }
}
