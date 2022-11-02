package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.Graphics2DAdapter;
import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.graphics.Paintable;

import java.awt.Image;
import java.awt.image.BufferedImage;


public final class ImagePainterImplementation implements ImagePainter
{
    private final Image
        image;
    
    private final GraphicsAdapter
        graphicsAdapter;
    
    private final PaintManager
        paintManager = PaintManager.getInstance();
    
    
    public ImagePainterImplementation(int imageSize)
    {
        image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        graphicsAdapter = Graphics2DAdapter.of(image);
        graphicsAdapter.turnAntialiasingOn();
    }
    
    @Override
    public Image paintImageOf(Paintable paintableEntity)
    {
        paintManager.paint(graphicsAdapter, paintableEntity);
        return image;
    }
}
