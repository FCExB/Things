package simulation;

import java.awt.Graphics;
import java.util.Random;

import rosick.jglsdk.glm.Mat4;
import rosick.jglsdk.glm.Vec3;
import rosick.jglsdk.glm.Vec4;
import ui.threed.Util;

public class Thing {
	private final Vec3 location; // Centre
	private final Vec3 direction;
	private final Vec3 up;

	private final Vec3 dimensions;

	// Things are round

	private static Random rand;

	private float radius;

	private Thing baby;

	private boolean readyForIt;
	private boolean hungry;
	private boolean havingBaby;

	private static final int RADIUS = 0;
	private static final int SLEEP = 1;
	private static final int CHANGE_DIRECTION = 2;

	private final float[] attributes = new float[3];

	private float timeSinceIt;
	private float timeSinceFood;

	private Colony home;

	// chances of action between 0 and 1
	double sleep = 0.01;
	double changeDirection = 0.05;

	public Thing(float radius, float x, float y, Colony home) {
		dimensions = new Vec3(radius * 2, radius * 2, radius * 2);

		location = new Vec3(x, y, -20);

		direction = new Vec3(0f);
		direction.x = (float) (Math.random() * 2 - 1);
		direction.x = (float) (Math.random() * 2 - 1);

		up = new Vec3(0, 0, 1);

		timeSinceIt = timeSinceFood = 0;
		readyForIt = hungry = havingBaby = false;

		rand = new Random();

		attributes[RADIUS] = radius;
		attributes[SLEEP] = 0.01f;
		attributes[CHANGE_DIRECTION] = 0.05f;

		this.radius = radius;
		this.home = home;
	}

	private Thing(float radius, Vec3 location, Colony home) {
		this.location = location;
		dimensions = new Vec3(radius * 2, radius * 2, radius * 2);

		direction = new Vec3(0f);
		direction.x = (float) (Math.random() * 2 - 1);
		direction.x = (float) (Math.random() * 2 - 1);

		up = null;

		timeSinceIt = timeSinceFood = 0;
		readyForIt = hungry = false;

		rand = new Random();

		this.radius = radius;
		this.home = home;
	}

	private Thing(Thing thing1, Thing thing2) {
		location = new Vec3(0, 0, -20);
		dimensions = thing1.dimensions;
		up = null;

		direction = new Vec3(0f);
		direction.x = (float) (Math.random() * 2 - 1);
		direction.x = (float) (Math.random() * 2 - 1);

		float[] one = thing1.getAtttributes();
		float[] two = thing2.getAtttributes();

		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = (one[i] + two[i]) / 2;
		}

		readyForIt = false;
		havingBaby = true;
	}

	private Thing(Thing thing) {
		this.location = thing.location.clone();
		this.direction = thing.direction.clone();
		this.dimensions = thing.dimensions.clone();
		this.up = thing.up.clone();

		timeSinceIt = timeSinceFood = 0;
		readyForIt = hungry = havingBaby = false;

		radius = thing.radius;
		havingBaby = thing.havingBaby;
		home = thing.home;

		readyForIt = thing.readyForIt;
	}

	public float[] getAtttributes() {
		return attributes.clone();
	}

	public boolean getIsReadyForIt() {
		return readyForIt;
	}

	public void update(float deltaT) {

		int variation = (int) (Math.random() * 20 - 10);

		timeSinceIt += deltaT;
		timeSinceIt += variation;
		timeSinceFood += deltaT;
		timeSinceFood += variation;

		if (havingBaby) {

		}

		if (timeSinceIt > 6000) {
			timeSinceIt = 0;
			readyForIt = true;
		}

		if (timeSinceFood > 30000) {
			timeSinceFood = 0;
			hungry = true;
		}

		if (Thing.rand.nextDouble() < sleep) {
			return;
		}

		if (Thing.rand.nextDouble() < changeDirection) {
			changeDirection();
		}

		if (home.validPosition(getNextLocation(deltaT), radius)) {
			move(deltaT);
		} else {
			turnOppositeDirection();
			move(deltaT);
		}
	}

	public Vec3 getNextLocation(float deltaT) {
		return Vec3.add(location, Vec3.scale(direction, deltaT * 0.01f));
	}

	private void move(float deltaT) {
		location.add(Vec3.scale(direction, deltaT * 0.01f));
	}

	public Thing makeBaby(Thing thing) {
		readyForIt = false;
		havingBaby = true;

		Vec3 newLocation = new Vec3(0, 0, -20);

		if (home.validPosition(newLocation, radius)) {
			havingBaby = false;
			return new Thing(radius, newLocation, home);
		}

		return null;
	}

	public float distanceTo(Thing thing) {
		return vectTo(thing).getMagnitude();
	}

	public Vec3 vectTo(Thing thing) {
		return Vec3.sub(thing.location, location);
	}

	public Thing react(Thing thing) {

		if (distanceTo(thing) > radius * 2 + thing.radius) {
			return null;
		}

		if (thing.readyForIt && readyForIt) {
			readyForIt = false;
			return new Thing(thing, this);

		} else if (hungry) {

			hungry = false;
			return thing;
		}

		return null;
	}

	public void changeDirection() {
		double rand = Math.random() * 2 - 1;
		if (Math.random() < 0.5) {
			direction.x += rand;
		} else {
			direction.y += rand;
		}
		direction.normalise();
	}

	public void turnOppositeDirection() {
		direction.x *= -1;
		direction.y *= -1;
		direction.z *= -1;
	}

	public Mat4 constructMatrix(float elapsedTime) {

		update(elapsedTime);
		return translate().mul(rotate());
	}

	private Mat4 translate() {
		Mat4 theMat = new Mat4(1.0f);
		theMat.setColumn(3, new Vec4(location, 1.0f));

		return theMat;
	}

	private Mat4 rotate() {
		float zDeg = Util.radToDeg(Math.atan(direction.y / direction.x));
		float xDeg = Util.radToDeg(Math.atan(direction.y / direction.z));
		float yDeg = Util.radToDeg(Math.atan(direction.z / direction.x));

		if (direction.x == 0f) {
			zDeg = 0;
			yDeg = 0;
		}
		if (direction.z == 0f) {
			xDeg = 0;
		}

		Mat4 rotateZ = Mat4.getRotateZ(zDeg);
		Mat4 rotateX = Mat4.getRotateX(xDeg);
		Mat4 rotateY = Mat4.getRotateY(yDeg);

		return rotateZ.mul(rotateX.mul(rotateY));
	}

	private Mat4 scale() {
		Mat4 theMat = new Mat4(1.0f);
		theMat.set(0, 0, dimensions.x);
		theMat.set(1, 1, dimensions.y);
		theMat.set(2, 2, dimensions.z);

		return theMat;
	}

	public void draw2D(Graphics g, Vec3 origin) {
		g.drawOval((int) (origin.x + (location.x - radius)),
				(int) (origin.y - (location.y + radius)), (int) (radius * 2),
				(int) (radius * 2));
	}

	public Thing clone() {
		return new Thing(this);
	}
}
