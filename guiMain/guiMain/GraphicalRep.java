package guiMain;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GraphicalRep extends JPanel{
	
	private static final long serialVersionUID = 1709208790748660218L;
	
	GuiMain window = null;
	
	public GraphicalRep(GuiMain window){
		this.window = window;
	}
	
	//Main method
	public void paint(Graphics g){
		 
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawString("TEST", 50, 50);
		
	}
	
	/*public static void main(String[] args) {

        JFrame frame = new JFrame("Java 2D Skeleton");
        frame.add(new GraphicalRep());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 240);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }*/

}
