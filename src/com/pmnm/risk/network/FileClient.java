package com.pmnm.risk.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class FileClient implements Runnable{
 
 private Socket s;
 
//Transients are useless, I put them to make warnings disappear.
 private  Socket connection;
 private  DataOutputStream output;
 private  DataInputStream input;
 
  private String clientName;
  private String serverIP;
 
 public FileClient(String clientName, String serverIP) {
 this.clientName = clientName;
 this.serverIP = serverIP;
 
 new Thread(this).start();
 }
 
 public static void main(String[] arg) {
  // User specifies the name and IP.
 /* try (Scanner input = new Scanner(System.in)) {
   System.out.println("Enter name: \n");
   String clientName = input.nextLine();
   System.out.println("Enter IP: \n");
   String serverIP = input.nextLine();
   
   new FileClient(clientName, serverIP);
  }*/
   new FileClient("ege" , "localhost");
 }
 
 public void sendFile(String file) throws IOException {
  DataOutputStream dos = new DataOutputStream(s.getOutputStream());
  FileInputStream fis = new FileInputStream(file);
  byte[] buffer = new byte[90000];
  
  while (fis.read(buffer) > 0) {
   dos.write(buffer);
  }
  
  fis.close();
  dos.close(); 
 }
 
//*************************************************************************
 // connect to server
 @Override
 public void run() {
  try {
   connectToServer();
   setupStreams();
   sendToServer("clientFiles\\currentGame.gz");
   whileChatting();
   closeCrap();
  } catch (IOException | ClassNotFoundException ex) {
   ex.printStackTrace();
  }
 }
 
 // *************************************************************************
  /**
   * Connect to server
   * @throws IOException
   */
  private void connectToServer() throws IOException {
   
   connection = new Socket(InetAddress.getByName(serverIP), 27015);
  }
 
  // *************************************************************************
  /**
   * Setup streams to send and receive messages
   * 
   * @throws IOException
   */
  private void setupStreams() throws IOException {
   output = new DataOutputStream(connection.getOutputStream());
   input = new DataInputStream(connection.getInputStream());
  }
  
  // *************************************************************************
  /**
   * While chatting with server.
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void whileChatting() throws IOException, ClassNotFoundException {
  
   while (true) {
     
   }
   
  }
  
  
  // *************************************************************************
  /**
   * Close the streams and sockets
   * 
   * @throws IOException
   */
  private void closeCrap() throws IOException {
   output.close();
   input.close();
   connection.close();
  }
  
  public void sendToServer(String file) throws IOException {
   FileInputStream fis = new FileInputStream(file);
   
   byte[] buffer = new byte[90000];
   
   while (fis.read(buffer) > 0) {
    output.write(buffer);
   }
   
   fis.close();
   output.close(); 
  }
  
  
 

}