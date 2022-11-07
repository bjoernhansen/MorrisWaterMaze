package MorrisWaterMaze;

import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.control.SimulationControllerFactory;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.graphics.painter.ImagePainterImplementation;
import MorrisWaterMaze.model.Background;
import MorrisWaterMaze.model.Pool;
import MorrisWaterMaze.model.WaterMorrisMazeSimulation;
import MorrisWaterMaze.parameter.ParameterAccessor;
import MorrisWaterMaze.parameter.ParameterSource;


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
	
	
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = PARAMETER_SOURCE.makeParameterAccessorInstance(args);
		WaterMorrisMazeSimulation simulation = new WaterMorrisMazeSimulation(parameterAccessor);
		Paintable background = new Background(BACKGROUND_SIDE_LENGTH);
		ImagePainter imagePainter = ImagePainterImplementation.newInstanceWithBackground(IMAGE_SIZE, background);
		SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterAccessor, imagePainter);
		simulationController.start();
    }
}