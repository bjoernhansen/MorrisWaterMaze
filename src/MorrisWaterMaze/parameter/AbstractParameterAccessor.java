package MorrisWaterMaze.parameter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.StringJoiner;

public abstract class AbstractParameterAccessor implements ParameterAccessor
{
    String generateFilename()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" );
        return dateFormat.format(date) + "_parameter_" + getParameterString();
    }
    
    private String getParameterString()
    {
        StringJoiner joiner = new StringJoiner("_");
        joiner.add(String.valueOf(getNumberOfSimulations()))
              .add(String.valueOf(getMaximumMouseSwimmingTime()))
              .add(String.valueOf(getMouseTrainingLevel()))
              .add(String.valueOf(getStepLengthBias()))
              .add(String.valueOf(isMouseStartingPositionLeft()))
              .add(String.valueOf(mouseSpeed()))
              .add(String.valueOf(isStartingWithGui()))
              .add(String.valueOf(getNumberOfPics()))
              .add(String.valueOf(getLowerBoundOfPictureTimeFrame()))
              .add(String.valueOf(getUpperBoundOfPictureTimeFrame()))
              .add(String.valueOf(getMaximumTrajectoriesPerPicture()));
        return joiner.toString();
    }
}
