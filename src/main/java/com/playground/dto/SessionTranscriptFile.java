package com.playground.dto;

import java.util.List;

import lombok.Data;

@Data
public class SessionTranscriptFile {
	
	private String sessionId;
	private List<TranscriptFile> transcriptFiles;
}
