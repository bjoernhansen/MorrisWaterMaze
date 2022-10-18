package MorrisWaterMaze.model;

import MorrisWaterMaze.graphics.Paintable;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Platform implements Paintable
{
    private final Rectangle2D
            bounds;

    private final Point2D
            center;


    public Platform()
    {
        bounds = createBounds();
        center = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }

    private static Rectangle2D createBounds()
    {
        double alpha = 0.25*Math.PI;
        double platformSiteLength = 10;
        double outerCornerRadius = 0.5*(Pool.RADIUS + platformSiteLength *Math.sqrt(2));
        return new Rectangle2D.Double(
                Pool.CENTER_TO_BORDER_DISTANCE + outerCornerRadius*Math.cos(alpha) - platformSiteLength,
                Pool.CENTER_TO_BORDER_DISTANCE + outerCornerRadius*Math.sin(alpha) - platformSiteLength,
                platformSiteLength, platformSiteLength);
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public Point2D getCenter() {
        return center;
    }
}
