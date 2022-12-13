package morris_water_maze.control.gui.swing;

import morris_water_maze.graphics.adapter.Graphics2dAdapter;
import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.graphics.Paintable;
import morris_water_maze.graphics.painter.image.ImagePainter;
import morris_water_maze.model.simulation.SettingModifier;
import morris_water_maze.parameter.ParameterAccessor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


final class SimulationPanel extends JPanel
{
    private static final double
        NUMBER_OF_PICTURES_PER_SECOND = 75;
    
    private static final int
        MAXIMUM_NUMBER_OF_SIMULATIONS = Integer.MAX_VALUE;
    
    private final int
        initialNumberOfSimulations;
    
    private final double
        initialMouseTrainingLevel;
    
    private long
        lastPainted = System.currentTimeMillis();
        
    private final SettingModifier
        settingModifier;
    
    private final SwingSimulationController
        simulationController;
    
    private final ImagePainter
        imagePainter;
    
    private final Paintable
        simulation;
        
    private JButton
        startAndPauseButton;
    
    private JButton
        restartButton;
    
    private JSpinner
        mouseTrainingLevelSpinner;
    
    private JSpinner
        numberOfSimulationsSpinner;
    
    
    public SimulationPanel(SettingModifier settingModifier, ParameterAccessor parameterAccessor, SwingSimulationController simulationController, ImagePainter imagePainter, Paintable simulation)
    {
        this.simulationController = simulationController;
        this.settingModifier = settingModifier;
        this.imagePainter = imagePainter;
        this.simulation = simulation;
        this.initialNumberOfSimulations = parameterAccessor.getNumberOfSimulations();
        this.initialMouseTrainingLevel = parameterAccessor.getMouseTrainingLevel();
        
        prepareGui();
    }
    
    private void prepareGui()
    {
        setLayout(null);
        createComponents();
        addListeners();
        addInitializedMainPanel();
    }
    
    private void createComponents()
    {
        startAndPauseButton = new JButton("Starten");
        restartButton = new JButton("Neustart");
        mouseTrainingLevelSpinner = new JSpinner(new SpinnerNumberModel(initialMouseTrainingLevel, 0.0, 1.0, 0.01));
        numberOfSimulationsSpinner = new JSpinner(new SpinnerNumberModel(initialNumberOfSimulations, 1.0, MAXIMUM_NUMBER_OF_SIMULATIONS, 1.00));
    }
    
    private void addListeners()
    {
        startAndPauseButton.addActionListener(startAndPauseButtonActionListener);
        restartButton.addActionListener(resetButtonActionListener);
        mouseTrainingLevelSpinner.addChangeListener(mouseTrainingLevelSpinnerListener);
        numberOfSimulationsSpinner.addChangeListener(numberOfSimulationsSpinnerListener);
    }
    
    private void addInitializedMainPanel()
    {
        JLabel mouseLevelLabel = new JLabel("training level");
        JLabel numberOfSimulationLabel = new JLabel("number of simulations");
        
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBounds(SimulationFrame.getControlPanelDimension());
        panel.setBorder(BorderFactory.createEtchedBorder());
     
        panel.add(startAndPauseButton);
        panel.add(restartButton);
        panel.add(mouseLevelLabel);
        panel.add(mouseTrainingLevelSpinner);
        panel.add(numberOfSimulationLabel);
        panel.add(numberOfSimulationsSpinner);
        
        add(panel);
    }
    
    @Override
    public void paintComponent(Graphics graphics)
    {
        if(hasEnoughTimePassedBeforeRepaint())
        {
            super.paintComponent(graphics);
            GraphicsAdapter graphicsAdapter = Graphics2dAdapter.of(graphics);
            imagePainter.initializeImage();
            imagePainter.paint(simulation);
            Image image = imagePainter.getImage();
            graphicsAdapter.drawImage(image, SimulationFrame.IMAGE_BORDER_DISTANCE, SimulationFrame.IMAGE_BORDER_DISTANCE, null);
            lastPainted = System.currentTimeMillis();
        }
    }
    
    private boolean hasEnoughTimePassedBeforeRepaint()
    {
        return System.currentTimeMillis() - lastPainted > 1000 / NUMBER_OF_PICTURES_PER_SECOND;
    }
    
    void resetStartAndPauseButton()
    {
        startAndPauseButton.setText("Start");
    }
    
    ActionListener startAndPauseButtonActionListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(simulationController.isSimulationInProgress())
            {
                resetStartAndPauseButton();
            }
            else
            {
                startAndPauseButton.setText("Stop");
            }
            simulationController.switchSimulationActivityState();
            mouseTrainingLevelSpinner.setEnabled(false);
            numberOfSimulationsSpinner.setEnabled(false);
        }
    };
    
    ActionListener resetButtonActionListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            simulationController.reset();
            simulationController.stopLooping();
            settingModifier.resetRemainingNumberOfSimulationRuns();
            settingModifier.clearSearchTime();
            mouseTrainingLevelSpinner.setEnabled(true);
            numberOfSimulationsSpinner.setEnabled(true);
        }
    };
    
    ChangeListener mouseTrainingLevelSpinnerListener = new ChangeListener()
    {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            double mouseTrainingLevel = getDoubleValueFrom(mouseTrainingLevelSpinner);
            settingModifier.setMouseTrainingLevel(mouseTrainingLevel);
        }
    };
    
    ChangeListener numberOfSimulationsSpinnerListener = new ChangeListener()
    {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            int numberOfSimulations = getIntValueFrom(numberOfSimulationsSpinner);
            settingModifier.setRemainingAndTotalNumberOfSimulationRuns(numberOfSimulations);
        }
    };
    
    private double getDoubleValueFrom(JSpinner spinner)
    {
        return Double.parseDouble(spinner.getValue().toString());
    }
    
    private int getIntValueFrom(JSpinner spinner)
    {
        return (int) getDoubleValueFrom(spinner);
    }
}
