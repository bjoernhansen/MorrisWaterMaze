package morris_water_maze;

import morris_water_maze.control.SimulationController;
import morris_water_maze.control.SimulationControllerFactory;
import morris_water_maze.control.gui.GuiType;
import morris_water_maze.graphics.painter.image.ImagePainter;
import morris_water_maze.model.Pool;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.model.simulation.WaterMorrisMazeSimulation;
import morris_water_maze.parameter.ParameterProviderGenerator;
import morris_water_maze.parameter.SimulationParameterProvider;
import morris_water_maze.report.FileNameProvider;
import morris_water_maze.report.ImageFileParameterProvider;
import morris_water_maze.report.SimulationProgressReporter;
import morris_water_maze.report.histogram.HistogramFileMaker;
import morris_water_maze.report.histogram.HistogramFileMakerFactory;
import morris_water_maze.report.ImageFileCreator;
import morris_water_maze.report.ReportWriter;
import morris_water_maze.util.DirectoryCreator;


public final class Main
{
    public static final GuiType
        GUI_TYPE = GuiType.SWING;
    
    public static final double
        ZOOM_FACTOR = 4.0;
    
    public static final int
        IMAGE_SIZE = (int) (ZOOM_FACTOR * 2.0 * Pool.CENTER_TO_BORDER_DISTANCE);
    
    
    private Main()
    {
        throw new UnsupportedOperationException();
    }
    
    public static void main(String[] args)
    {
        ParameterProviderGenerator parameterProviderGenerator = new ParameterProviderGenerator();
        SimulationParameterProvider simulationParameterProvider = parameterProviderGenerator.getSimulationParameterProvider();
        String simulationId = simulationParameterProvider.getSimulationId();
        FileNameProvider fileNameProvider = new FileNameProvider(simulationId);
        createDirectories(fileNameProvider);
        
        Simulation simulation = getSimulationWithObserversRegistered(parameterProviderGenerator, fileNameProvider);
        
        SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterProviderGenerator);
        simulationController.start();
    }
    
    private static Simulation getSimulationWithObserversRegistered(ParameterProviderGenerator parameterProviderGenerator, FileNameProvider fileNameProvider)
    {
        Simulation simulation = new WaterMorrisMazeSimulation(parameterProviderGenerator);
        
        ReportWriter reportWriter = new ReportWriter(fileNameProvider);
        simulation.registerSimulationSeriesCompletionObservers(reportWriter);
        
        HistogramFileMakerFactory histogramFileMakerFactory = new HistogramFileMakerFactory(parameterProviderGenerator.getHistogramParameterProvider(), fileNameProvider);
        HistogramFileMaker histogramFileMaker = histogramFileMakerFactory.makeHistogramFileCreator();
        simulation.registerSimulationSeriesCompletionObservers(histogramFileMaker);
        
        SimulationProgressReporter simulationProgressReporter = new SimulationProgressReporter();
        simulation.registerSimulationStepObservers(simulationProgressReporter);
        
        ImageFileParameterProvider imageFileParameterProvider = parameterProviderGenerator.getImageFileParameterProvider();
        if(areMouseTrajectoryImagesToBeCreated(imageFileParameterProvider))
        {
            ImagePainter imagePainterForImageFileCreator = makeInstanceOfImagePainterForImageFileCreator(imageFileParameterProvider);
            ImageFileCreator imageFileCreator = new ImageFileCreator(imagePainterForImageFileCreator, imageFileParameterProvider, fileNameProvider);
            simulation.registerSimulationStepObservers(imageFileCreator);
        }
        
        return simulation;
    }
    
    private static boolean areMouseTrajectoryImagesToBeCreated(ImageFileParameterProvider imageFileParameterProvider)
    {
        return imageFileParameterProvider.getNumberOfPics() > 0;
    }
    
    private static ImagePainter makeInstanceOfImagePainterForImageFileCreator(ImageFileParameterProvider imageFileParameterProvider)
    {
        return imageFileParameterProvider.getImagePainterTypeForPictureExport()
                                         .makeInstance();
    }
    
    private static void createDirectories(FileNameProvider fileNameProvider)
    {
        DirectoryCreator directoryCreator = new DirectoryCreator(fileNameProvider);
        directoryCreator.makeDirectories();
    }
}
