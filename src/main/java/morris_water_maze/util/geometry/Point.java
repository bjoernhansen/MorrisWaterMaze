package morris_water_maze.util.geometry;

import morris_water_maze.model.mouse.VectorBuilder;
import morris_water_maze.util.DoubleComparison;

import java.util.Objects;

import static morris_water_maze.model.mouse.Calculations.length;
import static morris_water_maze.util.DoubleComparison.doubleEquals;


public final class Point
{
    private final double
        x;
    
    private final double
        y;
    
    
    public static Point newInstance(double x, double y)
    {
        return new Point(x, y);
    }
 
    private Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public double distance(Point point)
    {
        double x = point.getX() - this.getX();
        double y = point.getY() - this.getY();
        return Math.sqrt(x * x + y * y);
    }
    
    public Point translate(double distance, double angle)
    {
        return Point.newInstance(
                    x + distance * Math.cos(angle),
                    y + distance * Math.sin(angle));
    }
    
    public boolean isOnTheEdgeOf(Circle circle)
    {
        Point vector = VectorBuilder.from(circle.getCenter())
                                    .to(this);
        return DoubleComparison.doubleEquals(length(vector), circle.getRadius());
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        
        Point other = (Point) o;
        return     doubleEquals(this.x, other.x)
                && doubleEquals(this.y, other.y);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString()
    {
        return "Point{ x=" + x + ", y=" + y + '}';
    }
}