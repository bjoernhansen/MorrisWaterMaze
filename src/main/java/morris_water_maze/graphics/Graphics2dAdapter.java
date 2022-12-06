package morris_water_maze.graphics;

import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
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
    }
    
    @Override
    public void setColor(Color c)
    {
        graphics.setColor(c);
    }
    
    @Override
    public void draw(Shape s)
    {
        graphics.draw(s);
    }
    
    @Override
    public void fillOval(int x, int y, int width, int height)
    {
        graphics.fillOval(x, y, width, height);
    }
    
    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        graphics.fillRect(x, y, width, height);
    }
    
    @Override
    public void drawImage(Image img, int x, int y, ImageObserver observer)
    {
        graphics.drawImage(img, x, y, observer);
    }
    
    @Override
    public void fill(Shape s)
    {
        graphics.fill(s);
    }
    
    @Override
    public void turnAntialiasingOn()
    {
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
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
