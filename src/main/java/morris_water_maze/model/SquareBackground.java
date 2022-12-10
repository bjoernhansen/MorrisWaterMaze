package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;

import java.awt.geom.Rectangle2D;


public final class SquareBackground implements Paintable
{
    private final Rectangle2D
        bounds;
     
    
    public SquareBackground(double sideLength)
    {
        this.bounds = new Rectangle2D.Double(0, 0, sideLength, sideLength);
    }
    
    public Rectangle2D getBounds()
    {
        return bounds;
    }
}

