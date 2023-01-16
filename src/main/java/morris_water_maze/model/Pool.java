package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.Point;


public final class Pool implements Paintable
{
	public static final double
		RADIUS = 65;

	private static final double
		BORDER_DISTANCE = 25;

	public static final double
		CENTER_TO_BORDER_DISTANCE = RADIUS + BORDER_DISTANCE;
	
	private final Circle
		border;			// Jeder Punkt des Kreises, welcher die Maus repräsentiert, muss sich innerhalb dieses Kreises befinden
	

	public Pool()
	{
		Point center = Point.newInstance(CENTER_TO_BORDER_DISTANCE, CENTER_TO_BORDER_DISTANCE);
		this.border = Circle.newInstance(center, RADIUS);
	}
	
	public Circle getBorder()
	{
		return border;
	}
	
	public Point getCenter()
	{
		return border.getCenter();
	}
}
