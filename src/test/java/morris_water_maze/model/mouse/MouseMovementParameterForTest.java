package morris_water_maze.model.mouse;

public class MouseMovementParameterForTest implements MovementDirectionParameter
{
    @Override
    public double getFieldOfView()
    {
        return 90.0;
    }
    
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
