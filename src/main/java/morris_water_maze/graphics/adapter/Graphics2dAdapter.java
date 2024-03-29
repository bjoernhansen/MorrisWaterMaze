package morris_water_maze.graphics.adapter;

import morris_water_maze.graphics.Color;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;


public final class Graphics2dAdapter extends AbstractGraphicsAdapter<Graphics2D>
{
    public static GraphicsAdapter of(Graphics graphics)
    {
        return new Graphics2dAdapter((Graphics2D) graphics);
    }
    
    public static GraphicsAdapter of(Image image)
    {
        return Graphics2dAdapter.of(image.getGraphics());
    }
    
    private Graphics2dAdapter(Graphics2D graphics2D)
    {
        super(graphics2D);
        turnAntialiasingOn();
    }
    
    private void turnAntialiasingOn()
    {
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    @Override
    public void setColor(Color color)
    {
        graphics.setColor(color.asAwtColor());
    }
    
    @Override
    public void drawLine(LineSegment lineSegment)
    {
        graphics.drawLine(
            (int)(ZOOM_FACTOR * lineSegment.getStart().getX()),
            (int)(ZOOM_FACTOR * lineSegment.getStart().getY()),
            (int)(ZOOM_FACTOR * lineSegment.getEnd().getX()),
            (int)(ZOOM_FACTOR * lineSegment.getEnd().getY()));
    }
    
    @Override
    public void drawEllipse(Circle circle)
    {
        graphics.drawOval(
            (int)(ZOOM_FACTOR * circle.getX()),
            (int)(ZOOM_FACTOR * circle.getY()),
            (int)(ZOOM_FACTOR * circle.getDiameter()),
            (int)(ZOOM_FACTOR * circle.getDiameter()));
    }
    
    @Override
    public void fillCircleOnTopOfAPoint(Point center, double radius)
    {
        graphics.fillOval(
            (int)(ZOOM_FACTOR * (center.getX() - radius)),
            (int)(ZOOM_FACTOR * (center.getY() - radius)),
            (int)(ZOOM_FACTOR * 2.0 * radius),
            (int)(ZOOM_FACTOR * 2.0 * radius));
    }
    
    @Override
    public void fillSquare(Square square)
    {
        graphics.fillRect(
            (int)(ZOOM_FACTOR * square.getX()),
            (int)(ZOOM_FACTOR * square.getY()),
            (int)(ZOOM_FACTOR * square.getSideLength()),
            (int)(ZOOM_FACTOR * square.getSideLength()));
    }
    
    @Override
    public void drawImage(Image img, int x, int y, ImageObserver observer)
    {
        graphics.drawImage(img, x, y, observer);
    }
    
    @Override
    public String getSvgString()
    {
        if(graphics instanceof SVGGraphics2D)
        {
            return ((SVGGraphics2D)graphics).getSVGElement();
        }
        else
        {
            throw new UnsupportedOperationException();
        }
    }
}
