package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.Platform;

import java.awt.geom.Rectangle2D;

import static morris_water_maze.Main.ZOOM_FACTOR;


final class PlatformPainter extends Painter<Platform>
{
    @Override
    public void paint(GraphicsAdapter graphics, Platform platform)
    {
        Rectangle2D platformBounds = platform.getBounds();
    
        graphics.setColor(Color.DARK_GREY);
        graphics.fillRect(
            (int)(ZOOM_FACTOR * platformBounds.getX()),
            (int)(ZOOM_FACTOR * platformBounds.getY()),
            (int)(ZOOM_FACTOR * platformBounds.getWidth()),
            (int)(ZOOM_FACTOR * platformBounds.getHeight()));
    }
}
