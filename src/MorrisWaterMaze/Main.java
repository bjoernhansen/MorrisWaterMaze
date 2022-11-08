package MorrisWaterMaze;

import MorrisWaterMaze.control.FileNameProvider;
import MorrisWaterMaze.control.report.ReportWriter;
import MorrisWaterMaze.control.ImageFileCreator;
import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.control.SimulationControllerFactory;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.graphics.painter.ImagePainterImplementation;
import MorrisWaterMaze.model.Background;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.model.simulation.Simulation;
import MorrisWaterMaze.model.simulation.WaterMorrisMazeSimulation;
import MorrisWaterMaze.parameter.ParameterAccessor;
import MorrisWaterMaze.parameter.ParameterSource;
import MorrisWaterMaze.util.DirectoryCreator;


public final class Main
{
	private static final ParameterSource
		PARAMETER_SOURCE = ParameterSource.PROPERTIES_FILE;
	
	public static final double
		ZOOM_FACTOR = 4.0;
		
	private static final int
		IMAGE_SIZE = (int) (2.0 * ZOOM_FACTOR * Pool.CENTER_TO_BORDER_DISTANCE);
	
	private static final int
		BACKGROUND_SIDE_LENGTH = (int) ZOOM_FACTOR * IMAGE_SIZE;
	
	
	private Main()
	{
		throw new UnsupportedOperationException();
	}
	
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = PARAMETER_SOURCE.makeParameterAccessorInstance(args);
		FileNameProvider fileNameProvider = new FileNameProvider(parameterAccessor);
		createDirectories(fileNameProvider);
		ImagePainter imagePainter = makeImagePainter();
		
		ImageFileCreator imageFileCreator = new ImageFileCreator(imagePainter, parameterAccessor, fileNameProvider);
		ReportWriter reportWriter = new ReportWriter(fileNameProvider);
		
		Simulation simulation = new WaterMorrisMazeSimulation(parameterAccessor);
		simulation.registerSimulationStepObservers(imageFileCreator);
		simulation.registerSimulationCompletionObservers(reportWriter);
		
		SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterAccessor, imagePainter);
		simulationController.start();
    }
	
	private static void createDirectories(FileNameProvider fileNameProvider)
	{
		DirectoryCreator directoryCreator = new DirectoryCreator(fileNameProvider);
		directoryCreator.makeDirectories();
	}
	
	private static ImagePainter makeImagePainter()
	{
		Paintable background = new Background(BACKGROUND_SIDE_LENGTH);
		return ImagePainterImplementation.newInstanceWithBackground(IMAGE_SIZE, background);
	}
}
