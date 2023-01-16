package morris_water_maze.util;

import org.apache.commons.math3.util.Precision;


public final class DoubleComparison
{
    public static final double
        EPSILON = 0.000001;
    
    
    public static boolean doubleEquals(double value1, double value2)
    {
        return Precision.equals(value1, value2, EPSILON);
    }
}
