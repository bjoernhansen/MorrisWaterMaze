package MorrisWaterMaze;

import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.control.SimulationControllerFactory;
import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.graphics.painter.ImagePainterImplementation;
import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.parameter.ParameterAccessor;
import MorrisWaterMaze.parameter.ParameterSource;

import static MorrisWaterMaze.control.SimulationController.IMAGE_SIZE;


public final class Main
{
	private static final ParameterSource
		PARAMETER_SOURCE = ParameterSource.PROPERTIES_FILE;
	
	
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = PARAMETER_SOURCE.makeParameterAccessorInstance(args);
		Simulation simulation = new Simulation(parameterAccessor);
		ImagePainter imagePainter = new ImagePainterImplementation(IMAGE_SIZE);
		SimulationController simulationController = SimulationControllerFactory.newInstance(simulation, parameterAccessor, imagePainter);
		simulationController.start();
    }
}