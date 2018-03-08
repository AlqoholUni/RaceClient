/*
 * Classname: Car.java
 * Author: 1534674
 * Version: 1.0
 */

package com.nullopt;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Car {

	private final int ID;

	private ImageIcon sprite;
	private float rotation;
	private float velocity;
	private final Vec2 position;

	Car(int id, ImageIcon sprite, float rotation, Vec2 position) {
		this.ID = id;
		this.sprite = sprite;
		this.rotation = rotation;
		this.position = position;
	}

	public float accelerate() {
		this.velocity += 0.2f;
		if (this.velocity > 5f) {
			this.velocity = 5f;
		}
		return this.velocity;
	}

	public float brake() {
		this.velocity -= 0.1f;
		if (this.velocity < 0.0f) {
			this.velocity = 0.0f;
		}
		return this.velocity;
	}

	public Image getImage() {
		return this.sprite.getImage();
	}

	private void setImage(ImageIcon sprite) {
		this.sprite = sprite;
	}

	private float getRotation() {
		return this.rotation;
	}

	private void setRotation(float rot) {
		this.rotation = rot;
	}

	public Vec2 getPosition() {
		return this.position;
	}

	/**
	 * Moves the car
	 * @param pressedKeys Currently held keys
	 */
	public void move(ArrayList<Boolean> pressedKeys) {
		if (!pressedKeys.get(0) && !pressedKeys.get(1) && this.ID == 0) {
			float angle = this.getRotation();
			this.forward(angle, false);
		}

		if (pressedKeys.get(0) && this.ID == 0) {
			float angle = this.getRotation();
			this.forward(angle, true);
		}

		if (pressedKeys.get(1) && this.ID == 0) {
			float angle = this.getRotation();
			this.backwards(angle, true);
		}

		if (pressedKeys.get(2) && this.ID == 0) {
			this.rotateAntiClockwise(0);
		}

		if (pressedKeys.get(3) && this.ID == 0) {
			this.rotateClockwise(0);
		}

		if (!pressedKeys.get(4) && this.ID == 1) {
			float angle = this.getRotation();
			this.forward(angle, false);
		}

		if (pressedKeys.get(4) && this.ID == 1) {
			float angle = this.getRotation();
			this.forward(angle, true);
		}

		if (pressedKeys.get(5) && this.ID == 1) {
			float angle = this.getRotation();
			this.backwards(angle, true);
		}

		if (pressedKeys.get(6) && this.ID == 1) {
			this.rotateAntiClockwise(1);
		}

		if (pressedKeys.get(7) && this.ID == 1) {
			this.rotateClockwise(1);
		}
	}

	private void rotateClockwise(int i) {
		this.setRotation(this.getRotation() + 22.5f);
		if (this.getRotation() == 360f) {
			this.setRotation(0f);
		}
		if (this.getRotation() > 360f) {
			this.setRotation(22.5f);
		}
		this.changeImage(i);
	}

	private void rotateAntiClockwise(int i) {
		this.setRotation(this.getRotation() - 22.5f);
		if (this.getRotation() < 0f) {
			this.setRotation(360f - 22.5f);
		}
		this.changeImage(i);
	}

	/**
	 * @param angle Angle to extend on.
	 */
	private void backwards(float angle, boolean accel) {
		float yBias;
		float xBias;
		float vel = accel ? this.accelerate() : this.brake();
		if (angle >= 0 && angle <= 90) {
			yBias = 1 - (angle / 90);
			xBias = 1 - yBias;
			this.getPosition().move((xBias * vel) * -1, yBias * vel);
		} else if (angle >= 91 && angle <= 180) {
			xBias = 1 - ((angle - 90) / 90);
			yBias = 1 - xBias;
			this.getPosition().move((xBias * vel) * -1, (yBias * vel) * -1);
		} else if (angle >= 181 && angle <= 270) {
			yBias = 1 - ((angle - 180) / 90);
			xBias = 1 - yBias;
			this.getPosition().move(xBias * vel, (yBias * vel) * -1);
		} else if (angle >= 271 && angle < 360) {
			xBias = 1 - ((angle - 270) / 90);
			yBias = 1 - xBias;
			this.getPosition().move(xBias * vel, yBias * vel);
		}
	}

	/**
	 * @param angle Angle to extend on
	 */
	private void forward(float angle, boolean accel) {
		float yBias;
		float xBias;
		float vel = accel ? this.accelerate() : this.brake();
		if (angle >= 0 && angle <= 90) {
			yBias = 1 - (angle / 90);
			xBias = 1 - yBias;
			this.getPosition().move(xBias * vel, (yBias * vel) * -1);
		} else if (angle >= 91 && angle <= 180) {
			xBias = 1 - ((angle - 90) / 90);
			yBias = 1 - xBias;
			this.getPosition().move(xBias * vel, yBias * vel);
		} else if (angle >= 181 && angle <= 270) {
			yBias = 1 - ((angle - 180) / 90);
			xBias = 1 - yBias;
			this.getPosition().move((xBias * vel) * -1, yBias * vel);
		} else if (angle >= 271 && angle < 360) {
			xBias = 1 - ((angle - 270) / 90);
			yBias = 1 - xBias;
			this.getPosition().move((xBias * vel) * -1, (yBias * vel) * -1);
		}
	}

	/**
	 * @param playerId ID of Car
	 */
	private void changeImage(int playerId) {
		this.setImage(new ImageIcon("Images/" + this.getRotation() + (playerId == 0 ? "car.png" : "rac.png")));
	}

	public void resetPosition() {
		this.getPosition().setPosition(380, this.ID == 0 ? 450 : 500);
		this.rotation = 90;
		this.changeImage(this.ID);
		//this.setImage(new ImageIcon("Images/" + this.getRotation() + (this.ID == 0 ? "car.png"
		//	: "rac.png")));
	}

	public boolean collides(ArrayList<Car> others) {
		Rectangle rec1 = this.getPosition().getBounds();

		Rectangle rec2;

		for (Car car :
			others) {
			if (this == car) {
				continue;
			}
			rec2 = car.getPosition().getBounds();

			if (rec1.intersects(rec2)) {
				return true;
			}
		}

		return false;
	}
}
