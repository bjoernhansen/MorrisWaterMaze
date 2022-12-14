package morris_water_maze.model.mouse;

import morris_water_maze.util.Point;

import java.awt.geom.Line2D;


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
    
    public Line2D to(Point end)
    {
        return new Line2D.Double(start.asPoint2D(), end.asPoint2D());
    }
}
