package com.playground.domain;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.playground.dto.TranscriptFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
