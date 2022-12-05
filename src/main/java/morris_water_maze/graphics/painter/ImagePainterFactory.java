package morris_water_maze.graphics.painter;

import morris_water_maze.model.Background;

public class ImagePainterFactory
{
    private final Background
        background;
    
    
    public ImagePainterFactory(int squareImageSideLength)
    {
        background = new Background(squareImageSideLength);
    }
    
    public ImagePainter makeImagePainter(ImagePainterType imagePainterType)
    {
        if(imagePainterType == ImagePainterType.SVG)
        {
            return SvgImagePainter.newInstanceWithBackground(background.getSideLength(), background);
        }
        else
        {
            return DefaultImagePainter.newInstanceWithBackground(background.getSideLength(), background);
        }
    }
}
