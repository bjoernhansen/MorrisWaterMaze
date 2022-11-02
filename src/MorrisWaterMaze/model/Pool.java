package MorrisWaterMaze.model;

import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.util.Point;

import java.awt.geom.Ellipse2D;


public final class Pool implements Paintable
{
	public static final double
		RADIUS = 65;

	private static final double
		BORDER_DISTANCE = 25;

	public static final double
		CENTER_TO_BORDER_DISTANCE = RADIUS + BORDER_DISTANCE;

	
	Point
		center;			// Mittelpunkt des Pools
	
	Ellipse2D
		border,			// Jeder Punkt des Kreises, welcher die Maus repräsentiert, muss sich innerhalb dieses Kreises befinden
		collisionSize;	// Der Mittelpunkt der Maus muss sich innerhalb dieses Kreises befinden.
	
	
	
	public Pool()
	{
		this.center = Point.newInstance(CENTER_TO_BORDER_DISTANCE, CENTER_TO_BORDER_DISTANCE);
		this.collisionSize = new Ellipse2D.Double(	center.getX() - RADIUS + MouseMovement.RADIUS,
													center.getY() - RADIUS + MouseMovement.RADIUS,
													2 * (RADIUS - MouseMovement.RADIUS),
													2 * (RADIUS - MouseMovement.RADIUS));
		
		this.border = 		 new Ellipse2D.Double(	center.getX() - RADIUS,
													center.getY() - RADIUS,
													2 * RADIUS, 
													2 * RADIUS);
	}
	
	public Ellipse2D getBorder()
	{
		return border;
	}
}
