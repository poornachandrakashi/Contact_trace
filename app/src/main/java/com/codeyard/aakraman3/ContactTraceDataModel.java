package com.codeyard.aakraman3;

import com.orm.SugarRecord;

public class ContactTraceDataModel extends SugarRecord {
    private String myId;
    private String otherId;
    private String timestamp;

    //    For SugarDB
    public ContactTraceDataModel() {

    }

    public ContactTraceDataModel(String myId, String otherId, String timestamp) {
        this.myId = myId;
        this.otherId = otherId;
        this.timestamp = timestamp;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
