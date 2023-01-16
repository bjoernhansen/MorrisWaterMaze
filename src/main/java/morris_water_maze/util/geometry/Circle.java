package morris_water_maze.util.geometry;


import java.awt.geom.Ellipse2D;

public final class Circle
{
    private final Point
        center;
    
    private final double
        radius;
    
    private final Ellipse2D
        ellipse2D;
    
    
    public static Circle newInstance(Point center, double radius)
    {
        return new Circle(center, radius);
    }
    
    private Circle(Point center, double radius)
    {
        this.center = center;
        this.radius = radius;
        double diameter = 2 * radius;
        this.ellipse2D = new Ellipse2D.Double(
                                        center.getX() - radius,
                                        center.getY() - radius,
                                        diameter,
                                        diameter);
    }
    
    public double getX(){
        return ellipse2D.getX();
    }
    
    public double getMaxX(){
        return ellipse2D.getMaxX();
    }
    
    public double getY(){
        return ellipse2D.getY();
    }
    
    public Point getCenter()
    {
        return center;
    }
    
    public double getRadius()
    {
        return radius;
    }
    
    public double getDiameter()
    {
        return ellipse2D.getWidth();
    }
    
    public boolean contains(Point point)
    {
        return ellipse2D.contains(point.getX(), point.getY());
    }
    
    @Override
    public String toString()
    {
        return "Circle{center=" + center + ", radius=" + radius + '}';
    }
}
