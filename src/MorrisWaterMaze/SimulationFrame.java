package MorrisWaterMaze;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimulationFrame extends JFrame
{
    private static final String
        APPLICATION_NAME = "Morris Water Maze Simulation";
    
    private static final Dimension
        APPLICATION_DIMENSION = new Dimension(1024, 768);
    
    
    SimulationFrame(JPanel panel)
    {
        super(APPLICATION_NAME);
        setSize(APPLICATION_DIMENSION);
        addWindowListener(new ShutdownWindowAdapter());
        this.add(panel);
    }
    
    static class ShutdownWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing (WindowEvent e){System.exit(0);}
    }
}
