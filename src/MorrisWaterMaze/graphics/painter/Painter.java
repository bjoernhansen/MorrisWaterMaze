package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.Main;
import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.util.Point;

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
    
    final void paintCircleOnTopOfAPoint(GraphicsAdapter graphicsAdapter, Point point, double radius)
    {
        double zoomFactor = ZOOM_FACTOR;
        graphicsAdapter.fillOval(
            (int)(zoomFactor * (point.getX() - radius)),
            (int)(zoomFactor * (point.getY() - radius)),
            (int)(zoomFactor * radius * 2),
            (int)(zoomFactor * radius * 2));
    }
}
