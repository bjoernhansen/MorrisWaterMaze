package morris_water_maze.parameter;

import morris_water_maze.Main;
import morris_water_maze.graphics.painter.image.ImagePainterType;
import morris_water_maze.model.StartingSide;
import morris_water_maze.report.ImageFileFormat;

import java.util.List;


final class ParameterAccessorFromArgs extends AbstractParameterAccessor
{
    private final int
        numberOfSimulations;
    
    private final int
        maximumMouseSwimmingTime;
    
    private final double
        mouseTrainingLevel;
    
    private final double
        stepLengthBias;
    
    private final StartingSide
        startingSide;
    
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
    
    private final ImagePainterType
        imagePainterTypeForPictureExport;
    
    private final String
        simulationId;
    
    
    public ParameterAccessorFromArgs(List<String> inputParameters)
    {
        checkArgumentVector(inputParameters);
        numberOfSimulations = Integer.parseInt(inputParameters.get(0));
        maximumMouseSwimmingTime = determineMaximumSwimmingTime(inputParameters.get(1));
        mouseTrainingLevel = Double.parseDouble(inputParameters.get(2));
        stepLengthBias = Double.parseDouble(inputParameters.get(3));
        startingSide = Boolean.parseBoolean(inputParameters.get(4)) ? StartingSide.LEFT : StartingSide.RIGHT;
        mouseSpeed = Double.parseDouble(inputParameters.get(5));
        isStartingWithGui = Boolean.parseBoolean(inputParameters.get(6));
    
        boolean paintPictures = inputParameters.size() == 11;
        if(paintPictures)
        {
            numberOfPics = Integer.parseInt(inputParameters.get(7));
            lowerBoundOfPictureTimeFrame = Double.parseDouble(inputParameters.get(8));
            upperBoundOfPictureTimeFrame = Double.parseDouble(inputParameters.get(9));
            maximumTrajectoriesPerPicture = Integer.parseInt(inputParameters.get(10));
            imagePainterTypeForPictureExport = Boolean.parseBoolean(inputParameters.get(11))
                                                ? ImagePainterType.DEFAULT
                                                : ImagePainterType.SVG;
        }
        else
        {
            numberOfPics = 0;
            lowerBoundOfPictureTimeFrame = 0;
            upperBoundOfPictureTimeFrame = 0;
            maximumTrajectoriesPerPicture = 0;
            imagePainterTypeForPictureExport = ImagePainterType.DEFAULT;
        }
        simulationId = generateSimulationId();
    }
    
    private void checkArgumentVector(List<String> inputParameters)
    {
        if(inputParameters.size() != 7 && inputParameters.size() != 12 || inputParameters.get(0).equals("help"))
        {
            if(!(inputParameters.size() > 0 && inputParameters.get(0).equals("help"))){System.out.println("Wrong parameter input!");}
            System.out.println("Expected Input: " + Main.class.getName() + " nr_of_sims max_sim_time training_level step_length_bias mouse_start_pos mouse_speed is_app");
            System.out.println("Alternative Input: " + Main.class.getName() + " nr_of_sims max_sim_time training_level step_length_bias mouse_start_pos mouse_speed is_app nr_of_pics pic_time_frame_lower_bound pic_time_frame_upper_bound");
            System.out.println("\nExample input 1: " + Main.class.getName() + " 500 120 0.0 2 false 5 true");
            System.out.println(	"500 MWM simulations (maximal length of 120s each; small starting distance between mouse and platform) for a completely untrained mouth " +
                "of speed 5cm/s. Prolong each unidirectional step of the mouth by ln(2)s and start the simulation as application (with GUI).");
            System.out.println("\nExample input 2: " + Main.class.getName() + " 100000 0 0.7 5 true 3 false 20 17.5 18.0 false");
            System.out.println(	"100000 MWM simulations (big starting distance between mouse and platform) without time restriction for a training level 0.7 mouse " +
                "of speed 3cm/s. Prolong each unidirectional step of the mouth by ln(5)s and do not start the simulation with GUI. Save up to 20 pictures of " +
                "mice trajectories when the total search time is between 17.5s and 18.0s. Save pictures in PNG file format.");
            System.exit(1);
        }
    }
    
    @Override
    public int getNumberOfSimulations()
    {
        return numberOfSimulations;
    }
    
    @Override
    public int getMaximumMouseSwimmingDuration()
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
    public StartingSide getStartingSide()
    {
        return startingSide;
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
    public ImagePainterType getImagePainterTypeForPictureExport()
    {
        return imagePainterTypeForPictureExport;
    }
    
    @Override
    public String getSimulationId()
    {
        return simulationId;
    }
}
