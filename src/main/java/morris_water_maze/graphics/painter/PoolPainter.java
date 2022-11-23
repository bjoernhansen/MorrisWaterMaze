package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.model.Pool;

import java.awt.Color;


final class PoolPainter extends Painter<Pool>
{
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, Pool pool)
    {
        graphicsAdapter.setColor(Color.black);
        graphicsAdapter.draw(affineTransformation.createTransformedShape(pool.getBorder()));
    }
}
