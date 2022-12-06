package morris_water_maze;

import morris_water_maze.control.SimulationController;
import morris_water_maze.control.SimulationControllerFactory;
import morris_water_maze.graphics.painter.image.ImagePainter;
import morris_water_maze.graphics.painter.image.ImagePainterType;
import morris_water_maze.model.Pool;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.model.simulation.WaterMorrisMazeSimulation;
import morris_water_maze.parameter.ParameterAccessor;
import morris_water_maze.parameter.ParameterSource;
import morris_water_maze.report.FileNameProvider;
import morris_water_maze.report.ImageFileCreator;
import morris_water_maze.report.ReportWriter;
import morris_water_maze.util.DirectoryCreator;


public final class Main
{
    public static final double
        ZOOM_FACTOR = 4.0;
    
    public static final int
        IMAGE_SIZE = (int) (2.0 * ZOOM_FACTOR * Pool.CENTER_TO_BORDER_DISTANCE);
    
    private static final ParameterSource
        PARAMETER_SOURCE = ParameterSource.PROPERTIES_FILE;
    
    
    private Main()
    {
        throw new UnsupportedOperationException();
    }
    
    public static void main(String[] args)
    {
        ParameterAccessor parameterAccessor = PARAMETER_SOURCE.makeParameterAccessorInstance(args);
        FileNameProvider fileNameProvider = new FileNameProvider(parameterAccessor);
        createDirectories(fileNameProvider);
        
        // TODO wenn die Bild-Erstellung nicht gewünscht ist müssen manche Klassen gar nicht erst erstellt werden
        
        
        ImagePainterType imagePainterType = parameterAccessor.getImagePainterTypeForPictureExport();
        ImagePainter imagePainterForFileCreator = imagePainterType.makeInstance();
        ImageFileCreator imageFileCreator = new ImageFileCreator(imagePainterForFileCreator, parameterAccessor, fileNameProvider);
        
        ReportWriter reportWriter = new ReportWriter(fileNameProvider);
        
        Simulation simulation = new WaterMorrisMazeSimulation(parameterAccessor);
        simulation.registerSimulationStepObservers(imageFileCreator);
        simulation.registerSimulationSeriesCompletionObservers(reportWriter);
        
        SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterAccessor);
        simulationController.start();
    }
    
    private static void createDirectories(FileNameProvider fileNameProvider)
    {
        DirectoryCreator directoryCreator = new DirectoryCreator(fileNameProvider);
        directoryCreator.makeDirectories();
    }
}
