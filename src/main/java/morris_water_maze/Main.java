package morris_water_maze;

import morris_water_maze.control.FileNameProvider;
import morris_water_maze.control.report.ReportWriter;
import morris_water_maze.control.ImageFileCreator;
import morris_water_maze.control.SimulationController;
import morris_water_maze.control.SimulationControllerFactory;
import morris_water_maze.graphics.painter.ImagePainter;
import morris_water_maze.graphics.painter.ImagePainterFactory;
import morris_water_maze.graphics.painter.ImagePainterType;
import morris_water_maze.model.Pool;
import morris_water_maze.model.simulation.Simulation;
import morris_water_maze.model.simulation.WaterMorrisMazeSimulation;
import morris_water_maze.parameter.ParameterAccessor;
import morris_water_maze.parameter.ParameterSource;
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
		
		// TODO es müssen zwei Klassen erstellt werden: SVGImagePainter und SVGImageFileCreator
		// TODO wenn die Bild-Erstellung nicht gewünscht ist müssen manche Klassen gar nicht erst erstellt werden
		
		ImagePainterFactory imagePainterFactory = new ImagePainterFactory(IMAGE_SIZE);
		ImagePainter imagePainterForFileCreator = imagePainterFactory.makeImagePainter(parameterAccessor.imagePainterTypeForPictureExport());
		ImageFileCreator imageFileCreator = new ImageFileCreator(imagePainterForFileCreator, parameterAccessor, fileNameProvider);
		
		ReportWriter reportWriter = new ReportWriter(fileNameProvider);
		
		Simulation simulation = new WaterMorrisMazeSimulation(parameterAccessor);
		simulation.registerSimulationStepObservers(imageFileCreator);
		simulation.registerSimulationSeriesCompletionObservers(reportWriter);
	
		// TODO falls ohne GUI gestartet wird, müssen auch manche Instanzen nicht erstellt werden
		ImagePainter imagePainterForAnimation = imagePainterFactory.makeImagePainter(ImagePainterType.DEFAULT);
		SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterAccessor, imagePainterForAnimation);
		simulationController.start();
    }
	
	private static void createDirectories(FileNameProvider fileNameProvider)
	{
		DirectoryCreator directoryCreator = new DirectoryCreator(fileNameProvider);
		directoryCreator.makeDirectories();
	}
}
