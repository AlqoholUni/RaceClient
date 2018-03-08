package com.nullopt;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * This class defines the different TYPE of messages that will be exchanged between the
 * Clients and the Server.
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class Packet implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	// The different types of MESSAGE sent by the Client
	// KEY_PRESSED to receive the list of the users connected
	// KEY_RELEASED an ordinary MESSAGE
	// LOGOUT to disconnect from the Server
	static final int MOVEMENT = 0, COLLISION_EVENT = 1, LOGOUT = 2, HEARTBEAT = 3, NEW_CONNECTION
		= 4;
	private final int TYPE;
	private String MESSAGE;
	private final String USERNAME;
	//private final ArrayList<Boolean> KEYS_HELD;
	private String KEYS_HELD;

	Packet(int type, String username, String keysHeld, Boolean test) {
		this.TYPE = type;
		this.USERNAME = username;
		this.KEYS_HELD = keysHeld;
	}

	// constructor
	Packet(int type, String username, String message) {
		this.TYPE = type;
		this.USERNAME = username;
		this.MESSAGE = message;
	}

	// getters
	int getType() {
		return this.TYPE;
	}

	String getUsername() {
		return this.USERNAME;
	}

	String getMessage() {
		return this.MESSAGE;
	}

	String getKeysHeld() {
		return this.KEYS_HELD;
	}
}

