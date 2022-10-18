package MorrisWaterMaze.model;

import MorrisWaterMaze.graphics.Paintable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Pool implements Paintable
{
	public static final double
			RADIUS = 65;

	private static final double
			BORDER_DISTANCE = 25;

	public static final double
			CENTER_TO_BORDER_DISTANCE = RADIUS + BORDER_DISTANCE;

	
	Point2D		center;			// Mittelpunkt des Pools 
	Ellipse2D 	border,			// Jeder Punkt des Kreises, welcher die Maus repräsentiert, muss sich innerhalb dieses Kreises befinden
			  	collisionSize;	// Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
	
	
	
	public Pool()
	{
		this.center = new Point2D.Double(CENTER_TO_BORDER_DISTANCE, CENTER_TO_BORDER_DISTANCE);
		this.collisionSize = new Ellipse2D.Double(	center.getX() - RADIUS + Mouse.RADIUS,
													center.getY() - RADIUS + Mouse.RADIUS,
													2 * (RADIUS - Mouse.RADIUS),
													2 * (RADIUS - Mouse.RADIUS));
		
		this.border = 		 new Ellipse2D.Double(	center.getX() - RADIUS,
													center.getY() - RADIUS,
													2 * RADIUS, 
													2 * RADIUS);
	}
	
	public void paint(Graphics2D g2d, AffineTransform scalor)
	{		
		g2d.setColor(Color.black);
		g2d.draw(scalor.createTransformedShape(this.border));
	}
}
