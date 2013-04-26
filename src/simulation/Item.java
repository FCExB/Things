package simulation;

import java.awt.Graphics;

import rosick.jglsdk.glm.*;
import ui.*;
import ui.threed.Util;

public abstract class Item {
	private final Vec3 location; //Centre
	private final Vec3 direction;
	private final Vec3 up;
	
	public Item(){
		location = new Vec3(0,0,-20);
		direction = new Vec3(1,0,0);
		up = new Vec3(0,0,1);
	}
	
	public Item(Vec3 dimensions, Vec3 location) {
		
		this.location = location;
		
		direction = new Vec3(0f);
		direction.x = (float)(Math.random()*2 -1);
		direction.x = (float)(Math.random()*2 -1);
		
		up = new Vec3(0,0,1);
		
	}
	
	private void setDirection(Vec3 next) {
		direction.x = next.x;
		direction.y = next.y;
		direction.z = next.z;	
	}
	
	public void changeDirection(){
		double rand = Math.random()*2 - 1;
		if(Math.random() < 0.5) {
			direction.x += rand;	
		} else {
			direction.y += rand;
		}
		setDirection(getNormalisedDirection());
	}
	
    public Vec3 getLocation() {
    	return location;
    }
	
	public Vec3 getNextLocation(float deltaT){
		return Vec3.add(location, Vec3.scale(direction, deltaT*0.01f));
	}
	
    public void turnOppositeDirection(){
    	direction.x *= -1;
    	direction.y *= -1;
    	direction.z *= -1;
    }
    
	public Vec3 getNormalisedDirection() {
		return Vec3.scale(direction, 1/direction.getMagnitude());
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
	
	private Mat4 rotate(){
		float zDeg = Util.radToDeg(Math.atan(direction.y/direction.x));
		float xDeg = Util.radToDeg(Math.atan(direction.y/direction.z));
	    float yDeg = Util.radToDeg(Math.atan(direction.z/direction.x));
		
		if(direction.x == 0f) {
			zDeg = 0;
			yDeg = 0;
		}
		if(direction.z == 0f) {
			xDeg = 0;
		}
	    
		Mat4 rotateZ = Mat4.getRotateZ(zDeg);
		Mat4 rotateX = Mat4.getRotateX(xDeg);
		Mat4 rotateY = Mat4.getRotateY(yDeg);
		
		return rotateZ.mul(rotateX.mul(rotateY));
	}
	
	/*
	private Mat4 scale(){
		Mat4 theMat = new Mat4(1.0f);
		theMat.set(0, 0, dimensions.x);
		theMat.set(1, 1, dimensions.y);
		theMat.set(2, 2, dimensions.z);
		
		return theMat;
	}*/
	
	public abstract void update(float deltaT);
}
