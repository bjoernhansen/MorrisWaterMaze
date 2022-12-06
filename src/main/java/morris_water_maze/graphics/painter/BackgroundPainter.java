package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.model.SquareBackground;

import java.awt.Color;


public final class BackgroundPainter extends Painter<SquareBackground>
{
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, SquareBackground squareBackground)
    {
        graphicsAdapter.setColor(Color.white);
        graphicsAdapter.fillRect(0, 0, 720, squareBackground.getSideLength());
    }
}
