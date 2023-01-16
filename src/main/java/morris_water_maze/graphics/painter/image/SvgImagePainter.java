package morris_water_maze.graphics.painter.image;

import morris_water_maze.graphics.adapter.Graphics2dAdapter;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.Graphics;
import java.awt.Image;


final class SvgImagePainter extends AbstractImagePainter
{
    SvgImagePainter(int imageSize)
    {
        Graphics svgGraphics2D = new SVGGraphics2D(imageSize, imageSize);
        GraphicsAdapter graphics = Graphics2dAdapter.of(svgGraphics2D);
        
        setGraphics(graphics);
    }
    
    @Override
    public Image getImage()
    {
        throw new UnsupportedOperationException();
    }
}
