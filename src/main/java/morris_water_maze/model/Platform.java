package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.Point;

import java.awt.geom.Rectangle2D;


public final class Platform implements Paintable
{
    private static final double
        PLATFORM_SIDE_LENGTH = 10;
    
    private final Rectangle2D
            bounds;

    private final Point
            center;


    public Platform()
    {
        bounds = createBounds();
        center = Point.newInstance(bounds.getCenterX(), bounds.getCenterY());
    }

    private static Rectangle2D createBounds()
    {
        double alpha = 0.25 * Math.PI;
        double outerCornerRadius = 0.5 * (Pool.RADIUS + PLATFORM_SIDE_LENGTH * Math.sqrt(2));
        return new Rectangle2D.Double(
                Pool.CENTER_TO_BORDER_DISTANCE + outerCornerRadius * Math.cos(alpha) - PLATFORM_SIDE_LENGTH,
                Pool.CENTER_TO_BORDER_DISTANCE + outerCornerRadius * Math.sin(alpha) - PLATFORM_SIDE_LENGTH,
                PLATFORM_SIDE_LENGTH,
                PLATFORM_SIDE_LENGTH);
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public Point getCenter() {
        return center;
    }
}
