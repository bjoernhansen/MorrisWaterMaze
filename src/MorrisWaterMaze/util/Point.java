package MorrisWaterMaze.util;

import java.awt.geom.Point2D;


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
    
    public static Point of(Point2D startPosition)
    {
        return new Point(startPosition.getX(), startPosition.getY());
    }

    private Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Point2D asPoint2D()
    {
        return new Point2D.Double(x, y);
    }
    
    public double distance(Point point)
    {
        double x = point.getX() - this.getX();
        double y = point.getY() - this.getY();
        return Math.sqrt(x * x + y * y);
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
}