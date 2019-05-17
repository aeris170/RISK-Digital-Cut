package com.pmnm.risk.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class FileClient implements Runnable {

	private Socket s;

//Transients are useless, I put them to make warnings disappear.
	private Socket connection;
	private DataOutputStream output;
	private DataInputStream input;

	private String clientName;
	private String serverIP;

	public FileClient(String clientName, String serverIP) {
		this.clientName = clientName;
		this.serverIP = serverIP;
		new Thread(this).start();
	}

	public static void main(String[] arg) {
		// User specifies the name and IP.
		/*
		 * try (Scanner input = new Scanner(System.in)) {
		 * System.out.println("Enter name: \n"); String clientName = input.nextLine();
		 * System.out.println("Enter IP: \n"); String serverIP = input.nextLine();
		 * 
		 * new FileClient(clientName, serverIP); }
		 */
		new FileClient("ege", "localhost");
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
	 * 
	 * @throws IOException
	 */
	private void connectToServer() throws IOException {

		connection = new Socket(InetAddress.getByName(serverIP), 27016);
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
			try {
				FileOutputStream fos = new FileOutputStream("testfile.gz");
				byte[] buffer = new byte[90000];
				int filesize = 90000; // Send file size in separate msg
				int read = 0;
				int remaining = filesize;
				while ((read = input.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
					remaining -= read;
					System.out.println("read " + read + " bytes.");
					fos.write(buffer, 0, read);
				}
				fos.close();
				Thread.sleep(200);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				ex.printStackTrace();
			}
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
		byte[] buffer = new byte[4096];
		while (fis.read(buffer) > 0) {
			output.write(buffer);
		}
		fis.close();
	}

	public Socket getSocket() {
		return connection;
	}
}