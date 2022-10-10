package MorrisWaterMaze;

import MorrisWaterMaze.parameter.ParameterAccessor;
import MorrisWaterMaze.parameter.ParameterAccessorFromArgs;
import MorrisWaterMaze.parameter.ParameterAccessorFromPropertiesFile;
import MorrisWaterMaze.parameter.ParameterSource;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
public class MorrisWaterMaze extends JFrame
{
	private static final long
		serialVersionUID = 2638500843910810227L;
	
	private static final ParameterSource
		PARAMETER_SOURCE = ParameterSource.PROPERTIES_FILE;

	MorrisWaterMaze(Simulation anim)
    {
    	super("Morris Water Maze Simulation");
    	setSize(1024, 768);
    	addWindowListener(new DemoAdapter());    	
    	this.add(anim);        
    }
    
    public static void main(String[] args)
    {
		ParameterAccessor parameterAccessor = makeParameterAccessorInstance(args);
		
		Simulation.totalNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
		Simulation.remainingNumberOfSimulations = Simulation.totalNumberOfSimulations;
		Simulation.mouse = new Mouse(parameterAccessor);
    	Simulation.isStartingWithGui = parameterAccessor.isStartingWithGui();
    	Simulation.fileName = parameterAccessor.getFilename();
		Simulation.makeDirectory();
		
    	if(parameterAccessor.getNumberOfPics() > 0)
    	{
    		Simulation.number_of_pics = parameterAccessor.getNumberOfPics();
    		Simulation.pic_time_frame_lower_bound = parameterAccessor.getLowerBoundOfPictureTimeFrame();
    		Simulation.pic_time_frame_upper_bound = parameterAccessor.getUpperBoundOfPictureTimeFrame();
    		Simulation.max_nr_of_pic_in_series = parameterAccessor.getMaximumTrajectoriesPerPicture();
    	}
    	
    	if(Simulation.isStartingWithGui)
        {        	
    		Simulation simulation = new Simulation();
			simulation.addParameterAccessor(parameterAccessor);
    		JFrame f = new MorrisWaterMaze(simulation);
    		f.setVisible(true);
        }
    	else
    	{
    		Simulation.loop = true;
    		Simulation.startSim();
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