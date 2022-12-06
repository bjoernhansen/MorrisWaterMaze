package morris_water_maze.graphics;

import java.util.Objects;


abstract class AbstractGraphicsAdapter<E> implements GraphicsAdapter
{
    protected final E graphics;
    
    protected AbstractGraphicsAdapter(E graphics)
    {
        this.graphics = Objects.requireNonNull(graphics);
    }
    
    @Override
    public String getSvgString()
    {
        throw new UnsupportedOperationException();
    }
}