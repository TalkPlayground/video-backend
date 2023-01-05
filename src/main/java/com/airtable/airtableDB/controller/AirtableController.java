package com.airtable.airtableDB.controller;

import com.airtable.airtableDB.dto.ErrorLogsDTO;
import com.airtable.airtableDB.dto.StatusChangeTrascriptDTO;
import com.airtable.airtableDB.entity.Member;
import com.airtable.airtableDB.entity.Recordings;
import com.airtable.airtableDB.entity.Session;
import com.airtable.airtableDB.service.AirtableService;
import com.airtable.airtableDB.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class AirtableController {

    @Autowired
    AirtableService airtableService;

    @PostMapping("/v1/user/session/airtable")
    public ResponseEntity<Object> insertMemberInAirtable(@RequestBody Member member) {
        boolean response = airtableService.insertMemberInAirtable(member);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, "Success", true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
    }

    @PostMapping("/v1/user/session/store/airtable")
    public ResponseEntity<Object> insertSessionInAirtable(@RequestBody Session session) {
        boolean response = airtableService.insertSessionInAirtable(session);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, "Success", true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
    }

    @PostMapping("/v1/user/session/recording/airtable")
    public ResponseEntity<Object> insertRecordingInAirtable(@RequestBody Recordings recordings) {
        boolean response = airtableService.insertRecordingInAirtable(recordings);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, "Success", true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
    }

    @PostMapping("/v1/user/transcripts/status/airtable")
    public ResponseEntity<Object> updateDeleteStatusInAirtable(@RequestBody StatusChangeTrascriptDTO status) {
        try {
            boolean response = airtableService.updateStatusInAirtable(status);
            if (response) {
                return Response.generateResponse(HttpStatus.OK, response, "Success", true);
            }
            return Response.generateResponse(HttpStatus.OK, response, "Status Not Changed", false);
        } catch (Exception e) {
            return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
        }
    }

    @PostMapping("/v1/user/airtableCL/errorLogs")
    public ResponseEntity<Object> errorLogsAirtableCL(@RequestBody ErrorLogsDTO status) {
        try {
            boolean response = airtableService.errorLogsAirtableCL(status);
            if (response) {
                return Response.generateResponse(HttpStatus.OK, response, "Success", true);
            }
            return Response.generateResponse(HttpStatus.OK, response, "Status Not Changed", false);
        } catch (Exception e) {
            return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
        }
    }

}
