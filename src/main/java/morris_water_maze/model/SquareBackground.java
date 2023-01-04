package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.geometry.Square;


public final class SquareBackground implements Paintable
{
    private final Square
        bounds;
     
    
    public SquareBackground(double sideLength)
    {
        this.bounds = Square.newInstance(0.0, 0.0, sideLength);
    }
    
    public Square getBounds()
    {
        return bounds;
    }
}
