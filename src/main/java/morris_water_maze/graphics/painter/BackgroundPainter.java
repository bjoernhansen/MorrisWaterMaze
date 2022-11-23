package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.model.Background;

import java.awt.Color;


public final class BackgroundPainter extends Painter<Background>
{
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, Background background)
    {
        graphicsAdapter.setColor(Color.white);
        graphicsAdapter.fillRect(0, 0, 720, background.getHeight());
    }
}
