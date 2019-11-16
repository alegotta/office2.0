package com.office.domain;

public class DataBeacon {
    private String address;
    private int rssi;
    private int txPower;
    private double distance;
    private String namespaceId;
    private String instanceId;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getTxPower() {
        return this.txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getNameSpaceId() {
        return this.namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString(){
        String prova = distance + " " + address;
        return prova;

    }
    
}