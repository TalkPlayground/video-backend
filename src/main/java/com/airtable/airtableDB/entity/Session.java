package com.airtable.airtableDB.entity;

import com.airtable.airtableDB.dto.TranscriptFile;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Session {


    @Id
    private String id;
    @Indexed(unique = true)
    private String sessionUUID;
    private Set<String> memberUUID;
    private String creatorUUID;
    private String startTime;
    private String endTime;
    private boolean hasRecording;
    private String sessionStatus;
    private List<TranscriptFile> transcriptFiles;

}
