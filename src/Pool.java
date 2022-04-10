import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Pool
{	
	Ellipse2D collison_size, border;
	double radius;
	
	Pool(double x, double y, double radius, double mouse_radius)
	{		
		this.radius = radius;
		collison_size = new Ellipse2D.Double(	x - this.radius + mouse_radius, 
												y - this.radius + mouse_radius,
												2*(this.radius - mouse_radius), 
												2*(this.radius - mouse_radius));
		border = new Ellipse2D.Double(	x-this.radius, y-this.radius, 2*this.radius, 2*this.radius);
	}
		
	void paint(Graphics2D g2d)
	{		
		g2d.setColor(new Color(0,0,150));	
		g2d.fill(border);
	}
}
