package serverConnector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Description:Chat Application
 * @author EgeTuran
 */

public class Client extends JFrame{
	
	//Text Data Entrance Part
	private JTextField clientText;
	private JTextArea chatWindows;
	
	//Data in and data out
	private ObjectOutputStream output; //output always from this computer to other one
	private ObjectInputStream input; //input is always taken whatever computer is it
	
	//Message and IP
	private String message  = "";
	private String serversIP;
	
	//Socket For Connection
	private Socket connection;
	
	/**
	 * Constructor of Client class
	 * @param host takes IP 
	 */
	public Client(String host){
		
		super("Client Message Box");
		serversIP = host;
		
		//Arrange Client Text Field
		clientText = new JTextField();
		clientText.setEditable(false);
		
		//Add ActionListener For Text Field
		ActionListener listener = new ActionClient();
		clientText.addActionListener(listener);
		
		add(clientText, BorderLayout.NORTH);
		chatWindows = new JTextArea();
		add(new JScrollPane(chatWindows), BorderLayout.CENTER);
		setSize(300, 450);
		setVisible(true);
	}
	
	//*************************************************************************
	public static void main(String[] arg){
	//Scanner Declaration
	Scanner input = new Scanner(System.in);
	//Want the IP or press localHost 
	System.out.println("Please enter the IP of the serve or write localHost");
	String IP  = input.nextLine();
	
	//Object Declaration To run the Client
	Client clientProtocol = new Client(IP);
	clientProtocol.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	clientProtocol.setBackground(Color.GREEN);
	clientProtocol.startRunnning();
	}
	
	//*************************************************************************
	
	//connect to server
	public void startRunnning(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofExcception){
			showMessage("\n Client termianted the connection");
			}catch(IOException ioException){
				ioException.printStackTrace();
			}finally{
				closeCrap();
			}
	}
	
	
	
	//*************************************************************************
	/**
	 * While chatting with server
	 * @throws IOException 
	 */
	private void whileChatting() throws IOException {
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n I don't know that object type");
			}
		}while(!message.equals("SERVER - END"));
		
	}
	
	//*************************************************************************
	
	/**
	 * Let the user type stuff into their box
	 * @param condition
	 */
	private void ableToType(boolean condition) {
		SwingUtilities.invokeLater( 
				new Runnable(){ //only will update chat window portion of gui
					public void run(){
						clientText.setEditable(condition);//add a message to end of the document
					}
				}
			);
	}

	//*************************************************************************
	
	/**
	 * Setup streams to send and receive messages
	 * @throws IOException 
	 */
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n streams are now good\n");	
	}

	//*************************************************************************
	/**
	 * Connect to server
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private void connectToServer() throws UnknownHostException, IOException {
		showMessage("Attempting connection..\n");
		//???????
		connection = new Socket(InetAddress.getByName(serversIP), 9876);
		showMessage("Connected to: " + connection.getInetAddress().getHostName());
	
	}
	//*************************************************************************
	
	/**
	 * Close the streams and sockets
	 */
	private void closeCrap() {
		showMessage("\n closing crap down...");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//*************************************************************************
	private void sendMessage(String message){
		try{
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nCLIENT -" + message);
			
		}catch(IOException ioException){
			chatWindows.append("\nsomething not working");
		}
	}
	
	//*************************************************************************
	
	/** Description: Updates chatWindow
	 * @param text
	 */
	private void showMessage(final String text){
		 //you want a change GUI this update the part or text inside the gui
		SwingUtilities.invokeLater( 
		new Runnable(){ //only will update chat window portion of gui
			public void run(){
				chatWindows.append(text);//add a message to end of the document
			}
		}
	);
}
	//*************************************************************************
	
	class ActionClient implements ActionListener{
		public void actionPerformed(ActionEvent event){
			sendMessage(event.getActionCommand());
			clientText.setText("");
		}
	}
}
