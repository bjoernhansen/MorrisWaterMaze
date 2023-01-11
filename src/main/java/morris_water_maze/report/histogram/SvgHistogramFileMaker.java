package morris_water_maze.report.histogram;

import morris_water_maze.report.ImageFileFormat;
import org.jfree.chart.JFreeChart;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;


final class SvgHistogramFileMaker extends HistogramFileMaker
{
    private static final ImageFileFormat
        IMAGE_FILE_FORMAT = ImageFileFormat.SVG;
    
    
    SvgHistogramFileMaker(HistogramParameterProvider parameterAccessor, String subDirectory)
    {
        super(parameterAccessor, subDirectory);
    }
    
    @Override
    void writeHistogramFile(JFreeChart histogram) throws IOException
    {
        final SVGGraphics2D svg2d = new SVGGraphics2D(HISTOGRAM_DIMENSION.getWidth(), HISTOGRAM_DIMENSION.getHeight());
        Rectangle2D.Double area = new Rectangle2D.Double(0,
                                                         0,
                                                         HISTOGRAM_DIMENSION.getWidth(),
                                                         HISTOGRAM_DIMENSION.getHeight());
        histogram.draw(svg2d, area);
        
        File file = new File(getHistogramImagePath());
        String svgString = svg2d.getSVGElement();
        SVGUtils.writeToSVG(file, svgString, false);
    }

    @Override
    String getImageFileExtension()
    {
        return IMAGE_FILE_FORMAT.getFileExtension();
    }
}
