package network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.dosse.upnp.UPnP;

/**
 * @author EgeTuran
 */
public class SocServer extends JFrame {

	private static final long serialVersionUID = 5565232993673926023L;

	private int capacity;
	private AtomicInteger threadsFinished = new AtomicInteger(0);

	protected JTextField userText;
	private JTextArea chatWindow;
	private ServerSocket server;
	private static String userName;

	// to create more stream
	private List<Socket> connections; // socket is basically connection between two computers
	private List<ObjectOutputStream> outputs; // output goes away from us
	private List<ObjectInputStream> inputs;
	private List<Thread> streamThreads;
	private List<Boolean> isThreadFinished;

	/**
	 * Constructor
	 */
	public SocServer(int capacity) {
		super("MESSENGER SERVER SIDE");
		this.capacity = capacity;
		connections = new ArrayList<>();
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		streamThreads = new ArrayList<>();
		isThreadFinished = new ArrayList<>();
		userText = new JTextField();
		userText.setEditable(false); // Without someone connect this need to be false

		userText.addActionListener(e -> {
			// whatever we type in here this action listener
			// this will pass to send Message
			sendMessage(e.getActionCommand()); // what we typed into this text field
			userText.setText("");
		});

		JPanel panel = new JPanel();

		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		chatWindow.setFont(new Font("default", Font.ITALIC, 15));
		panel.add(chatWindow);
		panel.setBackground(Color.GREEN);

		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		SocServer serverProtocol = new SocServer(2);
		userName = "HOST";
		new Thread(() -> serverProtocol.startRunning()).start();
	}

	// *************************************************************************
	// set up and run the server
	public void startRunning() {
		try {
			UPnP.openPortTCP(27015); // open port 27015 on gateway via WaifUPnP
			server = new ServerSocket(27015, capacity); // port number is 6789
			// backlog is how many people can access
			try {
				waitForConnection(); // pretty much every millisecond connection will be called
				// we need to set up output and input stream thus we can connect with each other
				whileChatting();
				// connect and have conversation
			} catch (EOFException ex) { // connection gonna end
				showMessage("Server ended the connection!"); // when connection ends
				ex.printStackTrace();
			} finally {
				closeCrap();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// *************************************************************************
	/**
	 * Close streams and sockets after you have complete the conversation
	 */
	private void closeCrap() {
		// once you done chatting, close everything
		showMessage("Closing connections...");
		ableToType(false);
		outputs.forEach(output -> {
			try {
				output.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		inputs.forEach(input -> {
			try {
				input.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		connections.forEach(connection -> {
			try {
				connection.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}

	// *************************************************************************
	/**
	 * Let the user type stuff into their box
	 * 
	 * @param condition
	 */
	private void ableToType(boolean condition) {
		// only will update chat window portion of gui
		// add a message to end of the document
		SwingUtilities.invokeLater(() -> userText.setEditable(condition));
	}

	// *************************************************************************
	/**
	 * Send a message to Client
	 * 
	 * @param message
	 */
	private void sendMessage(String message) {
		// one-way road from our computer to their computer
		outputs.forEach(output -> {
			try {
				output.writeObject(userName + " -  " + message);
				output.flush();
			} catch (IOException ex) {
				chatWindow.append("ERROR: Message Is not send");
				ex.printStackTrace();
			}
		});
		// message history
		showMessage("\n" + userName + " -" + message);
	}

	// *************************************************************************
	// wait for connection, then display connection info
	private void waitForConnection() throws IOException {
		showMessage("Waiting for someone to connect...");
		// do it this over and over again and someone really connected the connection is
		// made
		for (int i = 0; i < capacity; i++) {
			Socket connection = server.accept();
			connections.add(connection);
			setupStreams(connection);
			showMessage("Now connected to " + connection.getInetAddress().getHostName());
		}
	}

	// *************************************************************************
	/**
	 * //get stream to send and receive data
	 * 
	 * @throws IOException
	 */
	private void setupStreams(Socket socket) {
		// create a pathway allows us to connect to another computer
		try {
			outputs.add(new ObjectOutputStream(socket.getOutputStream()));
			inputs.add(new ObjectInputStream(socket.getInputStream())); // receive stuff from someone
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		showMessage("Streams are now set up");
	}

	// *************************************************************************
	/**
	 * This class will be working during the chat
	 * 
	 * @throws IOException
	 */
	// during the conversation
	private void whileChatting() {
		sendMessage("You are now connected!");
		ableToType(true);
		for (int i = 0; i < capacity; i++) {
			final int ii = i;
			streamThreads.add(new Thread(() -> {
				ObjectInputStream input = inputs.get(ii);
				while (isThreadFinished.get(ii)) {
					try {
						System.out.println(Thread.currentThread().getId() + " is waiting for input");
						// make sure it is string
						Object message = input.readObject();
						if (message instanceof String) {
							if (message.equals("DISCONNECT")) {
								isThreadFinished.set(ii, false);
							}
							showMessage((String) message);
							sendMessage((String) message);
						} else {
							// multiplayer code
							// send game data here
							showMessage("wtf this is not string");
						}
						System.out.println(message.toString());
					} catch (ClassNotFoundException | IOException ex) {
						showMessage("Invalid message sent by client!");
						ex.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						ex.printStackTrace();
					}
				}
				threadsFinished.incrementAndGet();
			}));
			isThreadFinished.add(true);
		}
		streamThreads.forEach(thread -> thread.start());
		try {
			synchronized (this) {
				while (threadsFinished.get() < capacity) {
					wait();
				}
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
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
		// you want a change GUI this update the part or text inside the gui
		// only will update chat window portion of gui
		// add a message to end of the document
		SwingUtilities.invokeLater(() -> chatWindow.append(text + "\n"));
	}
}