package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.Paintable;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImagePainterImplementation implements ImagePainter
{
    private final Image
        image;
    
    private final Graphics2D
        graphics2D;
    
    private final PaintManager
        paintManager = PaintManager.getInstance();
    
    
    public ImagePainterImplementation(int imageSize)
    {
        image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        graphics2D = getGraphics(image);
    }
    
    private Graphics2D getGraphics(Image Image)
    {
        Graphics2D graphics2D = (Graphics2D) Image.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return graphics2D;
    }
    
    @Override
    public Image paintImageOf(Paintable paintableEntity)
    {
        paintManager.paint(graphics2D, paintableEntity);
        return image;
    }
}
