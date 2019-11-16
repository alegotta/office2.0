package com.office.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DataPhone {
    private long timestamp;
    private int uid;
    private DataBeacon[] data;

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public DataBeacon[] getData() {
        return this.data;
    }

    @JsonDeserialize(contentAs = DataBeacon.class)
    public void setData(DataBeacon[] data) {
        this.data = data;
    }
}
