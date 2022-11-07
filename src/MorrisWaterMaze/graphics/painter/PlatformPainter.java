package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.model.Platform;
import MorrisWaterMaze.model.Pool;

import java.awt.Color;


final class PlatformPainter extends Painter<Platform>
{
    private static final Color
        DARK_GREY = new Color(75, 75, 75);
    
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, Platform platform)
    {
        graphicsAdapter.setColor(DARK_GREY);
        graphicsAdapter.fill(affineTransformation.createTransformedShape(platform.getBounds()));
        graphicsAdapter.setColor(Color.black);
    
        int y;
        int x = y = (int)(ZOOM_FACTOR * (Pool.CENTER_TO_BORDER_DISTANCE - 1));
        int size = (int) (2.0 * ZOOM_FACTOR);
    
        graphicsAdapter.fillOval(x, y, size, size);
    }
}
