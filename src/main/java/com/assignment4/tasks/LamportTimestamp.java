package com.assignment4.tasks;

public class LamportTimestamp {
    private int timestamp;

    public LamportTimestamp(int time){
        timestamp = time;
    }

    public synchronized void tick(){
        // TODO: update the timestamp by 1
        timestamp = timestamp + 1;
    }

    public synchronized int getCurrentTimestamp(){
        return timestamp;
    }

    public synchronized void updateClock(int receivedTimestamp){
        // TODO: update the function to choose the higher value out of the two received timestamps
        timestamp = Math.max(timestamp, receivedTimestamp) + 1;
    }

    // Needed by UdpLTClient
    public synchronized int getValue() {
        return timestamp;
    }

    // Needed by LTClientThread
    public synchronized void setValue(int newValue) {
        this.timestamp = newValue;
    }
}
