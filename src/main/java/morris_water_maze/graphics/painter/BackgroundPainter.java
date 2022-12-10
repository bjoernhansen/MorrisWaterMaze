package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.SquareBackground;

import static morris_water_maze.Main.ZOOM_FACTOR;


public final class BackgroundPainter extends Painter<SquareBackground>
{
    @Override
    public void paint(GraphicsAdapter graphics, SquareBackground squareBackground)
    {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(
            (int)(ZOOM_FACTOR * 0.0),
            (int)(ZOOM_FACTOR * 0.0),
            (int)(ZOOM_FACTOR*squareBackground.getSideLength()),
            (int)(ZOOM_FACTOR*squareBackground.getSideLength()));
    }
}
