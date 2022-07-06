package com.playground.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
public class Recordings {
	@Id
	private String id;
	@Indexed(unique = true)	
	private String recordingUUID;
	private String sessionUUID;
	private String groupUUID;
	private String memberUUID;
	private String zoomUrl;
	private String awsUrl;
	private LocalDateTime date;
}
