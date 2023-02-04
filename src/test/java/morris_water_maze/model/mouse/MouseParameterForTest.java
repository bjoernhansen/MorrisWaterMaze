package morris_water_maze.model.mouse;

import morris_water_maze.model.StartingSide;

public class MouseParameterForTest implements MouseParameter
{
    private static final double
        UNUSED = 0;
    
    
    @Override
    public double getMaximumMouseSwimmingDuration()
    {
        return UNUSED;
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return 0.5;
    }
    
    @Override
    public double getStepLengthBias()
    {
        return UNUSED;
    }
    
    @Override
    public StartingSide getStartingSide()
    {
        return null;
    }
    
    @Override
    public double getMouseSpeed()
    {
        return UNUSED;
    }
    
    @Override
    public double getFieldOfView()
    {
        return 90.0;
    }
    
    @Override
    public double getUntrainedAngleDistributionSigma()
    {
        return 22.5;
    }
    
    @Override
    public double getMeanPoolBorderReboundAngle()
    {
        return 60.0;
    }
    
    @Override
    public double getReboundAngleDistributionSigma()
    {
        return 15.0;
    }
    
    @Override
    public double getStartingDirectionAngleRange()
    {
        return 180.0;
    }
}
