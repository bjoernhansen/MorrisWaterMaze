package morris_water_maze.model.mouse;

import morris_water_maze.util.calculations.number_generation.EvenlyDistributedNumberGenerator;
import morris_water_maze.util.calculations.number_generation.RandomNumberGenerator;
import morris_water_maze.util.geometry.Point;

import static java.lang.Math.acos;


public final class Calculations
{
    private static final Point
        ORIGIN = Point.newInstance(0.0, 0.0);
    
    private static final double
        RAD_PER_DEGREE = Math.PI / 180.0;
    
    private static RandomNumberGenerator
        evenlyDistributedNumbers = new EvenlyDistributedNumberGenerator();
    
    
    static double angle(Point point1, Point point2)
    {
        return acos(dotProduct(point1, point2) / (length(point1) * length(point2)));
    }
    
    static double dotProduct(Point point1, Point point2)
    {
        return point1.getX() * point2.getX()
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
    
    static double degreesToRadians(double degree)
    {
        return degree * RAD_PER_DEGREE;
    }
    
    static double calculatePolarAngle(Point vector)
    {
        return Math.atan2(vector.getY(), vector.getX());
    }
    
    static double random()
    {
        return evenlyDistributedNumbers.nextDouble();
    }
    
    static void setEvenlyDistributedNumberGenerator(RandomNumberGenerator randomNumberGenerator)
    {
        evenlyDistributedNumbers = randomNumberGenerator;
    }
    
    private Calculations()
    {
        throw new UnsupportedOperationException("Utility class Calculations can not be instantiated.");
    }
}
