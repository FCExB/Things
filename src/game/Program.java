package game;

import ui.threed.Display;
import ui.twod.DisplayPanel;
import javax.swing.JFrame;

public class Program extends JFrame {
	int x = 1000;
	int y = 770;
	
	
	public static void main(String[] args) {
		//new Display().start();
		new Program();
	}

	public Program(){
			
			
		setSize(x,y);
		setVisible(true);
		add(new DisplayPanel(x,y,5));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
