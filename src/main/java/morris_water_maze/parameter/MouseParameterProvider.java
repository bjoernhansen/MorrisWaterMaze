package morris_water_maze.parameter;

import morris_water_maze.model.StartingSide;
import morris_water_maze.model.mouse.MouseParameter;

import java.util.Properties;


public final class MouseParameterProvider implements MouseParameter
{
    private final double
        maximumMouseSwimmingDuration;
    
    private final double
        mouseTrainingLevel;
    
    private final double
        stepLengthBias;
    
    private final StartingSide
        startingSide;
    
    private final double
        mouseSpeed;
    
    private final double
        fieldOfView;
    
    private final double
        untrainedAngleDistributionSigma;
    
    private final double
        meanPoolBorderReboundAngle;
    
    private final double
        reboundAngleDistributionSigma;
    
    private final double
        startingDirectionAngleRange;
    
    
    
    public MouseParameterProvider(Properties parameter)
    {
        maximumMouseSwimmingDuration = determineMaximumSwimmingTime(parameter.getProperty("maximumMouseSwimmingTime", "0.0"));
        mouseTrainingLevel = Double.parseDouble(parameter.getProperty("mouseTrainingLevel", "0.5"));
        stepLengthBias = Double.parseDouble(parameter.getProperty("stepLengthBias", "5.0"));
        startingSide = Boolean.parseBoolean(parameter.getProperty("isMouseStartPositionLeft", "true"))
            ? StartingSide.LEFT
            : StartingSide.RIGHT;
        mouseSpeed = Double.parseDouble(parameter.getProperty("mouseSpeed", "5"));
        fieldOfView = Double.parseDouble(parameter.getProperty("fieldOfView", " 90.0"));
        untrainedAngleDistributionSigma = Double.parseDouble(parameter.getProperty("untrainedAngleDistributionSigma", " 22.5"));
        meanPoolBorderReboundAngle = Double.parseDouble(parameter.getProperty("meanPoolBorderReboundAngle", " 15.0"));
        reboundAngleDistributionSigma = Double.parseDouble(parameter.getProperty("reboundAngleDistributionSigma", " 60.0"));
        startingDirectionAngleRange = Double.parseDouble(parameter.getProperty("startingDirectionAngleRange", " 180.0"));
        
        validate();
    }
    
    private void validate()
    {
        if(maximumMouseSwimmingDuration < 0.0)
        {
            throw new IllegalArgumentException("Illegal argument: maximum mouse swimming duration < 0.0");
        }
        if(mouseTrainingLevel < 0.0 || mouseTrainingLevel > 1.0)
        {
            throw new IllegalArgumentException("Illegal argument: mouse training level is outside of intervall [0.0, 1.0]");
        }
        if(stepLengthBias <= 1.0)
        {
            throw new IllegalArgumentException("Illegal argument: step length bias does not exceed 1.0");
        }
        if(mouseSpeed <= 0.0)
        {
            throw new IllegalArgumentException("Illegal argument: step length bias does not exceed 0.0");
        }
    }
    
    private double determineMaximumSwimmingTime(String swimmingTimeAsString)
    {
        double swimmingTime = Double.parseDouble(swimmingTimeAsString);
        return swimmingTime > 0.0
            ? swimmingTime
            : Double.MAX_VALUE;
    }
    
    @Override
    public double getMaximumMouseSwimmingDuration()
    {
        return maximumMouseSwimmingDuration;
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
    public double getMouseSpeed()
    {
        return mouseSpeed;
    }
    
    @Override
    public double getFieldOfView()
    {
        return fieldOfView;
    }
    
    @Override
    public double getUntrainedAngleDistributionSigma()
    {
        return untrainedAngleDistributionSigma;
    }
    
    @Override
    public double getMeanPoolBorderReboundAngle()
    {
        return meanPoolBorderReboundAngle;
    }
    
    @Override
    public double getReboundAngleDistributionSigma()
    {
        return reboundAngleDistributionSigma;
    }
    
    @Override
    public double getStartingDirectionAngleRange()
    {
        return startingDirectionAngleRange;
    }
}
