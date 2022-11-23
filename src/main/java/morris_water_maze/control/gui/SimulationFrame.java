package morris_water_maze.control.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static morris_water_maze.Main.IMAGE_SIZE;


final class SimulationFrame extends JFrame
{
    private static final String
        APPLICATION_NAME = "Morris Water Maze Simulation";
    
    public static final int
        IMAGE_BORDER_DISTANCE = 25;
    
    private static final int
        IMAGE_WITH_FRAME_SIZE = 2 * IMAGE_BORDER_DISTANCE + IMAGE_SIZE;
        
    private static final Rectangle
        CONTROL_PANEL_DIMENSION = new Rectangle(
            IMAGE_WITH_FRAME_SIZE,
            IMAGE_BORDER_DISTANCE,
            300,
            120);
    
    private static final Dimension
        APPLICATION_DIMENSION = new Dimension(
            IMAGE_WITH_FRAME_SIZE + CONTROL_PANEL_DIMENSION.width + IMAGE_BORDER_DISTANCE + 16,
            IMAGE_WITH_FRAME_SIZE + 38);
    
    
    public SimulationFrame(JPanel panel)
    {
        super(APPLICATION_NAME);
        setSize(APPLICATION_DIMENSION);
        addWindowListener(new ShutdownWindowAdapter());
        this.add(panel);
    }
    
    public static Rectangle getControlPanelDimension()
    {
        return CONTROL_PANEL_DIMENSION;
  
    }
    
    static class ShutdownWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing (WindowEvent e){System.exit(0);}
    }
}
