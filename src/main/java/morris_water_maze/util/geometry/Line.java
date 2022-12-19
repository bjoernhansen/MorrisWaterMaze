package morris_water_maze.util.geometry;

import java.awt.geom.Line2D;

public final class Line
{
    private final Point
        start;
    
    private final Point
        end;
    
    private final Line2D
        line2D;
    
    
    public static Line newInstance(Point start, Point end)
    {
        return new Line(start, end);
    }
    
    private Line(Point start, Point end)
    {
        this.start = start;
        this.end = end;
        line2D = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
    }
    
    public static int relativeCCW(double x1, double y1,
                                  double x2, double y2,
                                  double px, double py)
    {
        return Line2D.relativeCCW(x1, y1, x2, y2, px, py);
    }
    
    public boolean intersects(Square square)
    {
        // TODO geht besser, da hier ein neues Rectangle2D erstellt wird (siehe Implementierung)
        return line2D.intersects(
                        square.getX(),
                        square.getY(),
                        square.getSideLength(),
                        square.getSideLength());
    }
    
    public Point getStart()
    {
        return start;
    }
    
    public Point getEnd()
    {
        return end;
    }
    
    
    public double getStartX(){
        return start.getX();
    }
    
    public double getStartY(){
        return start.getY();
    }
    
    public double getEndX(){
        return end.getX();
    }
    
    public double getEndY(){
        return end.getY();
    }
    
    @Override
    public String toString()
    {
        return "Line{start=" + start + ", end=" + end + '}';
    }
}
