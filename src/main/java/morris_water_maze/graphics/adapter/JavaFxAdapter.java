package morris_water_maze.graphics.adapter;

import javafx.scene.canvas.GraphicsContext;
import morris_water_maze.graphics.Color;
import morris_water_maze.util.Point;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public final class JavaFxAdapter extends AbstractGraphicsAdapter<GraphicsContext>
{
    public static GraphicsAdapter of(GraphicsContext graphicsContext)
    {
        return new JavaFxAdapter(graphicsContext);
    }

    private JavaFxAdapter(GraphicsContext graphics)
    {
        super(graphics);
    }
    
    @Override
    public void setColor(Color color)
    {
        graphics.setFill(color.asJavaFxColor());
    }
    
    @Override
    public void drawLine(Line2D line)
    {
        graphics.strokeLine(
            ZOOM_FACTOR * line.getX1(),
            ZOOM_FACTOR * line.getY1(),
            ZOOM_FACTOR * line.getX2(),
            ZOOM_FACTOR * line.getY2());
    }
    
    @Override
    public void drawOval(Ellipse2D ellipse)
    {
        graphics.strokeOval(
            (int)(ZOOM_FACTOR * ellipse.getX()),
            (int)(ZOOM_FACTOR * ellipse.getY()),
            (int)(ZOOM_FACTOR * ellipse.getWidth()),
            (int)(ZOOM_FACTOR * ellipse.getHeight()));
    }
    
    @Override
    public void fillOval(int x, int y, int width, int height)
    {
        graphics.fillOval(
            (int)(ZOOM_FACTOR * x),
            (int)(ZOOM_FACTOR * y),
            (int)(ZOOM_FACTOR * width),
            (int)(ZOOM_FACTOR * height));
    }
    
    @Override
    public void fillRect(int x, int y, int width, int height)
    {
        graphics.fillRect(x, y, width, height);
    }
    
    @Override
    public void drawImage(Image image, int x, int y, ImageObserver observer)
    {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            ImageIO.write((RenderedImage) image, "png", out);
            out.flush();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            javafx.scene.image.Image javaFxImage = new javafx.scene.image.Image(in);
            graphics.drawImage(javaFxImage, x, y);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void drawPoint(Point point)
    {
    
    }
}
