/*
 * Classname: Vec2.java
 * Author: 1534674
 * Version: 1.0
 */

package com.nullopt;

import java.awt.*;

public class Vec2 {

	private int X;
	private int Y;
	private final int WIDTH;
	private final int HEIGHT;

	/**
	 * @param x      X coord
	 * @param y      Y coord
	 * @param width  Object width
	 * @param height Object height
	 */
	Vec2(int x, int y, int width, int height) {
		this.X = x;
		this.Y = y;
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	/**
	 * @return Returns X coord
	 */
	public int getX() {
		return this.X;
	}

	/**
	 * @return Returns Y coord
	 */
	public int getY() {
		return this.Y;
	}

	/**
	 * @return Returns width
	 */
	public int getWIDTH() {
		return this.WIDTH;
	}

	/**
	 * @return Returns height
	 */
	public int getHEIGHT() {
		return this.HEIGHT;
	}

	public void setPosition(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	/**
	 * @param velx X Velocity
	 * @param vely Y Velocity
	 */
	public void move(float velx, float vely) {
		this.X += (int) velx;
		this.Y += (int) vely;

		if (this.X < 50) {
			this.X = 50;
		} else if (this.X > 750) {
			this.X = 750;
		}
		if (this.Y < 50) {
			this.Y = 50;
		} else if (this.Y > 500) {
			this.Y = 500;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(this.X, this.Y, this.WIDTH, this.HEIGHT);
	}
}
