package game;

import org.lwjgl.input.Keyboard;

import rosick.jglsdk.glm.Mat4;
import rosick.jglsdk.glm.Vec3;
import rosick.jglsdk.glm.Vec4;
import simulation.Item;

public class Camera {

	private final Vec3 location; // Centre
	private final Vec3 direction;
	private final Vec3 up;

	float xDeg;
	float yDeg;
	float zDeg;

	float movementSensitivity = 0.005f;
	float viewSensitivity = 0.05f;

	public Camera() {
		location = new Vec3(0, 0, -20);
		up = new Vec3(0, 1, 0);
		direction = new Vec3(0, 0, -1);

		xDeg = 0;
		yDeg = 0;
		zDeg = 0;
	}

	public void update(float deltaT) {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			location.y += deltaT * movementSensitivity;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			location.y -= deltaT * movementSensitivity;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			location.x += deltaT * movementSensitivity;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			location.x -= deltaT * movementSensitivity;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			location.z += deltaT * movementSensitivity;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			location.z -= deltaT * movementSensitivity;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			yDeg += viewSensitivity;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			yDeg -= viewSensitivity;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			xDeg += viewSensitivity;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			xDeg -= viewSensitivity;
		}
	}

	public Mat4 constructMatrix(float elapsedTime) {

		update(elapsedTime);
		return translate().mul(rotate());
	}

	private Mat4 translate() {
		Mat4 theMat = new Mat4(1.0f);
		theMat.setColumn(3, new Vec4(Vec3.negate(location), 1.0f));

		return theMat;
	}

	private Mat4 rotate() {
		Mat4 rotateZ = Mat4.getRotateZ(-zDeg);
		Mat4 rotateX = Mat4.getRotateX(-xDeg);
		Mat4 rotateY = Mat4.getRotateY(-yDeg);

		return rotateZ.mul(rotateX.mul(rotateY));
	}
}
