import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
 
public class Main extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static AnimatorPanel anim;
    
    Main()
    {
    	super("Drowning Mice Simulation");
    	setSize(1024, 768);
    	addWindowListener(new DemoAdapter());    	
    	this.add(anim);        
    }
    
    public static void main( String argv[] ) throws FileNotFoundException
    {
        anim = new AnimatorPanel();
    	JFrame f = new Main();
    	f.setVisible(true);
    }
    
    static class DemoAdapter extends WindowAdapter
    {
        public void windowClosing (WindowEvent e){System.exit(0);}
    }    
}