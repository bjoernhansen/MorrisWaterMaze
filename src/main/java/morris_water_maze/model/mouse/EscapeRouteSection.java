package morris_water_maze.model.mouse;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;


public final class EscapeRouteSection implements Paintable
{
    private final LineSegment
        lineSegment;
    
    
    public EscapeRouteSection(Point start, Point end)
    {
        this.lineSegment = LineSegment.from(start).to(end);
    }
 
    public Point getEnd()
    {
        return lineSegment.getEnd();
    }
    
    public LineSegment getLine()
    {
        return lineSegment;
    }
}
