package morris_water_maze.report.histogram;

import morris_water_maze.control.observer.SimulationSeriesCompletionObserver;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.util.geometry.Dimension;
import org.jfree.chart.JFreeChart;

import java.io.IOException;


public abstract class HistogramFileMaker implements SimulationSeriesCompletionObserver
{
    static final Dimension
        HISTOGRAM_DIMENSION = new Dimension(600, 400);
    
    private static final String
        FILE_NAME_PREFIX = "histogram";
    
  
    private final HistogramCreator
        histogramCreator;
    
    private final String
        histogramImagePath;
    
    private Simulation
        simulation;
    
    
    HistogramFileMaker(HistogramParameter histogramParameter, String subDirectory)
    {
        histogramCreator = new HistogramCreator(histogramParameter);
        histogramImagePath = subDirectory + FILE_NAME_PREFIX + getImageFileExtension();
    }
    
    @Override
    public void beNotifiedAboutEndOfAllSimulations()
    {
        try
        {
            createAndOutputHistogram();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void createAndOutputHistogram() throws IOException
    {
        JFreeChart histogram = createHistogram();
        reportHistogramFileCreation();
        writeHistogramFile(histogram);
    }
    
    private JFreeChart createHistogram()
    {
        return histogramCreator.createHistogram(simulation.getSearchTimeProvider());
    }
    
    final void reportHistogramFileCreation()
    {
        System.out.println("\nSchreibe Datei: " + histogramImagePath);
    }
    
    abstract void writeHistogramFile(JFreeChart histogram) throws IOException;
    
    abstract String getImageFileExtension();
    
    final String getHistogramImagePath()
    {
        return histogramImagePath;
    }
    
    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }
}
