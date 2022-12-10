package morris_water_maze.model;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.util.Point;

import java.awt.geom.Line2D;


public final class EscapeRouteSection implements Paintable
{
    // TODO Last changed introduced painting bug --> Line not on top of circle
    
    private final Point
        end;
    
    private final Line2D
        line;
    
    
    public EscapeRouteSection(Point start, Point end)
    {
        this.end = end;
        line = new Line2D.Double(start.asPoint2D(), end.asPoint2D());
    }
   
    public Point getEnd()
    {
        return end;
    }
    
    public Line2D getLine()
    {
        return line;
    }
}
