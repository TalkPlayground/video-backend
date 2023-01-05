package com.playground.dto;

import lombok.Data;

@Data
public class ErrorLogsDTO {

    private String sessionId;
    private String userId;
    private String timeStamp;
    private String consoleErrorMessage;
    private String browserDetails;
    private String computerOS;
    private String browserVersion;
    private String sectionBug;

}
