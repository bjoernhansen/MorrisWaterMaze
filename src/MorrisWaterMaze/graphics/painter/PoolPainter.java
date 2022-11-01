package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.model.Pool;

import java.awt.Color;
import java.awt.Graphics2D;

class PoolPainter extends Painter<Pool>
{
    @Override
    public void paint(Graphics2D g2d, Pool pool)
    {
        g2d.setColor(Color.black);
        g2d.draw(affineTransformation.createTransformedShape(pool.getBorder()));
    }
}
