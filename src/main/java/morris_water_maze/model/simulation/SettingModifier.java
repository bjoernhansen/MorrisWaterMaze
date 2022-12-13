package morris_water_maze.model.simulation;

public interface SettingModifier
{
    void resetRemainingNumberOfSimulationRuns();
    void setMouseTrainingLevel(double mouseTrainingLevel);
    void setRemainingAndTotalNumberOfSimulationRuns(int numberOfSimulations);
    void clearSearchTime();
}
