package com.assignment4.tasks;

import java.util.Arrays;

public class VectorClock {

  private final int[] timestamps;

  public VectorClock(int numOfClients) {
    timestamps = new int[numOfClients];
    Arrays.fill(timestamps, 0);
  }

  public synchronized void setVectorClock(int processId, int time) {
    // TODO: Set the vector clock value for the processId
    if (processId >= 0 && processId < timestamps.length) {
      timestamps[processId] = time;
    }
  }

  public synchronized void tick(int processId) {
    // TODO: Increment the vector clock value for the processId
    if (processId >= 0 && processId < timestamps.length) {
      timestamps[processId]++;
    }
  }

  public synchronized int getCurrentTimestamp(int processId) {
    return timestamps[processId];
  }

  public synchronized void updateClock(VectorClock other) {
    // TODO: Update the vector clock based on the values of another vector clock
    // Take the maximum of each component
    for (int i = 0; i < timestamps.length && i < other.timestamps.length; i++) {
      timestamps[i] = Math.max(timestamps[i], other.timestamps[i]);
    }
  }

  public synchronized String showClock() {
    return Arrays.toString(timestamps);
  }

  // TODO:
  // For Task 2.2
  // Check if a message can be delivered or has to be buffered
  public synchronized boolean checkAcceptMessage(int senderId, VectorClock senderClock) {
    // Convert senderId from 1-indexed to 0-indexed
    int senderIndex = senderId - 1;
    
    // Bounds check
    if (senderIndex < 0 || senderIndex >= timestamps.length) {
      return false;
    }
    
    // Check if this is the next expected message from the sender
    // The sender's clock for itself should be exactly one more than our clock for that sender
    if (senderClock.getCurrentTimestamp(senderIndex) != this.getCurrentTimestamp(senderIndex) + 1) {
      return false;
    }
    
    // Check if we've seen all messages that the sender had seen before sending this message
    // For all other processes j != senderId, senderClock[j] <= receiverClock[j]
    for (int i = 0; i < timestamps.length; i++) {
      if (i != senderIndex) {
        if (senderClock.getCurrentTimestamp(i) > this.getCurrentTimestamp(i)) {
          return false;
        }
      }
    }
    
    return true;
  }
}
