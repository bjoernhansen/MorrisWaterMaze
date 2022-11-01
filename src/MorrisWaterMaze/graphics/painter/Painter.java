package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.SimulationController;
import MorrisWaterMaze.graphics.Paintable;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public abstract class Painter<E extends Paintable>
{
    static final PaintManager
        PAINT_MANAGER = PaintManager.getInstance();
    
    static final AffineTransform
        affineTransformation = new AffineTransform(SimulationController.ZOOM_FACTOR, 0, 0, SimulationController.ZOOM_FACTOR, 0, 0);
        
    abstract public void paint(Graphics2D graphicsAdapter, E paintableEntity);
}
