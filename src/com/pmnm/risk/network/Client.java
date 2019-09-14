package com.pmnm.risk.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import com.pmnm.risk.network.message.MessageBuilder.Message;
import com.pmnm.risk.network.message.MessageType;

public class Client implements Runnable {

	private static Client _this;
	private transient Socket connection;
	private transient ObjectOutputStream output;
	private transient ObjectInputStream input;

	@SuppressWarnings("unused")
	private String clientName;
	private String serverIP;

	public Client(String clientName, String serverIP) {
		this.clientName = clientName;
		this.serverIP = serverIP;
		new Thread(this).start();
		_this = this;
	}

	public static void main(String[] arg) {
		try (Scanner input = new Scanner(System.in)) {
			System.out.println("Enter name: \n");
			String clientName = input.nextLine();
			System.out.println("Enter IP: \n");
			String serverIP = input.nextLine();
			new Client(clientName, serverIP);
		}
	}

	@Override
	public void run() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
			closeCrap();
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private void connectToServer() throws IOException {
		connection = new Socket(InetAddress.getByName(serverIP), 9999);
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		input = new ObjectInputStream(connection.getInputStream());
	}

	private void whileChatting() throws IOException, ClassNotFoundException {
		while (true) {
			Message message = (Message) input.readObject();
			if (message.getType() == MessageType.CHAT) {
				System.out.println(message.getData());
			} else if (message.getType() == MessageType.COMPRESSED) {
				// GameInstance.deserializeGame((GameInstance) message.getData());
			}
		}
	}

	private void closeCrap() throws IOException {
		output.close();
		input.close();
		connection.close();
	}

	public void sendToServer(Message message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Socket getSocket() {
		return connection;
	}

	public static Client getInstance() {
		return _this;
	}

}