package morris_water_maze.graphics.painter;

import morris_water_maze.Main;
import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.graphics.Paintable;

import java.awt.geom.AffineTransform;


abstract class Painter<E extends Paintable>
{
    final static double
        ZOOM_FACTOR = Main.ZOOM_FACTOR;
    
    private static final PaintManager
        PAINT_MANAGER = PaintManager.getInstance();
    
    static final AffineTransform
        affineTransformation = new AffineTransform(ZOOM_FACTOR, 0, 0, ZOOM_FACTOR, 0, 0);
        
    
    abstract public void paint(GraphicsAdapter graphicsAdapter, E paintableEntity);
    
    void paintEntity(GraphicsAdapter graphics2D, Paintable paintableEntity)
    {
        PAINT_MANAGER.paint(graphics2D, paintableEntity);
    }
}
