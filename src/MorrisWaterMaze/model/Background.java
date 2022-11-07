package MorrisWaterMaze.model;

import MorrisWaterMaze.graphics.Paintable;


public class Background implements Paintable
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
