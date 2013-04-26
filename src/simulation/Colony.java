package simulation;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import game.Camera;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import rosick.jglsdk.glm.Mat4;
import rosick.jglsdk.glm.Vec3;
import rosick.jglsdk.glm.Vec4;
import ui.twod.DisplayPanel;

public class Colony {

	private Vec3 origin, location;
	private float radius;

	public ArrayList<Thing> members;

	public Colony(Vec3 origin) {
		this.origin = origin;

		location = new Vec3(0, 0, -20);

		radius = 100;

		members = new ArrayList<>();
		members.add(new Thing(10f, 0, 0, this));
		members.add(new Thing(10f, 50, 50, this));
		members.add(new Thing(10f, -50, -50, this));
		members.add(new Thing(10f, 50, -50, this));
	}

	public boolean validPosition(Vec3 theirLocation, float radius) {

		float diff = Vec3.sub(location, theirLocation).getMagnitude();
		return diff + radius < this.radius;
	}

	public void adjustLocation(Vec3 change) {
		location.add(change);
	}

	public void update(float deltaT) {
		List<Thing> toAdd = new ArrayList<>();
		List<Thing> toRemove = new ArrayList<>();

		for (Thing thing : members) {

			if (thing == null) {
				continue;
			}

			for (Thing thing2 : members) {
				if (thing2 != thing) {
					Thing newOne = thing.react(thing2);
					if (newOne == thing2) {
						toRemove.add(thing2);
					} else if (newOne != null) {
						toAdd.add(newOne);
						System.err.println("Item added");
					}
				}
			}

			thing.update((float) deltaT);
		}

		members.addAll(toAdd);
		members.removeAll(toRemove);
	}

	public Mat4 constructMatrix() {
		
		return translate().mul(new Mat4 (radius));
		
	}
	
	private Mat4 translate() {
		Mat4 theMat = new Mat4(1.0f);	
		theMat.setColumn(3, new Vec4(location, 1.0f));
		
		return theMat;	
	}
	

	public void updateAndDraw2D(Graphics g, int delay) {
		g.drawOval(DisplayPanel.getXLocation(origin, location)
				- (int) radius,
				DisplayPanel.getYLocation(origin, location)
						- (int) radius, (int) radius*2, (int) radius*2);

		update(delay);

		for (Thing thing : members) {

			Vec3 thingOrigin = new Vec3(DisplayPanel.getXLocation(origin,
					location), DisplayPanel.getYLocation(origin, location), -20);
			thing.draw2D(g, thingOrigin);
		}
	}
	
}
