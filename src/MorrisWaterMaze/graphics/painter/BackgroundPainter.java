package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.model.Background;

import java.awt.Color;


public final class BackgroundPainter extends Painter<Background>
{
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, Background background)
    {
        graphicsAdapter.setColor(Color.white);
        graphicsAdapter.fillRect(0, 0, background.getWidth(), background.getHeight());
    }
}
