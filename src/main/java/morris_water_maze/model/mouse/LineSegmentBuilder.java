package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Line;
import morris_water_maze.util.geometry.Point;


final class LineSegmentBuilder
{
    private final Point
        start;
    
    
    public static LineSegmentBuilder from(Point start)
    {
        return new LineSegmentBuilder(start);
    }
    
    private LineSegmentBuilder(Point start)
    {
        this.start = start;
    }
    
    public Line to(Point end)
    {
        return Line.newInstance(start, end);
    }
}
