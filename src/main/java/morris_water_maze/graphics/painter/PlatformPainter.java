package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.Color;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.Platform;


final class PlatformPainter extends Painter<Platform>
{
    @Override
    public void paint(GraphicsAdapter graphics, Platform platform)
    {
        graphics.setColor(Color.DARK_GREY);
        graphics.fillRect(platform.getBounds());
    }
}
