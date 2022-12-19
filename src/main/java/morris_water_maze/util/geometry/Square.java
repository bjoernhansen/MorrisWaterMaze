package morris_water_maze.util.geometry;


import java.awt.geom.Rectangle2D;

public final class Square
{
    private final Point
        center;
    
    private final Rectangle2D
        rectangle;
    
    
    public static Square newInstance(double x, double y, double sideLength)
    {
        return new Square(x, y, sideLength);
    }
    
    private Square(double x, double y, double sideLength)
    {
        this.rectangle = new Rectangle2D.Double(x, y, sideLength, sideLength);
        this.center = calculateCenterOf(this.rectangle);
    }
    
    private Point calculateCenterOf(Rectangle2D rectangle)
    {
        return Point.newInstance(rectangle.getCenterX(), rectangle.getCenterY());
    }
    
    
    public double getX()
    {
        return rectangle.getX();
    }
    
    public double getMinX()
    {
        return rectangle.getMinX();
    }
    
    public double getMaxX()
    {
        return rectangle.getMaxX();
    }
    
    public double getY()
    {
        return rectangle.getY();
    }
    
    public double getMinY()
    {
        return rectangle.getMinY();
    }
    
    public double getMaxY()
    {
        return rectangle.getMaxY();
    }
    
    public double getSideLength()
    {
        return rectangle.getWidth();
    }
   
    public Point getCenter()
    {
        return center;
    }
    
    public int outcode(double x, double y)
    {
        return rectangle.outcode(x, y);
    }
    
    // TODO diese Methode ist nur Zwischenlösung, in jeden Schritt neue Instanz zu erstellen ist ungünstig, Idee: Package Privat Zugriff innerhalb des Geometry packages
    public Rectangle2D asRectangle2D()
    {
        return new Rectangle2D.Double(
            rectangle.getX(),
            rectangle.getY(),
            getSideLength(),
            getSideLength());
    }
    
    @Override
    public String toString()
    {
        return "Square{x=" + rectangle.getX() + ", y=" + rectangle.getY() + ", sideLength=" + getSideLength() + "}";
    }
}
