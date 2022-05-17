package MorrisWaterMaze;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
 
public class MorrisWaterMaze extends JFrame
{
	private static final long serialVersionUID = 2638500843910810227L;

	MorrisWaterMaze(Simulation anim)
    {
    	super("Morris Water Maze Simulation");
    	setSize(1024, 768);
    	addWindowListener(new DemoAdapter());    	
    	this.add(anim);        
    }
    
    public static void main(String[] argv)
    {    	
    	if(argv.length != 7 && argv.length != 11 || argv[0].equals("help"))
    	{
    		if(!(argv.length > 0 && argv[0].equals("help"))){System.out.println("Wrong parameter input!");}
    		System.out.println("Expected Input: " + MorrisWaterMaze.class.getName() + " nr_of_sims max_sim_time training_level step_length_bias mouse_start_pos mouse_speed is_app");
    		System.out.println("Alternative Input: " + MorrisWaterMaze.class.getName() + " nr_of_sims max_sim_time training_level step_length_bias mouse_start_pos mouse_speed is_app nr_of_pics pic_time_frame_lower_bound pic_time_frame_upper_bound");
    		
    		System.out.println("\nExample input 1: " + MorrisWaterMaze.class.getName() + " 500 120 0.0 2 1 5 0");
    		System.out.println(	"500 MWM simulations (maximal length of 120s each; small starting distance between mouse and platform) for a completely untrained mouth " +
    							"of speed 5cm/s. Prolong each unidirectional step of the mouth by ln(2)s and start the simulation as application (with GUI).");  
    		System.out.println("\nExample input 2: " + MorrisWaterMaze.class.getName() + " 100000 0 0.7 5 0 3 1 20 17.5 18.0");
    		System.out.println(	"100000 MWM simulations (big starting distance between mouse and platform) without time restriction for a training level 0.7 mouse " +
								"of speed 3cm/s. Prolong each unidirectional step of the mouth by ln(5)s and do not start the simulation with GUI. Save up to 20 pictures of " +
								"mice trajectories when the total search time is between 17.5s and 18.0s.");
    		System.exit(1);
    	}
    	Simulation.start_as_app = argv[6].equals("0");
    	Simulation.remaining_number_of_sim = Simulation.total_number_of_sim = Integer.parseInt(argv[0]);
    	Simulation.mouse = new Mouse(argv);
    	Simulation.getFilename(concat(argv));
    	
    	if(argv.length == 11)
    	{
    		Simulation.number_of_pics = Integer.parseInt(argv[7]);
    		Simulation.pic_time_frame_lower_bound = Double.parseDouble(argv[8]);
    		Simulation.pic_time_frame_upper_bound = Double.parseDouble(argv[9]);
    		Simulation.max_nr_of_pic_in_series = Integer.parseInt(argv[10]);
    	}
    	
    	if(Simulation.start_as_app)
        {        	
    		Simulation anim = new Simulation();
    		JFrame f = new MorrisWaterMaze(anim);
    		f.setVisible(true);
        }
    	else
    	{
    		Simulation.loop = true;
    		Simulation.start_sim();
        }	
    }
    
    static String concat(String [] string_array)
    {
    	StringBuilder output_string = new StringBuilder(32);
		for (String s : string_array)
		{
			output_string.append("_")
						 .append(s);
		}
    	return output_string.toString();
    }
    
    static class DemoAdapter extends WindowAdapter
    {
        @Override
		public void windowClosing (WindowEvent e){System.exit(0);}
    }    
}