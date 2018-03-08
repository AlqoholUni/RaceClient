package com.nullopt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private final JLabel label;
	// to hold the Username and later on the messages
	private final JTextField tf;
	// to hold the server address an the port number
	private final JTextField tfServer;
	private final JTextField tfPort;
	// to Logout and get the list of the users
	private final JButton login;
	private final JButton logout;

	private final Board board;
	// the default port number
	private final int defaultPort;
	private final String defaultHost;
	// if it is for connection
	private boolean connected;
	// the Client object
	private Client client;
	private Boolean playerOneConnected = false;
	private Boolean playerTwoConnected = false;
	private ArrayList<Car> players = new ArrayList<>();

	//private final ArrayList<Boolean> PRESSED_KEYS = new ArrayList<>(Collections.nCopies(8,
	//false));

	private String PRESSED_KEYS = "00000000";

	// Constructor connection receiving a socket number
	private ClientGUI(String host, int port) {

		super("Chat Client");
		this.defaultPort = port;
		this.defaultHost = host;

		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		// the server name and the port number
		JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 3));
		// the two JTextField with default value for server address and port number
		this.tfServer = new JTextField(host);
		this.tfPort = new JTextField("" + port);
		this.tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(this.tfServer);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(this.tfPort);
		serverAndPort.add(new JLabel(""));
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);

		// the Label and the TextField
		this.label = new JLabel("Enter your driver's name below: ", SwingConstants.CENTER);
		northPanel.add(this.label);
		this.tf = new JTextField("Anonymous");
		this.tf.setBackground(Color.WHITE);
		northPanel.add(this.tf);
		this.add(northPanel, BorderLayout.NORTH);

		// The CenterPanel which is the chat room
		this.board = new Board(this.players);
		this.board.addKeyListener(this);
		this.board.setFocusable(true);
		this.board.requestFocus();
		this.board.grabFocus();
		this.add(this.board);

		this.setResizable(false);

		// the 2 buttons
		this.login = new JButton("Connect");
		this.login.addActionListener(this);
		this.logout = new JButton("Disconnect");
		// you have to login before being able to logout
		this.logout.addActionListener(this);
		this.logout.setEnabled(false);

		JPanel southPanel = new JPanel();
		southPanel.add(this.login);
		southPanel.add(this.logout);
		this.add(southPanel, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(875, 700);
		this.setVisible(true);

		this.board.setDoubleBuffered(true);

		Timer TIMER = new Timer(25, this);
		TIMER.start();
	}

	// to start the whole thing the server
	public static void main(String[] args) {
		new ClientGUI("localhost", 1500);
	}

	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		this.login.setEnabled(true);
		this.logout.setEnabled(false);
		this.label.setText("Enter your driver's name below: ");
		this.tf.setText(this.tf.getText());
		// reset port number and host name as a construction time
		this.tfPort.setText("" + this.defaultPort);
		this.tfServer.setText(this.defaultHost);
		// let the user change them
		this.tfServer.setEditable(false);
		this.tfPort.setEditable(false);
		// don't react to a <CR> after the username
		this.tf.removeActionListener(this);
		this.connected = false;
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if (o == this.logout) {
			this.client.sendMessage(new Packet(Packet.LOGOUT, this.tf.getText(), ""));
			return;
		}

		if (this.connected) {
			try {
				this.client.sendMessage(new Packet(Packet.MOVEMENT, this.tf.getText(), this
					.PRESSED_KEYS, true));
				//this.client.sendMessage(new Packet(Packet.MOVEMENT, this.tf.getText(), "Hey"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//System.out.println("Sent: " + this.PRESSED_KEYS);
		}
		this.repaint();

		if (o == this.login) {
			// ok it is a connection request
			String username = this.tf.getText().trim();
			// empty username ignore it
			if (username.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = this.tfServer.getText().trim();
			if (server.length() == 0)
				return;
			// empty or invalid port number, ignore it
			String portNumber = this.tfPort.getText().trim();
			if (portNumber.length() == 0)
				return;
			int port;
			try {
				port = Integer.parseInt(portNumber);
			} catch (Exception en) {
				return;   // nothing I can do if port number is not valid
			}

			// try creating a new Client with GUI
			this.client = new Client(server, port, username, this);
			// test if we can start the Client
			if (this.client.start())
				return;
			this.connected = true;

			this.tf.setEnabled(false);

			// disable login button
			this.login.setEnabled(false);
			// enable the 2 buttons
			this.logout.setEnabled(true);
			// disable the Server and Port JTextField
			this.tfServer.setEditable(false);
			this.tfPort.setEditable(false);
			this.board.requestFocus();
			this.board.grabFocus();
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		char[] keys = this.PRESSED_KEYS.toCharArray();
		switch (e.getKeyCode()) {
			case 38: // up
				keys[0] = '1';
				break;
			case 40: // down
				keys[1] = '1';
				break;
			case 37: // left
				keys[2] = '1';
				break;
			case 39: // right
				keys[3] = '1';
				break;
			case 87:
				keys[4] = '1';
				break;
			case 83:
				keys[5] = '1';
				break;
			case 65:
				keys[6] = '1';
				break;
			case 68:
				keys[7] = '1';
				break;
		}
		this.PRESSED_KEYS = String.valueOf(keys);
	}

	/**
	 * @param e KeyEvent Object
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		char[] keys = this.PRESSED_KEYS.toCharArray();
		switch (e.getKeyCode()) {
			case 38: // up
				keys[0] = '0';
				break;
			case 40: // down
				keys[1] = '0';
				break;
			case 37: // left
				keys[2] = '0';
				break;
			case 39: // right
				keys[3] = '0';
				break;
			case 87:
				keys[4] = '0';
				break;
			case 83:
				keys[5] = '0';
				break;
			case 65:
				keys[6] = '0';
				break;
			case 68:
				keys[7] = '0';
				break;
		}
		this.PRESSED_KEYS = String.valueOf(keys);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public Boolean getPlayerOneConnected() {
		return this.playerOneConnected;
	}

	public void setPlayerOneConnected(Boolean playerOneConnected) {
		this.playerOneConnected = playerOneConnected;
	}

	public Boolean getPlayerTwoConnected() {
		return this.playerTwoConnected;
	}

	public void setPlayerTwoConnected(Boolean playerTwoConnected) {
		this.playerTwoConnected = playerTwoConnected;
	}

	public ArrayList<Car> getPlayers() {
		return this.players;
	}

	public void setPlayers(ArrayList<Car> players) {
		this.players = players;
	}

	public void addPlayers(int i) {
		switch (i) {
			case 0:
				this.players.add(new Car(0, new ImageIcon("Images/90.0car.png"), 90, new Vec2
					(380, 450, 50, 30)));
				break;
			case 1:
				this.players.add(new Car(1, new ImageIcon("Images/90.0rac.png"), 90, new Vec2
					(380, 500, 50, 30)));
				break;
		}
	}
}

