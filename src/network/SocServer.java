package network;

import java.awt.Color;
import java.awt.Font;
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

import com.dosse.upnp.UPnP;

import network.message.MessageBuilder;
import network.message.MessageBuilder.Message;
import network.message.MessageType;

/**
 * @author Ege Turan, Doða Oruç
 */
public class SocServer extends JFrame {

	private static final long serialVersionUID = 5565232993673926023L;

	private int serverCapacity; // Exactly how many people will connect.
	private AtomicInteger threadsFinished = new AtomicInteger(0); // Used for synchronisation.

	private JTextArea chatWindow;
	private transient ServerSocket server;

	// To create more streams, we use lists.
	// Transients are useless, I put them to make warnings disappear.
	private transient List<Socket> connections; // Socket is basically connection between two computers.
	private transient List<ObjectOutputStream> outputs;
	private transient List<ObjectInputStream> inputs;
	private transient List<Thread> streamThreads; // Socket listeners.
	private List<Boolean> isThreadFinished; // Used to stop threads that finished listening.

	public SocServer(int serverCapacity) {
		super("SERVER");
		this.serverCapacity = serverCapacity;
		connections = new ArrayList<>();
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		streamThreads = new ArrayList<>();
		isThreadFinished = new ArrayList<>();

		JPanel panel = new JPanel();
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
		// User will specify the server capacity.
		SocServer serverProtocol = new SocServer(3);
		new Thread(() -> serverProtocol.startRunning()).start();
	}

	// *************************************************************************
	public void startRunning() {
		// Open port 27015 on gateway via WaifUPnP
		UPnP.openPortTCP(27015);
		try (ServerSocket sv = new ServerSocket(27015, serverCapacity)) {
			server = sv;
			// Auto connect to server.
			// User specifies the name ("HOST")
			connections.add(new Client("HOST", "localhost").getSocket());
			// Wait for connections to be made.
			waitForConnection();
			// Loop forever to get chat and MP. Finish when everyone leaves.
			whileChatting();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	// *************************************************************************
	private void waitForConnection() throws IOException {
		// Wait for all connections. Exactly the value of serverCapacity connections
		// must be made.
		for (int i = 1; i < serverCapacity; i++) {
			Socket connection = server.accept();
			connections.add(connection);
			setupStreams(connection);
		}

	}

	// *************************************************************************
	private void setupStreams(Socket socket) throws IOException {
		// Set up output and input stream to communicate with others.
		// Output stream for sending.
		// Input stream for receiving.
		outputs.add(new ObjectOutputStream(socket.getOutputStream()));
		inputs.add(new ObjectInputStream(socket.getInputStream()));
	}

	// *************************************************************************
	private void whileChatting() {
		// Tell everyone they have connected successfully.
		broadcast(new MessageBuilder().setSender("Server").setData("All connections have been established!").setType(MessageType.CHAT).build());
		for (int i = 0; i < serverCapacity; i++) {
			final int ii = i;
			// Create threads that will listen for input streams.
			streamThreads.add(new Thread(() -> {
				ObjectInputStream input = inputs.get(ii);
				while (!isThreadFinished.get(ii)) {
					try {
						// Read message.
						Message message = (Message) input.readObject();
						if (message.getType() == MessageType.DISCONNECT) {
							// If it is a disconnect message, shut the thread down.
							isThreadFinished.set(ii, true);
						} else if (message.getType() == MessageType.CHAT) {
							// Else if it is a chat message, broadcast it to all clients.
							broadcast(message);
						} else if (message.getType() == MessageType.GAME_MOVE) {
							// Else if it is a game move, @EGE @CAGRI you do this part!
							// TODO IMPLEMENT MULTIPLAYER
						}
						// Wait for 200 milliseconds before listening.
						Thread.sleep(200);
					} catch (ClassNotFoundException | IOException ex) {
						ex.printStackTrace();
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						ex.printStackTrace();
					}
				}
				// Increment the amount of threads finished. If threadFinished ==
				// serverCapacity, everyone left the game.
				threadsFinished.incrementAndGet();
				if (threadsFinished.get() == serverCapacity) {
					synchronized (this) {
						notifyAll();
					}
				}
			}));
			// Initialise threadFinished list to true. If a thread's finished value is false
			// it will loop forever, else it will shut down, stopping listening for more
			// input.
			isThreadFinished.add(false);
		}
		// Start all threads.
		streamThreads.forEach(thread -> thread.start());
		try {
			synchronized (this) {
				while (threadsFinished.get() < serverCapacity) {
					// Wait until all threads exited.
					wait();
				}
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	// *************************************************************************
	private void closeCrap() {
		// Close streams and sockets before closing the program.
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
	private void broadcast(Message message) {
		// Write the message to everyone.
		outputs.forEach(output -> {
			try {
				output.writeObject(message);
				output.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}
}