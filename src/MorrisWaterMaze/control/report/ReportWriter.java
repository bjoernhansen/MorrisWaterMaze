package MorrisWaterMaze.control.report;

import MorrisWaterMaze.control.FileNameProvider;
import MorrisWaterMaze.control.observer.SimulationSeriesCompletionObserver;
import MorrisWaterMaze.model.simulation.Simulation;


public final class ReportWriter implements SimulationSeriesCompletionObserver
{
    private Simulation
        simulation;
    
    private final FileNameProvider
        fileNameProvider;
    
    
    public ReportWriter(FileNameProvider fileNameProvider)
    {
        this.fileNameProvider = fileNameProvider;
    }
    
    @Override
    public void beNotifiedAboutEndOfAllSimulations()
    {
        printAverageSearchTimeOnScreen();
        writeFinalSimulationReport();
    }
    
    private void printAverageSearchTimeOnScreen()
    {
        System.out.println("\nDurchschnittliche Suchzeit: " + simulation.getAverageSearchTime() + "\n");
    }
    
    private void writeFinalSimulationReport()
    {
        System.out.println("Schreibe Datei: " + fileNameProvider.getFinalReportPath());
        String finalReportPath = fileNameProvider.getFinalReportPath();
        BufferedWriterWrapper bufferedWriterWrapper = new BufferedWriterWrapper(finalReportPath);
        simulation.getSearchTimeProvider()
                  .forEachSearchTime(bufferedWriterWrapper::write);
        bufferedWriterWrapper.close();
    }
    
    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }
}