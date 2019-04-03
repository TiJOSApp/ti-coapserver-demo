package com.tijos.coapserver.entity;

/**
 * Device command to device
 */
public class TiDeviceCommand {

    /**
     * timestamp of the command
     */
    private long timeStamp = System.currentTimeMillis();

    /**
     * command string in json
     */
    private String textCommand;

    public TiDeviceCommand(String textCommand) {
        this.textCommand = textCommand;
    }


    public String getTextCommand() {
        return textCommand;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
