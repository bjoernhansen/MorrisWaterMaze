package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.Paintable;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableMap;


class PaintManager
{
    private static PaintManager
        instance;
        
    private final Map<Class<? extends Paintable>, Painter<? extends Paintable>>
        painter = PaintableEntityType.getValues()
                                     .stream()
                                     .collect(toUnmodifiableMap(PaintableEntityType::getPaintableEntityClass, PaintableEntityType::makePainterInstance));
    
    
    public static PaintManager getInstance()
    {
        if(instance == null)
        {
            instance = new PaintManager();
        }
        return instance;
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
