package com.pmnm.risk.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import com.dosse.upnp.UPnP;
import com.pmnm.risk.network.message.MessageBuilder;
import com.pmnm.risk.network.message.MessageBuilder.Message;
import com.pmnm.risk.network.message.MessageType;

public class SocServer implements Runnable {

	private static SocServer _this;

	private int serverCapacity;
	private AtomicInteger threadsFinished = new AtomicInteger(0);
	private ServerSocket server;

	private List<Socket> connections;
	private List<ObjectOutputStream> outputs;
	private List<ObjectInputStream> inputs;
	private List<Thread> streamThreads;
	private List<Boolean> isThreadFinished;

	public SocServer(int serverCapacity) {
		this.serverCapacity = serverCapacity;
		connections = new ArrayList<>();
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		streamThreads = new ArrayList<>();
		isThreadFinished = new ArrayList<>();
		_this = this;
	}

	public static void startServer(int serverCapacity) {
		new Thread(_this = new SocServer(serverCapacity)).start();
		System.out.println("Socket server started");
	}

	public static void stopServer() {
		_this.closeCrap();
		System.out.println("server ended");
	}

	@Override
	public void run() {
		UPnP.openPortTCP(27015);
		try (ServerSocket sv = new ServerSocket(27015, serverCapacity)) {
			server = sv;
			connections.add(new Client("HOST", "localhost").getSocket());
			waitForConnection();
			whileChatting();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	private void waitForConnection() throws IOException {
		for (int i = 0; i < serverCapacity; i++) {
			Socket connection = server.accept();
			connections.add(connection);
			setupStreams(connection);
		}
	}

	private void setupStreams(Socket socket) throws IOException {
		outputs.add(new ObjectOutputStream(socket.getOutputStream()));
		inputs.add(new ObjectInputStream(socket.getInputStream()));
	}

	private void whileChatting() {
		broadcast(new MessageBuilder().setSender("Server").setData("All connections have been established!")
				.setType(MessageType.CHAT).build());
		for (int i = 0; i < serverCapacity; i++) {
			final int ii = i;
			streamThreads.add(new Thread(() -> {
				ObjectInputStream inputO = inputs.get(ii);
				while (!isThreadFinished.get(ii)) {
					try {
						Object ob = inputO.readObject();
						if (ob instanceof Message) {
							Message message = (Message) ob;
							if (message.getType() == MessageType.DISCONNECT) {
								isThreadFinished.set(ii, true);
							} else if (message.getType() == MessageType.CHAT) {
								broadcast(message);
							} else if (message.getType() == MessageType.COMPRESSED) {
								broadcast(message);
							}
						}
						Thread.sleep(200);
					} catch (ClassNotFoundException | IOException ex) {
						ex.printStackTrace();
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						ex.printStackTrace();
					}
				}
				threadsFinished.incrementAndGet();
				if (threadsFinished.get() == serverCapacity) {
					synchronized (this) {
						notifyAll();
					}
				}
			}));
			isThreadFinished.add(false);
		}
		streamThreads.forEach(thread -> thread.start());
		try {
			synchronized (this) {
				while (threadsFinished.get() < serverCapacity) {
					wait();
				}
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			ex.printStackTrace();
		}
	}

	private void closeCrap() {
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

	private void broadcast(Message message) {
		outputs.forEach(output -> {
			try {
				output.writeObject(message);
				output.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}

	public static SocServer getInstance() {
		return _this;
	}
}