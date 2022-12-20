package morris_water_maze.graphics.adapter;

import javafx.scene.canvas.GraphicsContext;
import morris_water_maze.graphics.Color;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.LineSegment;
import morris_water_maze.util.geometry.Point;
import morris_water_maze.util.geometry.Square;

import javax.imageio.ImageIO;
import java.awt.Image;
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
    public void drawLine(LineSegment lineSegment)
    {
        graphics.strokeLine(
            ZOOM_FACTOR * lineSegment.getStart().getX(),
            ZOOM_FACTOR * lineSegment.getStart().getY(),
            ZOOM_FACTOR * lineSegment.getEnd().getX(),
            ZOOM_FACTOR * lineSegment.getEnd().getY());
    }
    
    @Override
    public void drawEllipse(Circle circle)
    {
        graphics.strokeOval(
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
    public void fillSquare(Square rectangle)
    {
        graphics.fillRect(
            (int)(ZOOM_FACTOR * rectangle.getX()),
            (int)(ZOOM_FACTOR * rectangle.getY()),
            (int)(ZOOM_FACTOR * rectangle.getSideLength()),
            (int)(ZOOM_FACTOR * rectangle.getSideLength()));
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
}
