package com.playground.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordingPayload {
	public String id;
    public String recording_start;
    public String recording_end;
    public String file_name;
    public String file_type;
    public String file_extension;
    public int file_size;
    public String download_url;
    public String status;
}
