package morris_water_maze.report;

import morris_water_maze.control.observer.SimulationSeriesCompletionObserver;
import morris_water_maze.model.simulation.Simulation;


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
