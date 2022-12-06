package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;


public final class SquareBackground implements Paintable
{
    private final int
        sideLength;
    
    
    public SquareBackground(int sideLength)
    {
        this.sideLength = sideLength;
    }
    
    public int getSideLength()
    {
        return sideLength;
    }
}

