package com.airtable.airtableDB.dto;

import lombok.Data;

@Data
public class StatusChangeTrascriptDTO {

    private Boolean status;

    private String userId;

    private String sessionId;
}
