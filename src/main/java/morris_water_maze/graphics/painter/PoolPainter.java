package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.Pool;

import static morris_water_maze.Main.ZOOM_FACTOR;


final class PoolPainter extends Painter<Pool>
{
    private final int
        POOL_CENTER_SIZE = 2;
    
    
    @Override
    public void paint(GraphicsAdapter graphics, Pool pool)
    {
        graphics.setColor(Color.BLACK);
        graphics.drawOval(pool.getBorder());
        drawPoolCenter(graphics);
    }
    
    private void drawPoolCenter(GraphicsAdapter graphics)
    {        
        int y;
        int x = y = (int)(Pool.CENTER_TO_BORDER_DISTANCE - POOL_CENTER_SIZE/2.0);
    
        graphics.setColor(Color.BLACK);
        graphics.fillOval(x, y, POOL_CENTER_SIZE, POOL_CENTER_SIZE);
    }
}
