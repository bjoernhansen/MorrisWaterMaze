package MorrisWaterMaze.model;

public interface SettingModifier
{
    void resetRemainingNumberOfSimulations();
    void setMouseTrainingLevel(double mouseTrainingLevel);
    void setRemainingAndTotalNumberOfSimulations(int numberOfSimulations);
    void clearSearchTime();
}
