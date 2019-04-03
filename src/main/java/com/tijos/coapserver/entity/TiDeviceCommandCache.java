package com.tijos.coapserver.entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Device Command cache, every device has a command cache for remote request
 * there is no limit for the number currently, maybe limited in the future
 */
public class TiDeviceCommandCache {

    private String devicePath;

    //command deque
    private Deque<TiDeviceCommand> commandDeque =  new ArrayDeque<>();

    //device path
    public String getDevicePath() {
        return devicePath;
    }

    public void setDevicePath(String devicePath) {
        this.devicePath = devicePath;
    }

    public Deque<TiDeviceCommand> getCommandDeque() {
        return commandDeque;
    }

    public void setCommandDeque(Deque<TiDeviceCommand> commandDeque) {
        this.commandDeque = commandDeque;
    }

    /**
     * Command number in the cache
     * @return number
     */
    public int getCommandNumber(){
        return this.commandDeque.size();
    }

    /**
     * Add a command into the cache for this device
     * @param command
     */
    public void addCommand(String command) {
        this.commandDeque.offer(new TiDeviceCommand(command));
    }

    /**
     * Fetch a command from the device
     * @return
     */
    public String popCommand() {
        if(this.commandDeque.isEmpty())
            return "";

        return this.commandDeque.remove().getTextCommand();
    }

    /**
     * Get 1st command the device but doesn't remove it
     */
    public String getCommand() {
        if(this.commandDeque.isEmpty())
            return "";

        return this.commandDeque.getFirst().getTextCommand();
    }

    /**
     * if the cache is empty or not
     * @return
     */
    public boolean isEmpty() {
        return this.commandDeque.isEmpty();
    }


    /**
     * remove expired commands - expired time: 30 days
     */
    public void clearExpiredCommand() {

        for(TiDeviceCommand cmd:this.commandDeque) {
               long interval = (System.currentTimeMillis() - cmd.getTimeStamp()) / 1000; //convert to seconds
               if(interval > 30 * 24 * 60) //30 days
               {
                   this.commandDeque.remove(cmd);
               }
        }

    }
}
