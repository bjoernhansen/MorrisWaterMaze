package morris_water_maze.graphics.painter.image;

import morris_water_maze.graphics.Graphics2dAdapter;
import morris_water_maze.graphics.GraphicsAdapter;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.Graphics;
import java.awt.Image;


final class SvgImagePainter extends AbstractImagePainter
{
    SvgImagePainter(int imageSize)
    {
        Graphics graphics = new SVGGraphics2D(imageSize, imageSize);
        GraphicsAdapter graphicsAdapter = Graphics2dAdapter.of(graphics);
        
        setGraphicsAdapter(graphicsAdapter);
    }
    
    @Override
    public Image getImage()
    {
        throw new UnsupportedOperationException();
    }
}
