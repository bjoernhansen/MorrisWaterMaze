package morris_water_maze.model.mouse;

import morris_water_maze.model.StartingSide;


public interface MouseParameter extends MovementDirectionParameter
{
    double getMaximumMouseSwimmingDuration();
    
    double getStepLengthBias();
    
    StartingSide getStartingSide();
    
    double getMouseSpeed();
    
    double getUntrainedAngleDistributionSigma();
    
    double getMeanPoolBorderReboundAngle();
    
    double getReboundAngleDistributionSigma();
}
