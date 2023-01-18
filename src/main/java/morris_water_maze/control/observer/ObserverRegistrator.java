package morris_water_maze.control.observer;

import morris_water_maze.graphics.painter.image.ImagePainter;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.parameter.ParameterProvider;
import morris_water_maze.report.FileNameProvider;
import morris_water_maze.report.ImageFileCreator;
import morris_water_maze.report.ImageFileParameter;
import morris_water_maze.report.ReportWriter;
import morris_water_maze.report.SimulationProgressReporter;
import morris_water_maze.report.histogram.HistogramFileMaker;
import morris_water_maze.report.histogram.HistogramFileMakerFactory;


public final class ObserverRegistrator
{
    private final ParameterProvider
        parameterProvider;
    
    private final FileNameProvider
        fileNameProvider;
    
    private final ImageFileParameter
        imageFileParameter;
    
    
    public ObserverRegistrator(ParameterProvider parameterProvider)
    {
        this.parameterProvider = parameterProvider;
        this.fileNameProvider = parameterProvider.getFileNameProvider();
        this.imageFileParameter = parameterProvider.getImageFileParameterProvider();
    }
        
    public void registerObserversFor(Simulation simulation)
    {
        registerSimulationSeriesCompletionObserversFor(simulation);
        registerSimulationStepObserversFor(simulation);
    }
    
    private void registerSimulationSeriesCompletionObserversFor(Simulation simulation)
    {
        ReportWriter reportWriter = new ReportWriter(fileNameProvider);
        simulation.registerSimulationSeriesCompletionObservers(reportWriter);
    
        HistogramFileMakerFactory histogramFileMakerFactory = new HistogramFileMakerFactory(parameterProvider.getHistogramParameterProvider(), fileNameProvider);
        HistogramFileMaker histogramFileMaker = histogramFileMakerFactory.makeHistogramFileCreator();
        simulation.registerSimulationSeriesCompletionObservers(histogramFileMaker);
    }
    
    private void registerSimulationStepObserversFor(Simulation simulation)
    {
        if(isSimulationProgressToBeReported())
        {
            SimulationProgressReporter simulationProgressReporter = new SimulationProgressReporter();
            simulation.registerSimulationStepObservers(simulationProgressReporter);
        }
        
        if(areMouseTrajectoryImagesToBeCreated())
        {
            ImagePainter imagePainterForImageFileCreator = makeInstanceOfImagePainterForImageFileCreator();
            ImageFileCreator imageFileCreator = new ImageFileCreator(imagePainterForImageFileCreator, imageFileParameter, fileNameProvider);
            simulation.registerSimulationStepObservers(imageFileCreator);
        }
    }
    
    private boolean isSimulationProgressToBeReported()
    {
        return parameterProvider.getSimulationParameterProvider()
                                .isReportingSimulationProgress();
    }
    
    private boolean areMouseTrajectoryImagesToBeCreated()
    {
        return imageFileParameter.getNumberOfPics() > 0;
    }
    
    private ImagePainter makeInstanceOfImagePainterForImageFileCreator()
    {
        return imageFileParameter.getImagePainterTypeForPictureExport()
                                 .makeInstance();
    }
}
