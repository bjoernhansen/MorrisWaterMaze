package morris_water_maze.util.geometry;


public final class LineSegmentBuilder
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
    
    public LineSegment to(Point end)
    {
        return LineSegment.newInstance(start, end);
    }
}
