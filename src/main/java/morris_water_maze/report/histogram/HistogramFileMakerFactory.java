package morris_water_maze.report.histogram;

import morris_water_maze.report.FileNameProvider;
import morris_water_maze.report.ImageFileFormat;


public final class HistogramFileMakerFactory
{
    private final HistogramParameterAccessor
        parameterAccessor;
    
    private final FileNameProvider
        fileNameProvider;
    
    private final ImageFileFormat
        imageFileFormat;
    
    
    public HistogramFileMakerFactory(HistogramParameterAccessor parameterAccessor, FileNameProvider fileNameProvider)
    {
        this.parameterAccessor = parameterAccessor;
        this.fileNameProvider = fileNameProvider;
        this.imageFileFormat = parameterAccessor.getImageFileFormat();
    }
    
    public HistogramFileMaker makeHistogramFileCreator()
    {
        String subDirectoryPath = fileNameProvider.getSubDirectory();
        if (imageFileFormat == ImageFileFormat.SVG)
        {
            return new SvgHistogramFileMaker(parameterAccessor, subDirectoryPath);
        }
        return new PngHistogramFileMaker(parameterAccessor, subDirectoryPath);
    }
}
