package com.playground.domain;

import java.util.concurrent.ConcurrentHashMap;

public class OtpSessions {

    private ConcurrentHashMap<String, String> otp = new ConcurrentHashMap<>();

    public void setOtp(String email, String otp) {
        this.otp.put(email, otp);
    }

    public String getOtp(String email) {
        return otp.get(email);
    }

}
