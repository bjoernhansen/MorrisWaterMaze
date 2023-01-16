package morris_water_maze.graphics.painter.image;

import morris_water_maze.model.Pool;
import morris_water_maze.model.SquareBackground;
import morris_water_maze.report.ImageFileFormat;

import java.util.function.Function;

import static morris_water_maze.Main.IMAGE_SIZE;


public enum ImagePainterType implements ImagePainterFactory
{
    DEFAULT(DefaultImagePainter::new, ImageFileFormat.PNG),
    SVG(SvgImagePainter::new, ImageFileFormat.SVG);
    
    
    private static final SquareBackground
        squareBackground = new SquareBackground(2.0 * Pool.CENTER_TO_BORDER_DISTANCE);
    
    private final Function<Integer, ImagePainter>
        instantiationFunction;
    
    private final ImageFileFormat
        imageFileFormat;
    
    
    ImagePainterType(Function<Integer, ImagePainter> instantiationFunction, ImageFileFormat imageFileFormat)
    {
        this.instantiationFunction = instantiationFunction;
        this.imageFileFormat = imageFileFormat;
    }
    
    @Override
    public ImagePainter makeInstance()
    {
        ImagePainter imagePainter = instantiationFunction.apply(IMAGE_SIZE);
        imagePainter.setBackground(squareBackground);
        imagePainter.initializeImage();
        return imagePainter;
    }
    
    public ImageFileFormat getImageFileFormat()
    {
        return imageFileFormat;
    }
}
