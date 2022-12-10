package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;


public final class SquareBackground implements Paintable
{
    private final double
        sideLength;
    
    
    public SquareBackground(double sideLength)
    {
        this.sideLength = sideLength;
    }
    
    public double getSideLength()
    {
        return sideLength;
    }
}

