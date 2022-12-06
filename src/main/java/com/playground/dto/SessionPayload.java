package com.playground.dto;

import lombok.Data;

@Data
public class SessionPayload {
    public String id;
    public String session_name;
    public String start_time;
    public String end_time;
    public String duration;
    public int user_count;
    public boolean has_voip;
    public boolean has_video;
    public boolean has_screen_share;
    public boolean has_recording;
    public boolean has_pstn;
    public String session_key;
}
