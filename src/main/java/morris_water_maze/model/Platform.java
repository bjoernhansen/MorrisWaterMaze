package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;


public final class Platform implements Paintable
{
    private static final double
        PLATFORM_SIDE_LENGTH = 10;
    
    private final Square
        bounds;


    public Platform()
    {
        bounds = createBounds();
    }
    
    private Square createBounds()
    {
        double alpha = 0.25 * Math.PI;
        double outerCornerRadius = 0.5 * (Pool.RADIUS + PLATFORM_SIDE_LENGTH * Math.sqrt(2));
        
        double x = Pool.CENTER_TO_BORDER_DISTANCE + outerCornerRadius * Math.cos(alpha) - PLATFORM_SIDE_LENGTH;
        double y = Pool.CENTER_TO_BORDER_DISTANCE + outerCornerRadius * Math.sin(alpha) - PLATFORM_SIDE_LENGTH;
        
        return Square.newInstance(x, y, PLATFORM_SIDE_LENGTH);
    }

    public Square getBounds() {
        return bounds;
    }
   
    public Point getCenter() {
        return bounds.getCenter();
    }
}
