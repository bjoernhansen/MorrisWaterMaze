package MorrisWaterMaze;

import MorrisWaterMaze.parameter.ParameterAccessor;
import MorrisWaterMaze.parameter.ParameterAccessorFromArgs;
import MorrisWaterMaze.parameter.ParameterAccessorFromPropertiesFile;
import MorrisWaterMaze.parameter.ParameterSource;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
public class MorrisWaterMaze extends JFrame
{
	private static final long
		serialVersionUID = 2638500843910810227L;
	
	private static final ParameterSource
		PARAMETER_SOURCE = ParameterSource.PROPERTIES_FILE;
	
	public static final String
		APPLICATION_NAME = "Morris Water Maze Simulation";

	private static final Dimension
		APPLICATION_DIMENSION = new Dimension(1024, 768);

	
	
	MorrisWaterMaze(SimulationController anim)
    {
    	super(APPLICATION_NAME);
    	setSize(APPLICATION_DIMENSION);
    	addWindowListener(new DemoAdapter());    	
    	this.add(anim);        
    }
    
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = makeParameterAccessorInstance(args);
		
		SimulationController.totalNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
		SimulationController.remainingNumberOfSimulations = SimulationController.totalNumberOfSimulations;
		SimulationController.mouse = new Mouse(parameterAccessor);
    	SimulationController.isStartingWithGui = parameterAccessor.isStartingWithGui();
    	SimulationController.fileName = parameterAccessor.getFilename();
		SimulationController.makeDirectory();
		
    	if(parameterAccessor.getNumberOfPics() > 0)
    	{
    		SimulationController.numberOfPics = parameterAccessor.getNumberOfPics();
    		SimulationController.picTimeFrameLowerBound = parameterAccessor.getLowerBoundOfPictureTimeFrame();
    		SimulationController.picTimeFrameUpperBound = parameterAccessor.getUpperBoundOfPictureTimeFrame();
    		SimulationController.maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
    	}
    	
    	if(SimulationController.isStartingWithGui)
        {
    		SimulationController simulationController = new SimulationController();
			simulationController.addParameterAccessor(parameterAccessor);
    		JFrame f = new MorrisWaterMaze(simulationController);
    		f.setVisible(true);
        }
    	else
    	{
    		SimulationController.loop = true;
    		SimulationController.startSim();
        }	
    }
	
	private static ParameterAccessor makeParameterAccessorInstance(String[] commandLineArguments)
	{
		if(PARAMETER_SOURCE == ParameterSource.PROPERTIES_FILE)
		{
			return new ParameterAccessorFromPropertiesFile();
		}
		else if(PARAMETER_SOURCE == ParameterSource.COMMAND_LINE)
		{
			return new ParameterAccessorFromArgs(commandLineArguments);
		}
		return null;
	}
	
	static class DemoAdapter extends WindowAdapter
    {
        @Override
		public void windowClosing (WindowEvent e){System.exit(0);}
    }    
}