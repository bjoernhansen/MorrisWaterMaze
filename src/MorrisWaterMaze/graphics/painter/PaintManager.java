package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.Mouse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PaintManager extends Painter<Paintable>
{
    Map<Class<? extends Paintable>, Painter<? extends Paintable>> painter = new HashMap<>();
    
    
    PaintManager()
    {
        painter.put(Mouse.class, new MousePainter());
    }
    
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, Paintable paintableEntity)    {
        Optional.ofNullable(getPainter(paintableEntity)).ifPresent(
            painter -> painter.paint(graphicsAdapter, paintableEntity));
        
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Paintable> Painter<T> getPainter(T paintableEntity) {
        return (Painter<T>) painter.get(paintableEntity.getClass());
    }
}
