package morris_water_maze.graphics.adapter;

import morris_water_maze.Main;

import java.util.Objects;


abstract class AbstractGraphicsAdapter<E> implements GraphicsAdapter
{
    static final double
        ZOOM_FACTOR = Main.ZOOM_FACTOR;
    
    protected final E
        graphics;
    
        
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