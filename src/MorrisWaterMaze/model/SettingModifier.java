package MorrisWaterMaze.model;

public interface SettingModifier
{
    void resetRemainingNumberOfSimulationRuns();
    void setMouseTrainingLevel(double mouseTrainingLevel);
    void setRemainingAndTotalNumberOfSimulationRuns(int numberOfSimulations);
    void clearSearchTime();
}
