package morris_water_maze.model.mouse;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.geometry.Line;
import morris_water_maze.util.geometry.Point;


public final class EscapeRouteSection implements Paintable
{
    private final Line
        line;
    
    
    public EscapeRouteSection(Point start, Point end)
    {
        this.line = LineSegmentBuilder.from(start).to(end);
    }
 
    public Point getEnd()
    {
        return line.getEnd();
    }
    
    public Line getLine()
    {
        return line;
    }
}
