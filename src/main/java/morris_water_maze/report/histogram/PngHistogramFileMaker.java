package morris_water_maze.report.histogram;

import morris_water_maze.report.ImageFileFormat;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;


final class PngHistogramFileMaker extends HistogramFileMaker
{
    private static final ImageFileFormat
        IMAGE_FILE_FORMAT = ImageFileFormat.PNG;
    
    
    PngHistogramFileMaker(HistogramParameterAccessor parameterAccessor, String subDirectory)
    {
        super(parameterAccessor, subDirectory);
    }
    
    @Override
    void writeHistogramFile(JFreeChart histogram) throws IOException
    {
        File file = new File(getHistogramImagePath());
        ChartUtils.saveChartAsPNG(
                    file,
                    histogram,
                    HISTOGRAM_DIMENSION.getWidth(),
                    HISTOGRAM_DIMENSION.getHeight());
    }
    
    @Override
    String getImageFileExtension()
    {
        return IMAGE_FILE_FORMAT.getFileExtension();
    }
}
