package com.nullopt;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client {

	// if I use a GUI or not
	private final ClientGUI cg;
	// the server, the port and the username
	private final String server;
	private final String username;
	private final int port;
	// for I/O
	private ObjectInputStream sInput;        // to read from the socket
	private ObjectOutputStream sOutput;        // to write on the socket
	private Socket socket;

	/*
	 *  Constructor called by console mode
	 *  server: the server address
	 *  port: the port number
	 *  username: the username
	 */
	private Client(String server, int port, String username) {
		// which calls the common constructor with the GUI set to null
		this(server, port, username, null);
	}

	/*
	 * Constructor call when used from a GUI
	 * in console mode the ClientGUI parameter is null
	 */
	Client(String server, int port, String username, ClientGUI cg) {
		this.server = server;
		this.port = port;
		this.username = username;
		// save if we are in GUI mode or not
		this.cg = cg;
	}

	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			this.socket = new Socket(this.server, this.port);
		}
		// if it failed not much I can so
		catch (Exception ec) {
			this.display("Error connectiong to server:" + ec);
			return true;
		}

		String msg = "Connection accepted " + this.socket.getInetAddress() + ":" + this.socket.getPort();
		this.display(msg);

		/* Creating both Data Stream */
		try {
			this.sInput = new ObjectInputStream(this.socket.getInputStream());
			this.sOutput = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException eIO) {
			this.display("Exception creating new Input/output Streams: " + eIO);
			return true;
		}

		// creates the Thread to listen from the server
		new ListenFromServer().start();
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try {
			this.sOutput.writeObject(this.username);
		} catch (IOException eIO) {
			this.display("Exception doing login : " + eIO);
			this.disconnect();
			return true;
		}
		// success we inform the caller that it worked
		return false;
	}

	/*
	 * To send a message to the console or the GUI
	 */
	private void display(String msg) {
		if (this.cg == null)
			System.out.println(msg);      // println in console mode
		//else
		//this.cg.append(msg + "\n");        // append to the ClientGUI JTextArea (or whatever)
	}

	/*
	 * To send a message to the server
	 */
	void sendMessage(Packet packet) {
		try {
			this.sOutput.writeObject(packet);
		} catch (IOException e) {
			this.display("Exception writing to server: " + e);
		}
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	private void disconnect() {
		try {
			if (this.sInput != null) this.sInput.close();
		} catch (Exception ignored) {
		} // not much else I can do
		try {
			if (this.sOutput != null) this.sOutput.close();
		} catch (Exception ignored) {
		} // not much else I can do
		try {
			if (this.socket != null) this.socket.close();
		} catch (Exception ignored) {
		} // not much else I can do

		// inform the GUI
		if (this.cg != null)
			this.cg.connectionFailed();

	}

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a GUI or simply System.out.println() it in console mode
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while (true) {
				try {
					Packet data = (Packet) Client.this.sInput.readObject();

					switch (data.getType()) {
						case Packet.MOVEMENT:
							//System.out.println("Received: " + data.getKeysHeld());
							break;
						case Packet.LOGOUT:
							break;
						case Packet.COLLISION_EVENT:
							break;
						case Packet.HEARTBEAT:
							break;
						case Packet.NEW_CONNECTION:
							System.out.println(data.getMessage());
							if (Objects.equals(data.getMessage(), "Red"))
								Client.this.cg.addPlayers(0);
							else if (Objects.equals(data.getMessage(), "Blue"))
								Client.this.cg.addPlayers(1);
							break;
					}
				} catch (IOException e) {
					Client.this.display("Server has close the connection: " + e);
					if (Client.this.cg != null)
						Client.this.cg.connectionFailed();
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch (ClassNotFoundException ignored) {
				}
			}
		}
	}
}

