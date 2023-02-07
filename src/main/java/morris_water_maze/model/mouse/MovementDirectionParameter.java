package morris_water_maze.model.mouse;

public interface MovementDirectionParameter
{
    double getFieldOfView();
    
    double getUntrainedAngleDistributionSigma();
    
    double getMeanPoolBorderReboundAngle();
    
    double getReboundAngleDistributionSigma();
    
    double getStartingDirectionAngleRange();
}
