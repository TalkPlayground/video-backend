package com.playground.dto;

import lombok.Data;

@Data
public class StatusChangeDTO {

    private Boolean status;

    private String userId;

    private String sessionId;

}
