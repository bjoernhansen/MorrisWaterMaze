package morris_water_maze.graphics.adapter;

import javafx.scene.canvas.GraphicsContext;
import morris_water_maze.graphics.Color;
import morris_water_maze.util.Point;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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
    public void drawEllipse(Ellipse2D ellipse)
    {
        graphics.strokeOval(
            (int)(ZOOM_FACTOR * ellipse.getX()),
            (int)(ZOOM_FACTOR * ellipse.getY()),
            (int)(ZOOM_FACTOR * ellipse.getWidth()),
            (int)(ZOOM_FACTOR * ellipse.getHeight()));
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
    public void fillRect(Rectangle2D rectangle)
    {
        graphics.fillRect(
            (int)(ZOOM_FACTOR * rectangle.getX()),
            (int)(ZOOM_FACTOR * rectangle.getY()),
            (int)(ZOOM_FACTOR * rectangle.getWidth()),
            (int)(ZOOM_FACTOR * rectangle.getHeight()));
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
    
    /*
    
      public static javafx.scene.image.Image createImage(java.awt.Image image) throws IOException {
    if (!(image instanceof RenderedImage)) {
      BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
              image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      Graphics g = bufferedImage.createGraphics();
      g.drawImage(image, 0, 0, null);
      g.dispose();

      image = bufferedImage;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ImageIO.write((RenderedImage) image, "png", out);
    out.flush();
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    return new javafx.scene.image.Image(in);
  }
    
     */
}
