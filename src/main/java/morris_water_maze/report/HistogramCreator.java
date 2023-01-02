package morris_water_maze.report;

import morris_water_maze.control.observer.SimulationSeriesCompletionObserver;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterAccessor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;





public class HistogramCreator implements SimulationSeriesCompletionObserver
{
    private static final int
        DEFAULT_NUMBER_OF_BINS = 20;
    
    private static final int
        MAXIMUM_DISPLAYED_SEARCH_TIME_DURATION = 320;
    
    private static final int
        FREQUENCY_CAP = 500;
    
    private Simulation
        simulation;
    
    private final String
        subtitle;
    
    private final String
        histogramImagePath;
    
    private final DecimalFormat
        decimalFormat = new DecimalFormat("###,###,###");
    
    private Map<Integer, Integer>
        countMap = new HashMap<>();
    
    
    public HistogramCreator(ParameterAccessor parameterAccessor, FileNameProvider fileNameProvider)
    {
        subtitle = createSubtitleName(parameterAccessor);
        histogramImagePath = fileNameProvider.getHistogramImagePath();
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
                  .filter(this::isValidSearchTimeForHistogram)
                  .forEach(listOfSearchTimes::add);
    
        /*
        double max = listOfSearchTimes.stream()
                                              .mapToDouble(d -> d)
                                              .max().orElse(0.0);
        
        double min = listOfSearchTimes.stream()
                                      .mapToDouble(d -> d)
                                      .min().orElse(0.0);
           
       */
        int numberOfBins = calculateNumberOfBins(listOfSearchTimes);
        /*
        
        double binWidth = (max - min) / numberOfBins;
        */
        double[] searchTimes = getSearchTimeArrayFrom(listOfSearchTimes);
        
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", searchTimes, numberOfBins);
        
        JFreeChart histogram = ChartFactory.createHistogram("Frequency distribution of the duration of search times",
            "Search time duration", "Number of attempts", dataset);
        
        TextTitle title = new TextTitle(subtitle);
        histogram.setSubtitles(List.of(title));
        System.out.println("\nSchreibe Datei: " + histogramImagePath);
        
        
        
        
        
        ChartUtils.saveChartAsPNG(new File(histogramImagePath), histogram, 600, 400);
    
    
        final SVGGraphics2D svg2d = new SVGGraphics2D(600, 400);
        histogram.draw(svg2d,new Rectangle2D.Double(0, 0, 600, 400));
        
        String fileNameTemp = histogramImagePath + ".svg";
        System.out.println("\nSchreibe Datei: " + fileNameTemp);
    
        File file = new File(fileNameTemp);
        
        String svgString = svg2d.getSVGElement();
        SVGUtils.writeToSVG(file, svgString, false);
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
        return MAXIMUM_DISPLAYED_SEARCH_TIME_DURATION;/*(int) listOfSearchTimes.stream()
                                      .mapToDouble(value -> value)
                                      .max()
                                      .orElse(DEFAULT_NUMBER_OF_BINS);*/
    }
    
    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }
    
    boolean isValidSearchTimeForHistogram(double value)
    {
        //int binIndex = (int)Math.floor(value);
        if(value > MAXIMUM_DISPLAYED_SEARCH_TIME_DURATION /*|| getFromCountMap(binIndex) >= FREQUENCY_CAP*/)
        {
            return false;
        }
        //countMap.put(binIndex, getFromCountMap(binIndex)+1);
        return true;
    }
    
    private Integer getFromCountMap(int binIndex)
    {
        return Optional.ofNullable(countMap.get(binIndex)).orElse(0);
    }
}
