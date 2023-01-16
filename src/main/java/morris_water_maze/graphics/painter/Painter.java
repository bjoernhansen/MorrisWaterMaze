package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.graphics.Paintable;


abstract class Painter<E extends Paintable>
{
    private static final PaintManager
        PAINT_MANAGER = PaintManager.getInstance();
    
    
    abstract public void paint(GraphicsAdapter graphics, E paintableEntity);
    
    void paintEntity(GraphicsAdapter graphics, Paintable paintableEntity)
    {
        PAINT_MANAGER.paint(graphics, paintableEntity);
    }
}
