package MorrisWaterMaze.model;

import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.util.Point;


public final class EscapeRouteSection implements Paintable
{
    private final Point
        start;
    
    private final Point
        end;
    
    public EscapeRouteSection(Point start, Point end)
    {
        this.start = start;
        this.end = end;
    }
    
    public Point getStart()
    {
        return start;
    }
    
    public Point getEnd()
    {
        return end;
    }
}
