package ui.twod;


import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.lwjgl.input.Mouse;

import rosick.jglsdk.glm.Vec3;
import simulation.Colony;


public class DisplayPanel extends JPanel implements Runnable {
	
	int delay,width,height;
	
	List<Colony> colonys;
	
	MyMouse m;
	
	public DisplayPanel(int width, int height, int delay){
		super();
		this.delay = delay;
		this.width = width;
		this.height = height;
		
		m = new MyMouse();
		
		setDoubleBuffered(true);
		
		colonys = new ArrayList<>();
		
		colonys.add(new Colony(new Vec3(width/2, height/2, -20)));
		
		return;
	}
	
	public static int getXLocation(Vec3 origin, Vec3 location){
		return (int)(origin.x + location.y);
	}
	
	public static int getYLocation(Vec3 origin, Vec3 location){
		return (int)(origin.y - location.y);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		boolean clicking = false;
		int dx = 0;
		int dy = 0;
		
		Vec3 change = m.getChange();
		
		for (Colony colony : colonys){
			colony.adjustLocation(change);
			
			
			colony.updateAndDraw2D(g, delay);
		}
	}
	
	 public void addNotify() {
	     super.addNotify();
	     Thread refresher = new Thread(this);
	     refresher.start();
	 }
	
	public void run()
	{
		while(true)
		{
			long start = System.nanoTime();
			repaint();
			long end = System.nanoTime();
			try {
				Thread.sleep(delay-((start-end)/10000000));
			} catch (InterruptedException e) {
			}
		}
	}
}
