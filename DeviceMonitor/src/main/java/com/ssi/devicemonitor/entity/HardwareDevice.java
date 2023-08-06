package com.ssi.devicemonitor.entity;

public class HardwareDevice extends Device{
    private String location;
    private String mac;
    public HardwareDevice(String name, String manufacturer, String deviceType, String version, String location, String mac) {
        super(name,manufacturer,deviceType,version);
        this.location = location;
        this.mac = mac;
    }

    public String getLocation() {
        return location;
    }

    public String getMac() {
        return mac;
    }
}
