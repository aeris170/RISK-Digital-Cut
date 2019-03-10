package com.pmnm.risk.network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.pmnm.risk.network.message.MessageBuilder;
import com.pmnm.risk.network.message.MessageBuilder.Message;
import com.pmnm.risk.network.message.MessageType;

/**
 * Description:Chat Application
 * 
 * @author Ege Turan, Doða Oruç
 */

public class Client extends JFrame implements Runnable {

	private static final long serialVersionUID = 4580330924087695464L;

	// Text Data Entrance Part
	private JTextField clientText;
	private JTextArea chatWindows;

	// Data in and data out
	// Transients are useless, I put them to make warnings disappear.
	private transient Socket connection;
	private transient ObjectOutputStream output;
	private transient ObjectInputStream input;

	// Message and IP
	private String clientName;
	private String serverIP;

	// Socket For Connection
	/**
	 * Constructor of Client class
	 * 
	 * @param clientName Name of the client
	 * @param serverIP IP of the server
	 */
	public Client(String clientName, String serverIP) {
		super("Client");
		this.clientName = clientName;
		this.serverIP = serverIP;

		// Arrange Client Text Field
		clientText = new JTextField();
		clientText.setEditable(false);

		// Add ActionListener For Text Field
		clientText.addActionListener(e -> {
			sendToServer(new MessageBuilder().setSender(clientName).setData(clientText.getText()).setType(MessageType.CHAT).build());
			clientText.setText("");
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// Tell the server, you are disconnecting.
				sendToServer(new MessageBuilder().setSender(clientName).setType(MessageType.DISCONNECT).build());
				System.exit(0);
			}
		});

		setBackground(Color.GREEN);
		add(clientText, BorderLayout.NORTH);
		chatWindows = new JTextArea();
		add(new JScrollPane(chatWindows), BorderLayout.CENTER);
		setSize(300, 450);
		setVisible(true);
		new Thread(this).start();
	}

	// *************************************************************************
	public static void main(String[] arg) {
		// User specifies the name and IP.
		try (Scanner input = new Scanner(System.in)) {
			System.out.println("Enter name: \n");
			String clientName = input.nextLine();
			System.out.println("Enter IP: \n");
			String serverIP = input.nextLine();
			new Client(clientName, serverIP);
		}
	}

	// *************************************************************************
	// connect to server
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

	// *************************************************************************
	/**
	 * Connect to server
	 * 
	 * @throws IOException
	 */
	private void connectToServer() throws IOException {
		showMessage("Establishing connection...");
		connection = new Socket(InetAddress.getByName(serverIP), 27015);
	}

	// *************************************************************************
	/**
	 * Setup streams to send and receive messages
	 * 
	 * @throws IOException
	 */
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("Connected to: " + connection.getInetAddress().getHostName());
	}

	// *************************************************************************
	/**
	 * While chatting with server.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void whileChatting() throws IOException, ClassNotFoundException {
		ableToType(true);
		while (true) {
			Message message = (Message) input.readObject();
			if (message.getType() == MessageType.CHAT) {
				showMessage(message.getSender() + ": " + message.getData());
			} else if (message.getType() == MessageType.GAME_MOVE) {
				// TODO MP
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
		ableToType(false);
		output.close();
		input.close();
		connection.close();
	}

	// *************************************************************************
	public void sendToServer(Message message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// *************************************************************************
	/**
	 * Description: Updates chatWindow
	 * 
	 * @param text
	 */
	private void showMessage(final String text) {
		// Update the UI asynchronously.
		SwingUtilities.invokeLater(() -> chatWindows.append(text + "\n"));
	}

	// *************************************************************************
	/**
	 * Let the user type stuff into their box
	 * 
	 * @param condition
	 */
	private void ableToType(boolean condition) {
		// Update the UI asynchronously.
		SwingUtilities.invokeLater(() -> clientText.setEditable(condition));
	}

	public Socket getSocket() {
		return connection;
	}
}