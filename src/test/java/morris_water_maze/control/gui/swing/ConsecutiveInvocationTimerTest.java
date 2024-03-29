package morris_water_maze.control.gui.swing;

import morris_water_maze.util.CurrentTimeProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("ConsecutiveInvocationTimer")
class ConsecutiveInvocationTimerTest
{
    @Test
    @DisplayName("works as intended.")
    public void testTimer()
    {
        long timeIntervall = 100;
        ConsecutiveInvocationTimer timer = new ConsecutiveInvocationTimer(timeIntervall);
        
        CurrentTimeProvider currentTimeProvider = mock(CurrentTimeProvider.class);
        when(currentTimeProvider.currentTimeMills()).thenReturn(0L, timeIntervall/2, 3*timeIntervall/2);
    
        timer.setCurrentTimeProvider(currentTimeProvider);
        
        assertThat(timer.isNextSimulationStepToBeStartedNow()).isFalse();
        assertThat(timer.isNextSimulationStepToBeStartedNow()).isTrue();
        assertThat(timer.isNextSimulationStepToBeStartedNow()).isFalse();
    }
}

