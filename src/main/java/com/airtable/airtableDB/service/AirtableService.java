package com.airtable.airtableDB.service;


import com.airtable.airtableDB.entity.Member;
import com.airtable.airtableDB.entity.Recordings;
import com.airtable.airtableDB.entity.Session;
import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableRecord;
import dev.fuxing.airtable.AirtableTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AirtableService {


    public boolean insertMemberInAirtable(Member member) {

        try {

            AirtableApi api = new AirtableApi("keye6pe51CammrRlq");
            AirtableTable table = api.base("appdcO4ssd2E4iSbM").table("member");
            AirtableRecord airtableRecord = new AirtableRecord();
            airtableRecord.putField("id", member.getId());
            airtableRecord.putField("memberuuid", member.getMemberUUID());
            airtableRecord.putField("email", member.getEmail());
            airtableRecord.putField("roles", member.getRoles().get(0));
            airtableRecord.putField("fullName", member.getFullName());
            table.post(airtableRecord);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());

        }
        return true;
    }

    public boolean insertSessionInAirtable(Session session) {

        try {
            AirtableApi api = new AirtableApi("keye6pe51CammrRlq");
            AirtableTable table = api.base("appdcO4ssd2E4iSbM").table("session");
            AirtableRecord airtableRecord = new AirtableRecord();
            airtableRecord.putField("id", session.getId());
            airtableRecord.putField("sessionuuid", session.getSessionUUID());
            airtableRecord.putField("sessionStatus", session.getSessionStatus());
            airtableRecord.putField("creatoruuid", session.getCreatorUUID());
            airtableRecord.putField("endTime", session.getEndTime());
            airtableRecord.putField("startTime", session.getStartTime());
            table.post(airtableRecord);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage() );
        }
        return true;
    }

    public boolean insertRecordingInAirtable(Recordings recordings) {

        try {
            AirtableApi api = new AirtableApi("keye6pe51CammrRlq");
            AirtableTable table = api.base("appdcO4ssd2E4iSbM").table("recordings");
            AirtableRecord airtableRecord = new AirtableRecord();
            airtableRecord.putField("id", recordings.getId());
            airtableRecord.putField("recordingUUID", recordings.getRecordingUUID());
            airtableRecord.putField("sessionUUID", recordings.getSessionUUID());
//            airtableRecord.putField("memberUUID", recordings.getMemberUUID());
            airtableRecord.putField("zoomUrl", recordings.getZoomUrl());
            airtableRecord.putField("recordingStart", recordings.getRecordingStart());
            airtableRecord.putField("recordingEnd", recordings.getRecordingEnd());
            table.post(airtableRecord);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());

        }
        return true;
    }
}
