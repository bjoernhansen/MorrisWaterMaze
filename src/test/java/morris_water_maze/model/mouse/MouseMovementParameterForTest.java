package morris_water_maze.model.mouse;

public class MouseMovementParameterForTest implements MovementDirectionParameter
{
    @Override
    public double getFieldOfView()
    {
        return 90.0;
    }
    
    /*@Override
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
    */
    @Override
    public double getStartingDirectionAngleRange()
    {
        return 180.0;
    }
    
    @Override
    public double getMouseTrainingLevel()
    {
        return 0.5;
    }
}
