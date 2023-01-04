package morris_water_maze.util.geometry;


public enum RotationDirection
{
    CLOCKWISE (-1),
    COUNTERCLOCKWISE (1);
    
    
    private final int value;
    
    
    RotationDirection(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
}
