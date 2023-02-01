package morris_water_maze.model.simulation;

public interface SettingModifier
{
    void resetRemainingNumberOfSimulationRuns();
    
    void setRemainingAndTotalNumberOfSimulationRuns(int numberOfSimulations);
    void clearSearchTime();
}
