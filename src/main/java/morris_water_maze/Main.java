package morris_water_maze;

import morris_water_maze.control.SimulationController;
import morris_water_maze.control.SimulationControllerFactory;
import morris_water_maze.control.gui.GuiType;
import morris_water_maze.graphics.painter.image.ImagePainter;
import morris_water_maze.model.Pool;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.model.simulation.WaterMorrisMazeSimulation;
import morris_water_maze.parameter.ParameterAccessor;
import morris_water_maze.parameter.ParameterAccessorFromPropertiesFile;
import morris_water_maze.report.FileNameProvider;
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
        ParameterAccessor parameterAccessor = new ParameterAccessorFromPropertiesFile();
        FileNameProvider fileNameProvider = new FileNameProvider(parameterAccessor);
        createDirectories(fileNameProvider);
    
        Simulation simulation = getSimulationWithObserversRegistered(parameterAccessor, fileNameProvider);
        
        SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterAccessor);
        simulationController.start();
    }
    
    private static Simulation getSimulationWithObserversRegistered(ParameterAccessor parameterAccessor, FileNameProvider fileNameProvider)
    {
        Simulation simulation = new WaterMorrisMazeSimulation(parameterAccessor);
        
        ReportWriter reportWriter = new ReportWriter(fileNameProvider);
        simulation.registerSimulationSeriesCompletionObservers(reportWriter);
    
        HistogramFileMakerFactory histogramFileMakerFactory = new HistogramFileMakerFactory(parameterAccessor.getHistogramParameterAccessor(), fileNameProvider);
        HistogramFileMaker histogramFileMaker = histogramFileMakerFactory.makeHistogramFileCreator();
        simulation.registerSimulationSeriesCompletionObservers(histogramFileMaker);
    
        SimulationProgressReporter simulationProgressReporter = new SimulationProgressReporter();
        simulation.registerSimulationStepObservers(simulationProgressReporter);
        
        if(parameterAccessor.getNumberOfPics() > 0)
        {
            ImagePainter imagePainterForImageFileCreator = parameterAccessor.getImagePainterTypeForPictureExport()
                                                                            .makeInstance();
            ImageFileCreator imageFileCreator = new ImageFileCreator(imagePainterForImageFileCreator, parameterAccessor, fileNameProvider);
            simulation.registerSimulationStepObservers(imageFileCreator);
        }
        
        return simulation;
    }
    
    private static void createDirectories(FileNameProvider fileNameProvider)
    {
        DirectoryCreator directoryCreator = new DirectoryCreator(fileNameProvider);
        directoryCreator.makeDirectories();
    }
}
