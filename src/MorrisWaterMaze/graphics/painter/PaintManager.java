package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.Simulation;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.MouseMovement;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PaintManager
{
    Map<Class<? extends Paintable>, Painter<? extends Paintable>>
        painter = new HashMap<>();
        
    private static PaintManager
        instance;
    
    
    public static PaintManager getInstance()
    {
        if(instance == null)
        {
            instance = new PaintManager();
        }
        return instance;
    }
    
    private PaintManager()
    {
        painter.put(MouseMovement.class, new MouseMovementPainter());
        painter.put(Pool.class, new PoolPainter());
        painter.put(Simulation.class, new SimulationPainter());
        painter.put(Platform.class, new PlatformPainter());
    }
    
    public void paint(Graphics2D graphicsAdapter, Paintable paintableEntity)    {
        Optional.ofNullable(getPainter(paintableEntity)).ifPresent(
            painter -> painter.paint(graphicsAdapter, paintableEntity));
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Paintable> Painter<T> getPainter(T paintableEntity) {
        return (Painter<T>) painter.get(paintableEntity.getClass());
    }
}
