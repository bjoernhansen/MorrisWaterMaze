package morris_water_maze.model;

public interface SettingModifier
{
    void resetRemainingNumberOfSimulationRuns();
    void setMouseTrainingLevel(double mouseTrainingLevel);
    void setRemainingAndTotalNumberOfSimulationRuns(int numberOfSimulations);
    void clearSearchTime();
}
