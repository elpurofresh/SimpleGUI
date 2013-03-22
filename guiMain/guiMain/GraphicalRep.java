package guiMain;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class GraphicalRep extends JPanel{

	private static final long serialVersionUID = 1709208790748660218L;

	GuiMain window = null;
	
	public GraphicalRep(GuiMain window){
		super();
		this.window = window;
	}

	//Main method
	public void paintComponent(Graphics g){
		//super.paintComponent(g); //clear
		
		double centerPanelX = getWidth()/2;
		double centerPanelY = getHeight()/2;
		
		Graphics2D g2D = (Graphics2D) g;
		
		/*g2D.drawString("TEST", centerPanelX, centerPanelY);
		g2D.draw(drawCircleCenterAt(centerPanelX, centerPanelY, 50, 50));*/
		drawCircleText(g2D, "Mn", "Sending", centerPanelX-350, centerPanelY, 60, 60);
		drawCircleText(g2D, "C1", "T:25.2^C", centerPanelX, centerPanelY, 50, 50);
		drawCircleText(g2D, "C2", "T:24.8^C", centerPanelX+100, centerPanelY, 50, 50);

	}
	
	private Ellipse2D.Double drawCircleCenterAt(double cx, double cy, double w, double h){
		double x = cx - (w/2);
		double y = cy - (h/2);
		return new Ellipse2D.Double(x, y, w, h);		
	}
	
	private Rectangle2D.Double drawRectangleCenterAt(double cx, double cy, double w, double h){
		double x = cx - (w/2);
		double y = cy - (h/2);
		return new Rectangle2D.Double(x, y, w, h);		
	}
	
	private void drawCircleText(Graphics2D g, String name, String data, double cx, double cy, double w, double h){
		g.draw(drawCircleCenterAt(cx, cy, w, h));
		g.drawString(name, (int) (cx-7), (int) (cy+5));
		g.drawString(data, (int) (cx-w/2), (int) (cy + (h/2 + 16)));
		g.draw(drawRectangleCenterAt(cx, cy+(3*h/4), w*1.5, h/2));
		
	}
	
	
	


}
