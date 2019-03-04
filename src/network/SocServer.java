package network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

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

	protected JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output; // output goes away from us
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection; // socket is basically connection between two computers
	private static String userName;

	/**
	 * Constructor
	 */
	public SocServer() {
		super("MESSENGER SERVER SIDE");
		userText = new JTextField();
		userText.setEditable(false); // Without someone connect this need to be false
		// Create Listener

		ActionListener listener = new ActionSendMessage();
		userText.addActionListener(listener);

		// Panel Declaration
		JPanel panel = new JPanel();

		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		chatWindow.setFont(new Font("default", Font.ITALIC, 15));
		panel.add(chatWindow);
		panel.setBackground(Color.GREEN);

		add(panel);
		setSize(400, 500);
		setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		SocServer serverProtocol = new SocServer();

		Scanner input = new Scanner(System.in);
		System.out.println("Please enter your user name");
		userName = input.nextLine();

		serverProtocol.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set Location Of the Server
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		serverProtocol.setLocation(dim.width / 2 - serverProtocol.getSize().width / 2, dim.height / 2 - serverProtocol.getSize().height / 2);

		serverProtocol.setResizable(false);
		serverProtocol.startRunning();

	}

	// *************************************************************************

	// set up and run the server
	public void startRunning() {
		try {
			UPnP.openPortTCP(27015); // open port 27015 on gateway via WaifUPnP
			server = new ServerSocket(27015, 10); // port number is 6789
			// backlog is how many people can access
			while (true) {
				try {
					waitForConnection(); // pretty much every millisecond connection will be called
					// we need to set up output and input stream thus we can connect with each other
					setupStreams();
					whileChatting();
					// connect and have conversation
				} catch (EOFException eofException) { // connection gonna end
					showMessage("\n Server ended the connection! "); // when connection ends
				} finally {
					closeCrap();
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// *************************************************************************

	/**
	 * Close streams and sockets after you have complete the conversation
	 */
	private void closeCrap() {
		// once you done chatting, close everything
		showMessage("\n Closing connections... \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

	}

	// *************************************************************************

	/**
	 * Let the user type stuff into their box
	 * 
	 * @param condition
	 */
	private void ableToType(boolean condition) {
		SwingUtilities.invokeLater(new Runnable() { // only will update chat window portion of gui
			public void run() {
				userText.setEditable(condition);// add a message to end of the document
			}
		});
	}

	// *************************************************************************

	/**
	 * Send a message to Client
	 * 
	 * @param message
	 */

	private void sendMessage(String message) {
		try {
			// one-way road from our computer to their computer
			output.writeObject(userName + " -  " + message);
			output.flush();
			showMessage("\n" + userName + " -" + message); // message history
		} catch (IOException ioException) {
			chatWindow.append("\n ERROR: Message Is not send ");
		}
	}

	// *************************************************************************

	// wait for connection, then display connection info
	private void waitForConnection() throws IOException {
		showMessage("Waiting for someone to connect...\n");
		connection = server.accept(); // do it this over and over again and someone really connected the connection is
		                              // made
		// only will create socket, someone connected
		showMessage("Now connected to " + connection.getInetAddress().getHostName());

	}

	// *************************************************************************

	/**
	 * //get stream to send and receive data
	 * 
	 * @throws IOException
	 */
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());// create a pathway allows us to connect to another computer
		output.flush(); // craps are stays behind(housekeeping)
		input = new ObjectInputStream(connection.getInputStream()); // receive stuff from someone
		showMessage("\n Streams are know set up \n");

	}

	// *************************************************************************

	/**
	 * This class will be working during the chat
	 * 
	 * @throws IOException
	 */
	// during the conversation
	private void whileChatting() throws IOException {
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) input.readObject(); // whatever message is coming into us
				// make sure it is string
				showMessage("\n " + message);
			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\n Invalid message sent by client!");
			}
			// have a conversation
		} while (!message.equals("CLIENT - END"));
	}

	// *************************************************************************

	/**
	 * Description: When the user enter text into serverText this will take the text
	 * and send it to client via sendMessage method
	 * 
	 * @author EgeTuran
	 */
	class ActionSendMessage implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// whatever we type in here this action listener
			// this will pass to send Message
			sendMessage(event.getActionCommand()); // what we typed into this text field
			userText.setText("");
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
		SwingUtilities.invokeLater(new Runnable() { // only will update chat window portion of gui
			public void run() {
				chatWindow.append(text);// add a message to end of the document
			}
		});
	}

	// *************************************************************************

}