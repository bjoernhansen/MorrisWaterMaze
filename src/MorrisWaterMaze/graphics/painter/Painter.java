package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.graphics.Paintable;

abstract public class Painter<E extends Paintable>
{
    abstract public void paint(GraphicsAdapter graphicsAdapter, E paintableEntity);
}
