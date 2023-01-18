package morris_water_maze.report.histogram;

import morris_water_maze.report.FileNameProvider;
import morris_water_maze.report.ImageFileFormat;


public final class HistogramFileMakerFactory
{
    private final HistogramParameter
        histogramParameter;
    
    private final FileNameProvider
        fileNameProvider;
    
    private final ImageFileFormat
        imageFileFormat;
    
    
    public HistogramFileMakerFactory(HistogramParameter histogramParameter, FileNameProvider fileNameProvider)
    {
        this.histogramParameter = histogramParameter;
        this.fileNameProvider = fileNameProvider;
        this.imageFileFormat = histogramParameter.getImageFileFormat();
    }
    
    public HistogramFileMaker makeHistogramFileCreator()
    {
        String subDirectoryPath = fileNameProvider.getSubDirectory();
        if (imageFileFormat == ImageFileFormat.SVG)
        {
            return new SvgHistogramFileMaker(histogramParameter, subDirectoryPath);
        }
        return new PngHistogramFileMaker(histogramParameter, subDirectoryPath);
    }
}
