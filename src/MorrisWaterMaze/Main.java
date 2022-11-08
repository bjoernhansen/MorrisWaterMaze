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
	
	// TODO verbessern in kleinere Methoden auslagern
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = PARAMETER_SOURCE.makeParameterAccessorInstance(args);
		Simulation simulation = new WaterMorrisMazeSimulation(parameterAccessor);
		Paintable background = new Background(BACKGROUND_SIDE_LENGTH);
		ImagePainter imagePainter = ImagePainterImplementation.newInstanceWithBackground(IMAGE_SIZE, background);
	
		FileNameProvider fileNameProvider = new FileNameProvider(parameterAccessor);
		DirectoryCreator directoryCreator = new DirectoryCreator(fileNameProvider);
		directoryCreator.makeDirectories();
	
		ImageFileCreator imageFileCreator = new ImageFileCreator(imagePainter, parameterAccessor, fileNameProvider);
		simulation.registerSimulationStepObservers(imageFileCreator);
	
		ReportWriter reportWriter = new ReportWriter(fileNameProvider);
		simulation.registerSimulationCompletionObservers(reportWriter);
		
		SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterAccessor, imagePainter, fileNameProvider);
		simulationController.start();
    }
}
