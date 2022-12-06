package morris_water_maze.graphics.painter.image;

import morris_water_maze.graphics.Graphics2dAdapter;
import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.graphics.Paintable;
import morris_water_maze.graphics.painter.PaintManager;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;


abstract class AbstractImagePainter implements ImagePainter
{
    private Image
        image;
    
    private GraphicsAdapter
        graphicsAdapter;
    
    private final PaintManager
        paintManager = PaintManager.getInstance();
    
    private Paintable
        background = new Paintable(){};
    

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
    
    void setImage(Image image)
    {
        this.image = image;
    }
    
    void setGraphicsAdapter(GraphicsAdapter graphicsAdapter)
    {
        this.graphicsAdapter = graphicsAdapter;
    }
    
    @Override
    public String getSvgString()
    {
        return graphicsAdapter.getSvgString();
    }
}
