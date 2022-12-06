package morris_water_maze.graphics.painter.image;

import morris_water_maze.graphics.Graphics2dAdapter;
import morris_water_maze.graphics.GraphicsAdapter;

import java.awt.Image;
import java.awt.image.BufferedImage;


final class DefaultImagePainter extends AbstractImagePainter
{
    DefaultImagePainter(int imageSize)
    {
        Image image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        GraphicsAdapter graphicsAdapter = Graphics2dAdapter.of(image);
        graphicsAdapter.turnAntialiasingOn();
        
        setImage(image);
        setGraphicsAdapter(graphicsAdapter);
    }
}

