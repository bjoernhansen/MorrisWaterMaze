package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.SquareBackground;


public final class BackgroundPainter extends Painter<SquareBackground>
{
    @Override
    public void paint(GraphicsAdapter graphics, SquareBackground squareBackground)
    {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(squareBackground.getBounds());
    }
}
