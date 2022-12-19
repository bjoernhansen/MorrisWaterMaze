package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.geometry.Point;

import java.awt.geom.Ellipse2D;


public final class Pool implements Paintable
{
	public static final double
		RADIUS = 65;

	private static final double
		BORDER_DISTANCE = 25;

	public static final double
		CENTER_TO_BORDER_DISTANCE = RADIUS + BORDER_DISTANCE;
	
	private final Point
		center;			// Mittelpunkt des Pools
	
	private final Ellipse2D
		border;			// Jeder Punkt des Kreises, welcher die Maus repräsentiert, muss sich innerhalb dieses Kreises befinden
	

	public Pool()
	{
		this.center = Point.newInstance(CENTER_TO_BORDER_DISTANCE, CENTER_TO_BORDER_DISTANCE);
		this.border = 		 new Ellipse2D.Double(	center.getX() - RADIUS,
													center.getY() - RADIUS,
													2 * RADIUS, 
													2 * RADIUS);
	}
	
	public Ellipse2D getBorder()
	{
		return border;
	}
	
	public Point getCenter()
	{
		return center;
	}
	
	public double getRadius()
	{
		return RADIUS;
	}
	
	public double getCenterX()
	{
		return center.getX();
	}
	
	public double getCenterY()
	{
		return center.getY();
	}
}
