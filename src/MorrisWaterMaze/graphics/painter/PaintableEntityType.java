package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.model.Background;
import MorrisWaterMaze.model.simulation.WaterMorrisMazeSimulation;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.model.EscapeRouteSection;
import MorrisWaterMaze.model.MouseMovement;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;

import java.util.List;
import java.util.function.Supplier;


enum PaintableEntityType
{
    SIMULATION(WaterMorrisMazeSimulation.class, SimulationPainter::new),
    MOUSE_MOVEMENT(MouseMovement .class, MouseMovementPainter::new),
    PLATFORM(Platform.class, PlatformPainter::new),
    POOL(Pool.class, PoolPainter::new),
    ESCAPE_ROUTE_SECTION(EscapeRouteSection.class, EscapeRouteSectionPainter::new),
    BACKGROUND(Background.class, BackgroundPainter::new );
    
    
    private static final List<PaintableEntityType>
        VALUES = List.of(values());
    
    private final Class<? extends Paintable>
        paintableEntityClass;
        
    private final Supplier<? extends Painter<? extends Paintable>>
        painterInstanceSupplier;
    
    
    PaintableEntityType(Class<? extends Paintable> paintableEntityClass, Supplier<? extends Painter<? extends Paintable>> painterInstanceSupplier)
    {
        this.paintableEntityClass = paintableEntityClass;
        this.painterInstanceSupplier = painterInstanceSupplier;
    }
    
    public static List<PaintableEntityType> getValues()
    {
        return VALUES;
    }
    
    public Class<? extends Paintable> getPaintableEntityClass()
    {
        return paintableEntityClass;
    }
    
    public Painter<? extends Paintable> makePainterInstance()
    {
        return painterInstanceSupplier.get();
    }
}
