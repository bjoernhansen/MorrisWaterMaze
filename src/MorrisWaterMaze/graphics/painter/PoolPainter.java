package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.model.Pool;

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
