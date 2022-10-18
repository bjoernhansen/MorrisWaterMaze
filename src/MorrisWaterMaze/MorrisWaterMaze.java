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

	
	
	MorrisWaterMaze(Controller anim)
    {
    	super(APPLICATION_NAME);
    	setSize(APPLICATION_DIMENSION);
    	addWindowListener(new DemoAdapter());    	
    	this.add(anim);        
    }
    
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = makeParameterAccessorInstance(args);
		Simulation simulation = new Simulation(parameterAccessor);

    	Controller.isStartingWithGui = parameterAccessor.isStartingWithGui();
    	Controller.fileName = parameterAccessor.getFilename();
		Controller.makeDirectory();
		
    	if(parameterAccessor.getNumberOfPics() > 0)
    	{
    		Controller.numberOfPics = parameterAccessor.getNumberOfPics();
    		Controller.picTimeFrameLowerBound = parameterAccessor.getLowerBoundOfPictureTimeFrame();
    		Controller.picTimeFrameUpperBound = parameterAccessor.getUpperBoundOfPictureTimeFrame();
    		Controller.maxNrOfPicInSeries = parameterAccessor.getMaximumTrajectoriesPerPicture();
    	}
    	
    	if(Controller.isStartingWithGui)
        {
    		Controller controller = new Controller(simulation);
			controller.addParameterAccessor(parameterAccessor);
    		JFrame f = new MorrisWaterMaze(controller);
    		f.setVisible(true);
        }
    	else
    	{
			Controller.simulation = simulation;
			Controller.reset();
			while(simulation.isNotFinished())
			{
				simulation.nextStep();
			}
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