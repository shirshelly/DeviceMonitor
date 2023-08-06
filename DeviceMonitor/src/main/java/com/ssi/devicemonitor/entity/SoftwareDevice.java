package com.ssi.devicemonitor.entity;

public class SoftwareDevice extends Device {
    private String installTime;
    public SoftwareDevice(String name, String manufacturer, String deviceType, String version, String installTime) {
        super(name,manufacturer,deviceType,version);
        this.installTime = installTime;
    }

    public String getInstallTime() {
        return installTime;
    }
}
