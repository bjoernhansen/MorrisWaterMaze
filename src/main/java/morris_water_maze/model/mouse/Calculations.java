package morris_water_maze.model.mouse;

import morris_water_maze.util.geometry.Point;

import static java.lang.Math.acos;


public final class Calculations
{
    private static final Point
        ORIGIN = Point.newInstance(0.0, 0.0);
    
    
    static double angle(Point point1, Point point2)
    {
        return acos( dotProduct(point1, point2) / (length(point1) * length(point2)) );
    }
    
    static double dotProduct(Point point1, Point point2)
    {
        return    point1.getX() * point2.getX()
                + point1.getY() * point2.getY();
    }
    
    public static double length(Point point)
    {
        return point.distance(ORIGIN);
    }
    
    static double square(double value)
    {
        return value * value;
    }
    
    static double square(Point point)
    {
        return dotProduct(point, point);
    }
    
    private Calculations()
    {
        throw new UnsupportedOperationException("Utility class Calculations can not be instantiated.");
    }
}