package ui.twod;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import rosick.jglsdk.glm.Vec3;

public class MyMouse extends MouseAdapter {
	
	
	boolean isPressed = false;
	boolean change = false;
	
	Vec3 changeDirection = new Vec3(0,0,0);
	
	public void mouseClicked(MouseEvent e){
		if(isPressed){
			changeDirection.x = e.getXOnScreen() - changeDirection.x;
			changeDirection.y = e.getYOnScreen() - changeDirection.y;
			change = true;
			
		} else {
			isPressed = true;
			changeDirection.x = e.getXOnScreen();
			changeDirection.y = e.getYOnScreen();
		}
	}
	
	public Vec3 getChange(){
		if(change){
			change = false;
			return changeDirection;
		} else {
			return new Vec3(0,0,0);
		}
	}

}
