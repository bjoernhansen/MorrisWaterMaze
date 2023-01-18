package morris_water_maze.util.geometry;

import morris_water_maze.model.mouse.Geometry;

import java.awt.geom.Line2D;
import java.lang.IllegalArgumentException;
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
    
    public RotationDirection getRotationDirectionWithRespectTo(Point point)
    {
        int relativeCCW = Line2D.relativeCCW(
                                    point.getX(), point.getY(),
                                    end.getX(), end.getY(),
                                    start.getX(), start.getY());
        
        return relativeCCW == 1
                ? RotationDirection.CLOCKWISE
                : RotationDirection.COUNTERCLOCKWISE;
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
    
    public Point getExitPointOutOf(Circle circle)
    {
        return Geometry.lineSegmentExitPointOutOfCircle(this, circle)
                       .orElseThrow(() -> new IllegalArgumentException("There is no exit point from " + this + " out of " + circle + "."));
    }
    
    public Point getEntryPointInto(Square square)
    {
        return Geometry.clipLine(this, square)
                       .map(LineSegment::getStart)
                       .orElseThrow(() -> new IllegalArgumentException("There is no intersection between " + this + " and " + square + "."));
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
    
    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        LineSegment other = (LineSegment) o;
        return start.equals(other.start) && end.equals(other.end);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(start, end);
    }
}
