package com.playground.dto;

import lombok.Data;

import java.util.List;

@Data
public class SessionTranscriptFile {

    private String sessionId;
    private List<TranscriptFile> transcriptFiles;
}
