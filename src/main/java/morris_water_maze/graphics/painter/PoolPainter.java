package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.Pool;
import morris_water_maze.util.geometry.Circle;
import morris_water_maze.util.geometry.Point;


final class PoolPainter extends Painter<Pool>
{
    private static final double
        POOL_CENTER_CIRCLE_RADIUS = 1.0;
    
    private static final Point
        POOL_CENTER_LOCATION = Point.newInstance(Pool.CENTER_TO_BORDER_DISTANCE, Pool.CENTER_TO_BORDER_DISTANCE);

    
    @Override
    public void paint(GraphicsAdapter graphics, Pool pool)
    {
        drawPoolBorder(graphics, pool.getBorder());
        drawPoolCenter(graphics);
    }
    
    private void drawPoolBorder(GraphicsAdapter graphics, Circle circle)
    {
        graphics.setColor(Color.BLACK);
        graphics.drawEllipse(circle);
    }
    
    private void drawPoolCenter(GraphicsAdapter graphics)
    {
        graphics.setColor(Color.BLACK);
        graphics.fillCircleOnTopOfAPoint(POOL_CENTER_LOCATION, POOL_CENTER_CIRCLE_RADIUS);
    }
}
