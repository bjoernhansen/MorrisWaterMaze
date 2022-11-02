package MorrisWaterMaze;

import MorrisWaterMaze.control.SimulationProcessor;
import MorrisWaterMaze.parameter.ParameterAccessor;
import MorrisWaterMaze.parameter.ParameterSource;


public class Main
{
	private static final ParameterSource
		PARAMETER_SOURCE = ParameterSource.PROPERTIES_FILE;
	
	
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = PARAMETER_SOURCE.makeParameterAccessorInstance(args);
		SimulationProcessor simulationProcessor = new SimulationProcessor(parameterAccessor);
		simulationProcessor.run();
    }
}