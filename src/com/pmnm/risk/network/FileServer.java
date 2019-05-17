package com.pmnm.risk.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileServer extends Thread {

	private int serverCapacity; // Exactly how many people will connect.
	private AtomicInteger threadsFinished = new AtomicInteger(0); // Used for synchronisation.
	private ServerSocket server;
	Socket clientSock;

	private List<Socket> connections; // Socket is basically connection between two computers.
	private List<DataOutputStream> outputs;

	private List<DataInputStream> inputs;
	private List<Thread> streamThreads; // Socket listeners.
	private List<Boolean> isThreadFinished; // Used to stop threads that finished listening.
	public boolean fileTransferProtocol = false;

	public FileServer(int serverCapacity) {
		this.serverCapacity = serverCapacity;
		connections = new ArrayList<>();
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		streamThreads = new ArrayList<>();
		isThreadFinished = new ArrayList<>();
	}

	public void run() {
		try (ServerSocket sv = new ServerSocket(27016, serverCapacity)) {
			server = sv;
			// Auto connect to server.
			// User specifies the name ("HOST")
			System.out.println("egegegegegeegegegegegegegegege");
			// Wait for connections to be made.

			connections.add(new FileClient("HOST", "localhost").getSocket());
			System.out.println("reaches wait con");
			waitForConnection();

			// Loop forever to get chat and MP. Finish when everyone leaves.
			whileChatting();

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	private void waitForConnection() throws IOException {
		// Wait for all connections. Exactly the value of serverCapacity connections
		// must be made.
		for (int i = 0; i < serverCapacity; i++) {
			Socket connection = server.accept();
			connections.add(connection);
			setupStreams(connection);
		}
		System.out.println("Streams are created");
	}

	// *************************************************************************
	private void setupStreams(Socket socket) throws IOException {
		// Set up output and input stream to communicate with others.
		// Output stream for sending.
		// Input stream for receiving.
		outputs.add(new DataOutputStream(socket.getOutputStream()));
		inputs.add(new DataInputStream(socket.getInputStream()));
	}

	// *************************************************************************
	private void whileChatting() {
		// Tell everyone they have connected successfully.
		for (int i = 0; i < serverCapacity; i++) {
			final int ii = i;
			// Create threads that will listen for input streams.
			streamThreads.add(new Thread(() -> {
				DataInputStream input = inputs.get(ii);
				while (!isThreadFinished.get(ii)) {
					try {
						if (fileTransferProtocol) {
							for (DataOutputStream dos : outputs) {
								int count;
								byte[] buffer = new byte[8192]; // or 4096, or more
								while ((count = input.read(buffer)) > 0) {
									dos.write(buffer, 0, count);
								}
							}
						}
						fileTransferProtocol = false;
						Thread.sleep(200);
					} catch (IOException ex) {
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

	public void receiveFileAndDOShit(DataInputStream input) {

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

	public void sendToClient(String file) throws IOException {
		FileInputStream fis = new FileInputStream(file);

		byte[] buffer = new byte[100000];

		outputs.forEach(output -> {

			try {
				while (fis.read(buffer) > 0) {
					output.write(buffer);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		fis.close();
	}
}