package morris_water_maze.graphics.painter.image;

import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.graphics.Paintable;
import morris_water_maze.graphics.painter.PaintManager;

import java.awt.Image;


abstract class AbstractImagePainter implements ImagePainter
{
    private Image
        image;
    
    private GraphicsAdapter
        graphics;
    
    private final PaintManager
        paintManager = PaintManager.getInstance();
    
    private Paintable
        background = new Paintable(){};
    

    @Override
    public void paint(Paintable paintableEntity)
    {
        paintManager.paint(graphics, paintableEntity);
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
        paintManager.paint(graphics, background);
    }
    
    void setImage(Image image)
    {
        this.image = image;
    }
    
    void setGraphics(GraphicsAdapter graphics)
    {
        this.graphics = graphics;
    }
    
    @Override
    public String getSvgString()
    {
        return graphics.getSvgString();
    }
}
