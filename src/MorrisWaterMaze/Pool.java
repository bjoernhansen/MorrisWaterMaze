package MorrisWaterMaze;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Pool
{	
	Point2D		center;			// Mittelpunkt des Pools 
	Ellipse2D 	border,			// Jeder Punkt des Kreises, welcher die Maus repräsentiert, muss sich innerhalb dieses Kreises befinden
			  	collision_size;	// Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
	double 		radius;			// Pool-Radius
	
	
	
	Pool(double x, double y, double radius)
	{		
		this.radius = radius;
		this.center = new Point2D.Double(x, y);
		this.collision_size = new Ellipse2D.Double(	x - this.radius + Mouse.RADIUS,
													y - this.radius + Mouse.RADIUS,
													2 *(this.radius - Mouse.RADIUS), 
													2 *(this.radius - Mouse.RADIUS));
		
		this.border = 		 new Ellipse2D.Double(	x - this.radius, 
													y - this.radius, 
													2 * this.radius, 
													2 * this.radius);
	}
	
	void paint(Graphics2D g2d, AffineTransform scalor)
	{		
		g2d.setColor(Color.black);
		g2d.draw(scalor.createTransformedShape(this.border));
	}
}
