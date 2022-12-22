package morris_water_maze.report;

import morris_water_maze.control.observer.SimulationSeriesCompletionObserver;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterAccessor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.statistics.HistogramDataset;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class HistogramCreator implements SimulationSeriesCompletionObserver
{
    private static final int
        DEFAULT_NUMBER_OF_BINS = 20;
    
    private Simulation
        simulation;
    
    private final String
        subtitle;
    
    private final String
        histogramImagePath;
    
    private final DecimalFormat
        decimalFormat = new DecimalFormat("###,###,###");
    
    
    public HistogramCreator(ParameterAccessor parameterAccessor, FileNameProvider fileNameProvider)
    {
        this.subtitle = createSubtitleName(parameterAccessor);
        this.histogramImagePath = fileNameProvider.getHistogramImagePath();
    }
    
    @Override
    public void beNotifiedAboutEndOfAllSimulations()
    {
        try
        {
            createHistogram();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void createHistogram() throws IOException
    {
        List<Double> listOfSearchTimes = new ArrayList<>();
        
        simulation.getSearchTimeProvider()
                  .getSearchTimes()
                  .stream()
                  .filter(d -> 25 < d && d < 250)
                  .forEach(listOfSearchTimes::add);
       
        double[] searchTimes = getSearchTimeArrayFrom(listOfSearchTimes);
        int numberOfBins = calculateNumberOfBins(listOfSearchTimes);
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", searchTimes, numberOfBins);
        
        JFreeChart histogram = ChartFactory.createHistogram("Frequency distribution of the duration of search times",
            "Search time duration", "Number of attempts", dataset);
        
        TextTitle title = new TextTitle(subtitle);
        histogram.setSubtitles(List.of(title));
        ChartUtils.saveChartAsPNG(new File(histogramImagePath), histogram, 600, 400);
    }
    
    private String createSubtitleName(ParameterAccessor parameterAccessor)
    {
        String numberOfSimulations = decimalFormat.format(parameterAccessor.getNumberOfSimulations());
        
        return String.format(
            Locale.ENGLISH,
            "Histogram for a %s trials with mouse training level %.2f",
            numberOfSimulations,
            parameterAccessor.getMouseTrainingLevel());
    }
    
    private double[] getSearchTimeArrayFrom(List<Double> listOfSearchTimes)
    {
        double[] searchTimes = new double[listOfSearchTimes.size()];
        Arrays.setAll(searchTimes, listOfSearchTimes::get);
        return searchTimes;
    }
    
    private int calculateNumberOfBins(List<Double> listOfSearchTimes)
    {
        return (int) listOfSearchTimes.stream()
                                      .mapToDouble(value -> value)
                                      .max()
                                      .orElse(DEFAULT_NUMBER_OF_BINS);
    }
    
    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }
}
