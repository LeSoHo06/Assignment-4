package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class VectorClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final VectorClock vcl;
  private final int id;
  private final byte[] receiveData = new byte[1024]; // Buffer for incoming data
  private final List<Message> buffer = new ArrayList<>(); // This buffer can be used for Task 2.2

  public VectorClientThread(DatagramSocket clientSocket, VectorClock vcl, int id) {
    this.clientSocket = clientSocket;
    this.vcl = vcl;
    this.id = id;
  }

  @Override
  public void run() {

  // TODO:
  /*
      Write your code here to continuously listen for incoming messages from the server
      You should first process the received message and then update the vector clock based on the received message (you can use .replaceAll("[\\[\\]]", "").split(",\\s*"); to split a received vector clock into its components)
      Then display the received message and its vector clock
  */
    try {
      while (true) {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
        
        // Expected format: message:timestamp:id or message:[1, 0, 0, 0]:id
        String[] parts = received.split(":");
        if (parts.length < 3) continue;

        String messageBody = parts[0];
        String timestampStr = parts[1]; // This is the vector clock as a string like "[1, 0, 0, 0]"
        int senderId = Integer.parseInt(parts[2]);

        // Parse the vector clock from the string
        String clockStr = timestampStr.replaceAll("[\\[\\]]", "");
        String[] clockValues = clockStr.split(",\\s*");
        VectorClock receivedClock = new VectorClock(4);
        for (int i = 0; i < clockValues.length && i < 4; i++) {
          receivedClock.setVectorClock(i, Integer.parseInt(clockValues[i].trim()));
        }

        // Create a message object
        Message message = new Message(messageBody, receivedClock, senderId);

        // For Task 2.2: Check if message can be accepted or needs to be buffered
        if (vcl.checkAcceptMessage(senderId, receivedClock)) {
          // Message can be delivered
          displayMessage(message);
          
          // Check if any buffered messages can now be delivered
          processBufferedMessages();
        } else {
          // Message needs to be buffered
          buffer.add(message);
          System.out.println("Buffered message from Client " + senderId + ": \"" + messageBody + "\" with timestamp " + timestampStr);
        }
      }
    } catch (IOException e) {
      System.out.println("Receiver thread stopped.");
    }
  }

  private void processBufferedMessages() {
    // Try to deliver buffered messages that can now be accepted
    // Keep processing until no more messages can be delivered
    boolean changed = true;
    while (changed) {
      changed = false;
      List<Message> toRemove = new ArrayList<>();
      for (Message msg : buffer) {
        if (vcl.checkAcceptMessage(msg.getSenderID(), msg.getClock())) {
          displayMessage(msg);
          toRemove.add(msg);
          changed = true;
        }
      }
      buffer.removeAll(toRemove);
    }
  }

// TODO:
/*
    This method should print out the message (e.g. Client 1: Hello World!: [1, 0, 0]) and update
    the vector clock without ticking on receive. Then it should display the the updated vector clock.
    Example: Initial clock [0,0,0], updated clock after message from Client 1: [1, 0, 0]
*/
  private void displayMessage(Message message) {
    if (message == null) return;
    
    // Update the vector clock based on the received message (without ticking)
    vcl.updateClock(message.getClock());
    
    // Display the message
    System.out.println("Client " + message.getSenderID() + ": " + message.getMessage() + ": " + message.getClock().showClock());
    System.out.println("Updated clock: " + vcl.showClock());
  }
}
