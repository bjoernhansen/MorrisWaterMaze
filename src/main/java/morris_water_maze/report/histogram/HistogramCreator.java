package morris_water_maze.report.histogram;

import morris_water_maze.model.simulation.SearchTimeProvider;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


final class HistogramCreator
{
    private static final DecimalFormat
        decimalFormat = new DecimalFormat("###,###,###");
    
    private final double
        maximumDisplayedSearchTimeDuration;
    
    private final double
        binsPerSecond;
        
    private final String
        subtitle;
    
    private final boolean
        isPublishable;
    
    
    HistogramCreator(HistogramParameter histogramParameter)
    {
        binsPerSecond = histogramParameter.getBinsPerSecond();
        maximumDisplayedSearchTimeDuration = histogramParameter.getMaximumDisplayedSearchTimeDuration();
        isPublishable = histogramParameter.isPublishable();
        subtitle = createSubtitle(histogramParameter);
    }
    
    private String createTitle()
    {
        return isPublishable
            ? ""
            : "Frequency distribution of the duration of search times";
    }
    
    private String createSubtitle(HistogramParameter histogramParameter)
    {
        if(histogramParameter.isPublishable())
        {
            return "";
        }
        
        String numberOfSimulations = decimalFormat.format(histogramParameter.getNumberOfSimulations());
        
        return String.format(
            Locale.ENGLISH,
            "Histogram for a %s trials with mouse training level %.2f",
            numberOfSimulations,
            histogramParameter.getMouseTrainingLevel());
    }
    
    JFreeChart createHistogram(SearchTimeProvider searchTimeProvider)
    {
        List<Double> listOfSearchTimes = searchTimeProvider.getSearchTimes()
                                                           .stream()
                                                           .filter(this::isValidSearchTimeForHistogram)
                                                           .collect(Collectors.toList());
        
        /*GaussianPseudoSearchDurationProvider searchDurationProvider = new GaussianPseudoSearchDurationProvider();
        listOfSearchTimes = searchDurationProvider.getPseudoSearchTimeList();*/
        
        double[] searchTimes = getSearchTimeArrayFrom(listOfSearchTimes);
        
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", searchTimes, getNumberOfBins());
        
        String title = createTitle();
        
        JFreeChart histogram = ChartFactory.createHistogram(
            title,
            "Search duration",
            "Number of attempts",
            dataset);
        
        TextTitle subtitleTextTitle = new TextTitle(subtitle);
        histogram.setSubtitles(List.of(subtitleTextTitle));
        
        if(isPublishable)
        {
            histogram.getPlot().setBackgroundPaint(Color.WHITE);
        }
        
        return histogram;
    }
    
    private boolean isValidSearchTimeForHistogram(double value)
    {
        return value <= maximumDisplayedSearchTimeDuration;
    }
    
    private double[] getSearchTimeArrayFrom(List<Double> listOfSearchTimes)
    {
        double[] searchTimes = new double[listOfSearchTimes.size()];
        Arrays.setAll(searchTimes, listOfSearchTimes::get);
        return searchTimes;
    }
    
    private int getNumberOfBins()
    {
        return (int)(binsPerSecond * maximumDisplayedSearchTimeDuration);
    }
}
