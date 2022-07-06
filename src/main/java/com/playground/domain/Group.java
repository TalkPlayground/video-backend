package com.playground.domain;

import java.util.Set;

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
public class Group {
	@Id
	private String id;
    @Indexed(unique = true)
	private String groupUUID;
    private Set<String> memberUUID;
    private String groupName;
    private String creatorUUID;
    
}
