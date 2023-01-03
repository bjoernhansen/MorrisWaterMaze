package morris_water_maze.report.histogram;

import morris_water_maze.model.simulation.SearchTimeProvider;
import morris_water_maze.parameter.ParameterAccessor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.statistics.HistogramDataset;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


final class HistogramCreator
{
    private static final int
        MAXIMUM_DISPLAYED_SEARCH_TIME_DURATION = 320;
    
    private static final int
        BINS_PER_SECOND = 5;
        
    private final String
        subtitle;
    
    private final DecimalFormat
        decimalFormat = new DecimalFormat("###,###,###");
    
    
    
    HistogramCreator(ParameterAccessor parameterAccessor)
    {
        this.subtitle = createSubtitleName(parameterAccessor);
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
    
    JFreeChart createHistogram(SearchTimeProvider searchTimeProvider)
    {
        List<Double> listOfSearchTimes = searchTimeProvider.getSearchTimes()
                                                           .stream()
                                                           .filter(this::isValidSearchTimeForHistogram)
                                                           .collect(Collectors.toList());
        
        double[] searchTimes = getSearchTimeArrayFrom(listOfSearchTimes);
        
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", searchTimes, getNumberOfBins());
        
        JFreeChart histogram = ChartFactory.createHistogram(
            "Frequency distribution of the duration of search times",
            "Search time duration",
            "Number of attempts",
            dataset);
        
        TextTitle title = new TextTitle(subtitle);
        histogram.setSubtitles(List.of(title));
        
        return histogram;
    }
    
    private boolean isValidSearchTimeForHistogram(double value)
    {
        return value <= MAXIMUM_DISPLAYED_SEARCH_TIME_DURATION;
    }
    
    private double[] getSearchTimeArrayFrom(List<Double> listOfSearchTimes)
    {
        double[] searchTimes = new double[listOfSearchTimes.size()];
        Arrays.setAll(searchTimes, listOfSearchTimes::get);
        return searchTimes;
    }
    
    private int getNumberOfBins()
    {
        return BINS_PER_SECOND * MAXIMUM_DISPLAYED_SEARCH_TIME_DURATION;
    }
}
