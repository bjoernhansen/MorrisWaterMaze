package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;


public final class Background implements Paintable
{
    private final int
        sideLength;
    
    
    public Background(int sideLength)
    {
        this.sideLength = sideLength;
    }
    
    public int getSideLength()
    {
        return sideLength;
    }
}

