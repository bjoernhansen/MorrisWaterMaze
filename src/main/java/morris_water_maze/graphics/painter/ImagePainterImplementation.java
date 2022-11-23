package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Graphics2DAdapter;
import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.graphics.Paintable;

import java.awt.Image;
import java.awt.image.BufferedImage;


public final class ImagePainterImplementation implements ImagePainter
{
    private final int
        imageSize;
    
    private final Image
        image;
    
    private final GraphicsAdapter
        graphicsAdapter;
    
    private final PaintManager
        paintManager = PaintManager.getInstance();
    
    private Paintable
        background = new Paintable(){};
    
    
    public static ImagePainter newInstanceWithBackground(int imageSize, Paintable background)
    {
        ImagePainter imagePainter = new ImagePainterImplementation(imageSize);
        imagePainter.setBackground(background);
        imagePainter.initializeImage();
        return imagePainter;
    }
    
    private ImagePainterImplementation(int imageSize)
    {
        this.imageSize = imageSize;
        image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        graphicsAdapter = Graphics2DAdapter.of(image);
        graphicsAdapter.turnAntialiasingOn();
    }
  
    @Override
    public void paint(Paintable paintableEntity)
    {
        paintManager.paint(graphicsAdapter, paintableEntity);
    }
    
    @Override
    public Image getImage()
    {
        return image;
    }
    
    @Override
    public void setBackground(Paintable background)
    {
        this.background = background;
    }
    
    @Override
    public void initializeImage()
    {
        paintManager.paint(graphicsAdapter, background);
    }
    
    @Override
    public ImagePainter makeCopy()
    {
        return newInstanceWithBackground(imageSize, background);
    }
}
