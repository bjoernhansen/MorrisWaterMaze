package morris_water_maze.report.histogram;

import morris_water_maze.report.FileNameProvider;
import morris_water_maze.report.ImageFileFormat;


public final class HistogramFileMakerFactory
{
    private final HistogramParameterProvider
        histogramParameterProvider;
    
    private final FileNameProvider
        fileNameProvider;
    
    private final ImageFileFormat
        imageFileFormat;
    
    
    public HistogramFileMakerFactory(HistogramParameterProvider histogramParameterProvider, FileNameProvider fileNameProvider)
    {
        this.histogramParameterProvider = histogramParameterProvider;
        this.fileNameProvider = fileNameProvider;
        this.imageFileFormat = histogramParameterProvider.getImageFileFormat();
    }
    
    public HistogramFileMaker makeHistogramFileCreator()
    {
        String subDirectoryPath = fileNameProvider.getSubDirectory();
        if (imageFileFormat == ImageFileFormat.SVG)
        {
            return new SvgHistogramFileMaker(histogramParameterProvider, subDirectoryPath);
        }
        return new PngHistogramFileMaker(histogramParameterProvider, subDirectoryPath);
    }
}
