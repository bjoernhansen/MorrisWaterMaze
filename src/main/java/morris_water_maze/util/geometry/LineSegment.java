package morris_water_maze.util.geometry;

import java.awt.geom.Line2D;
import java.util.Objects;


public final class LineSegment
{
    private final Point
        start;
    
    private final Point
        end;
    
    public static Builder from(Point start)
    {
        return Builder.from(start);
    }
    
    private static LineSegment newInstance(Point start, Point end)
    {
        return new LineSegment(
                    Objects.requireNonNull(start),
                    Objects.requireNonNull(end));
    }
    
    private LineSegment(Point start, Point end)
    {
        this.start = start;
        this.end = end;
    }
    
    public static RotationDirection rotationDirection(Point lineStart,
                                                      Point lineEnd,
                                                      Point point)
    // TODO: Methode idealer Weise nicht mehr statisch sondern Methode der Instanz
    {
        int relativeCCW = Line2D.relativeCCW(
                                    lineStart.getX(), lineStart.getY(),
                                    lineEnd.getX(), lineEnd.getY(),
                                    point.getX(), point.getY());
        
        return relativeCCW == 1
            ? RotationDirection.COUNTERCLOCKWISE
            : RotationDirection.CLOCKWISE;
    }
    
    public boolean intersects(Square square)
    {
        return square.intersects(this);
    }
    
    public Point getStart()
    {
        return start;
    }
    
    public Point getEnd()
    {
        return end;
    }
    
    @Override
    public String toString()
    {
        return "LineSegment{start=" + start + ", end=" + end + '}';
    }
    
    
    public static class Builder
    {
        private final Point
            start;
        
        
        private static Builder from(Point start)
        {
            return new Builder(start);
        }
        
        private Builder(Point start)
        {
            this.start = start;
        }
        
        public LineSegment to(Point end)
        {
            return LineSegment.newInstance(start, end);
        }
    }
}
