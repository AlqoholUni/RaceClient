package com.nullopt;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {

	private final ArrayList<Car> players;

	Board(ArrayList<Car> players) {
		this.players = players;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.createMap(g);
		this.createPlayers(g);
	}

	private void createPlayers(Graphics g) {
		//System.out.println(players.size());
		this.players.forEach((x) -> g.drawImage(x.getImage(), x.getPosition().getX(), x
			.getPosition().getY(), null));
	}

	private void createMap(Graphics g) {
		Color c1 = Color.green;
		g.setColor(c1);
		g.fillRect(150, 150, 550, 300); // grass
		Color c2 = Color.black;
		g.setColor(c2);
		g.drawRect(50, 50, 750, 500);  // outer edge
		g.drawRect(150, 150, 550, 300); // inner edge
		Color c3 = Color.yellow;
		g.setColor(c3);
		g.drawRect(100, 100, 650, 400); // mid - lane marker
		Color c4 = Color.white;
		g.setColor(c4);
		g.drawLine(425, 450, 425, 550); // start line
	}
}
