package MorrisWaterMaze.graphics;

import java.util.Objects;


abstract class AbstractGraphicsAdapter<E> implements GraphicsAdapter
{
    protected final E graphics;
    
    protected AbstractGraphicsAdapter(E graphics)
    {
        this.graphics = Objects.requireNonNull(graphics);
    }
}