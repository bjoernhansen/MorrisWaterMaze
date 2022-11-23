package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;


public final class Background implements Paintable
{
    private final int
        height;
    
    private final int
        width;
    
    
    public Background(int sideLength)
    {
        width = sideLength;
        height = sideLength;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
}
