package morris_water_maze.util.geometry;


import java.util.Objects;

public final class Dimension
{
    private final int
        height;
    
    private final int
        width;
        
    public Dimension(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    @Override
    public boolean equals(Object otherObject)
    {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        Dimension otherDimension = (Dimension) otherObject;
        return height == otherDimension.height && width == otherDimension.width;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(height, width);
    }
    
    @Override
    public String toString()
    {
        return "Dimension{height=" + height + ", width=" + width + '}';
    }
}
