package morris_water_maze.control.gui.swing;

import morris_water_maze.util.CurrentTimeProvider;


final class ConsecutiveInvocationTimer
{
    private final long
        desiredTimeIntervallBetweenTwoInvocations;
    
    private long
        timePassedSinceLastInvocation;
       
    private long
        timeStampOfLastInvocationAttempt;
    
    private CurrentTimeProvider
        currentTimeProvider;

    
    ConsecutiveInvocationTimer(long desiredTimeIntervallBetweenTwoInvocations)
    {
        this.desiredTimeIntervallBetweenTwoInvocations = desiredTimeIntervallBetweenTwoInvocations;
        currentTimeProvider = new CurrentTimeProvider();
        timeStampOfLastInvocationAttempt = currentTimeProvider.currentTimeMills();
    }
    
    boolean isNextSimulationStepToBeStartedNow()
    {
        long currentTimeMills = currentTimeProvider.currentTimeMills();
        long timePassedSinceLastQuery = currentTimeMills - timeStampOfLastInvocationAttempt;
        timeStampOfLastInvocationAttempt = currentTimeMills;
        timePassedSinceLastInvocation += timePassedSinceLastQuery;
       
        if(timePassedSinceLastInvocation > desiredTimeIntervallBetweenTwoInvocations)
        {
            timePassedSinceLastInvocation -= desiredTimeIntervallBetweenTwoInvocations;
            timePassedSinceLastInvocation %= desiredTimeIntervallBetweenTwoInvocations;
            return true;
        }
        return false;
    }
    
    void setCurrentTimeProvider(CurrentTimeProvider currentTimeProvider)
    {
        this.currentTimeProvider = currentTimeProvider;
        timeStampOfLastInvocationAttempt = currentTimeProvider.currentTimeMills();
    }
}
