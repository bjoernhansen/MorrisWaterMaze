package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Point;


final class VectorBuilder
{
    private final Point start;
    
    
    public static VectorBuilder from(Point start)
    {
        return new VectorBuilder(start);
    }
    
    private VectorBuilder(Point start)
    {
        this.start = start;
    }
    
    public Point to(Point end)
    {
        return Point.newInstance(end.getX() - start.getX(), end.getY() - start.getY());
    }
}
