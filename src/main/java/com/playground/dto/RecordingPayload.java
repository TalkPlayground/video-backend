package com.playground.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordingPayload {
	 private String id;
	    private String recordingStart;
	    private String recordingEnd;
	    private String fileName;
	    private String fileType;
	    private String fileExtension;
	    private int fileSize;
	    private String downloadUrl;
	    private String status;
}
