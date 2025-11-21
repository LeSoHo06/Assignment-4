package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpLTClient {

  public static void main(String[] args) throws Exception {
    // Prompt the user to enter their ID
    System.out.println("Enter your id (1 to 4): ");
    Scanner idInput = new Scanner(System.in);
    int id = idInput.nextInt(); // Read the user's ID
    int port = 4040; // Server's port number

    // Prepare the client socket for communication
    DatagramSocket clientSocket = new DatagramSocket();
    InetAddress ipAddress = InetAddress.getByName("localhost"); // Server's IP address

    // Initialize the buffers for sending data
    byte[] sendData;
    int startTime = 0;

    // Initialize Lamport Clock with a starting timestamp
    LamportTimestamp lc = new LamportTimestamp(startTime);

    // Start a separate thread to continuously listen for incoming messages from the server
    LTClientThread client = new LTClientThread(clientSocket, lc);
    Thread receiverThread = new Thread(client);
    receiverThread.start();

    // TODO: This should not be counted as a message event, so the clock should not tick
    String joinMessage = "message:timestamp:id";

    // TODO: Send an initial "join" message to notify the other clients that a new one has connected
    joinMessage = "join:" + lc.getValue() + ":" + id;
    sendData = joinMessage.getBytes();

    //TODO: Send the packet to the server
    DatagramPacket joinPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
    clientSocket.send(joinPacket);

    // Prompt the user to enter messages
    System.out.println("[Client " + id + "] Enter any message:");
    Scanner input = new Scanner(System.in);

    while (true) {
      try {
        // Read user input from the console
        String messageBody = input.nextLine();

        // If the user types "quit", close the socket and exit the program
        if (messageBody.equalsIgnoreCase("quit")) {
          clientSocket.close(); // Close the client socket
          receiverThread.interrupt(); // Interrupt the receiver thread to stop listening
          System.exit(0); // Exit the program
        }

        if (!messageBody.isEmpty()) {

          //TODO: Increment the Lamport clock for the message event
          lc.tick();

          //TODO: Get the updated timestamp and prepare the message to send
          int timestamp = lc.getValue();
          String responseMessage = messageBody + ":" + timestamp + ":" + id;
          sendData = responseMessage.getBytes();

          // TODO: Send the message to the server
          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
          clientSocket.send(sendPacket);

          // Print the sent message along with its timestamp
          System.out.println("Sent message: \"" + messageBody + "\" with timestamp: " + timestamp);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
