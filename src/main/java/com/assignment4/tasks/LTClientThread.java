package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// This Class handles the continuous listening for incoming messages from the server
public class LTClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final LamportTimestamp lc;
  byte[] receiveData = new byte[1024];

  public LTClientThread(DatagramSocket clientSocket, LamportTimestamp lc) {
    this.clientSocket = clientSocket;
    this.lc = lc;
  }

  @Override
  public void run() {
    // TODO:
    // Write your code here to continuously listen for incoming messages from the server and display them.
    // Make use of the Datagram sockets and functions in Java https://docs.oracle.com/javase/8/docs/api/java/net/DatagramSocket.html

    try {
      while (true) {
        DatagramPacket receivePacket =
                new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String received = new String(receivePacket.getData(), 0,
                receivePacket.getLength());

        // Expected format: message:timestamp:id
        String[] parts = received.split(":");
        if (parts.length < 3) continue;

        String messageBody = parts[0];
        int receivedClock = Integer.parseInt(parts[1]);
        int senderId = Integer.parseInt(parts[2]);

        // TODO:
        // Update the clock based on the timestamp received from the server.

        int localClock = lc.getValue();
        int newClock = Math.max(localClock, receivedClock) + 1;
        lc.setValue(newClock);

        System.out.println("[RECEIVED] From Client " + senderId +
                ": \"" + messageBody + "\" | timestamp " + receivedClock +
                " → local clock updated to " + newClock);
      }

    } catch (IOException e) {
      System.out.println("Receiver thread stopped.");
    }
  }
}
