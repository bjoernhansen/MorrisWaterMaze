package MorrisWaterMaze.parameter;

import MorrisWaterMaze.MorrisWaterMaze;


public class ParameterAccessorFromArgs extends AbstractParameterAccessor
{
    private final int
        numberOfSimulations;
    
    private final int
        maximumMouseSwimmingTime;
    
    private final double
        mouseTrainingLevel;
    
    private final double
        stepLengthBias;
    
    private final boolean
        isMouseStartPositionLeft;
    
    private final double
        mouseSpeed;
    
    private final boolean
        isStartingWithGui;
    
    private final int
        numberOfPics;
    
    private final double
        lowerBoundOfPictureTimeFrame;
    
    private final double
        upperBoundOfPictureTimeFrame;
    
    private final int
        maximumTrajectoriesPerPicture;
    
    private final String
        fileName;
    
    
    public ParameterAccessorFromArgs(String[] args)
    {
        checkArgumentVector(args);
        numberOfSimulations = Integer.parseInt(args[0]);
        maximumMouseSwimmingTime = Integer.parseInt(args[1]);
        mouseTrainingLevel = Double.parseDouble(args[2]);
        stepLengthBias = Double.parseDouble(args[3]);
        isMouseStartPositionLeft = Boolean.parseBoolean(args[4]);
        mouseSpeed = Double.parseDouble(args[5]);
        isStartingWithGui = Boolean.parseBoolean(args[6]);
    
        boolean paintPictures = args.length == 11;
        if(paintPictures)
        {
            numberOfPics = Integer.parseInt(args[7]);
            lowerBoundOfPictureTimeFrame = Double.parseDouble(args[8]);
            upperBoundOfPictureTimeFrame = Double.parseDouble(args[9]);
            maximumTrajectoriesPerPicture = Integer.parseInt(args[10]);
        }
        else
        {
            numberOfPics = 0;
            lowerBoundOfPictureTimeFrame = 0;
            upperBoundOfPictureTimeFrame = 0;
            maximumTrajectoriesPerPicture = 0;
        }
        fileName = generateFilename();
    }
    
    private void checkArgumentVector(String[] argv)
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
    }
    
    @Override
    public int getNumberOfSimulations()
    {
        return numberOfSimulations;
    }
    
    @Override
    public int getMaximumMouseSwimmingTime()
    {
        return maximumMouseSwimmingTime;
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return mouseTrainingLevel;
    }
    
    @Override
    public double getStepLengthBias()
    {
        return stepLengthBias;
    }
    
    @Override
    public boolean isMouseStartingPositionLeft()
    {
        return isMouseStartPositionLeft;
    }
    
    @Override
    public double mouseSpeed()
    {
        return mouseSpeed;
    }
    
    @Override
    public boolean isStartingWithGui()
    {
        return isStartingWithGui;
    }
    
    @Override
    public int getNumberOfPics()
    {
        return numberOfPics;
    }
    
    @Override
    public double getLowerBoundOfPictureTimeFrame()
    {
        return lowerBoundOfPictureTimeFrame;
    }
    
    @Override
    public double getUpperBoundOfPictureTimeFrame()
    {
        return upperBoundOfPictureTimeFrame;
    }
    
    @Override
    public int getMaximumTrajectoriesPerPicture()
    {
        return maximumTrajectoriesPerPicture;
    }
    
    @Override
    public String getFilename()
    {
        return fileName;
    }
}
