package com.airtable.airtableDB.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Recordings {

    @Id
    private String id;
    @Indexed(unique = true)
    private String recordingUUID;
    private String sessionUUID;
    private String memberUUID;
    private String zoomUrl;
    private String awsUrl;
    private String recordingStart;
    private String recordingEnd;

}
