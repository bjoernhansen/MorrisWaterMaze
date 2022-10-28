package MorrisWaterMaze;

import MorrisWaterMaze.parameter.ParameterAccessor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SimulationPanel extends JPanel implements ActionListener, ChangeListener
{
    private long lastPainted = System.currentTimeMillis();
    
    double numberOfPicturesPerSecond = 75;
    
    private final JButton startAndPauseButton;
    private final JButton restartButton;
    private final JSpinner mouseTrainingLevelSpinner;
    private final JSpinner numberOfSimulationsSpinner;
    
    private final SettingModifier settingModifier;
    
    private final LoopController loopController;
    
    
    SimulationPanel(SettingModifier settingModifier, ParameterAccessor parameterAccessor, LoopController loopController)
    {
        this.loopController = loopController;
        this.settingModifier = settingModifier;
        setLayout(null);
        startAndPauseButton = new JButton("Starten");
        JLabel mouseLevelLabel = new JLabel("training level");
        JLabel numberOfSimulationLabel = new JLabel("number of simulations");
        restartButton = new JButton("Neustart");
        mouseTrainingLevelSpinner = new JSpinner(new SpinnerNumberModel(parameterAccessor.getMouseTrainingLevel(), 0.0, 1.0, 0.01));
        mouseTrainingLevelSpinner.addChangeListener(this);
        numberOfSimulationsSpinner = new JSpinner(new SpinnerNumberModel(parameterAccessor.getNumberOfSimulations(), 1.0, Double.MAX_VALUE, 1.00));
        numberOfSimulationsSpinner.addChangeListener(this);
        JPanel mainPanel = new JPanel(new GridLayout(3, 2));
        mainPanel.setBounds(675, 25, 300, 120);
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        startAndPauseButton.addActionListener(this);
        restartButton.addActionListener(this);
        add(mainPanel);
        mainPanel.add(startAndPauseButton);
        mainPanel.add(restartButton);
        mainPanel.add(mouseLevelLabel);
        mainPanel.add(mouseTrainingLevelSpinner);
        mainPanel.add(numberOfSimulationLabel);
        mainPanel.add(numberOfSimulationsSpinner);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        if(numberOfPicturesPerSecond != 0 && System.currentTimeMillis() - lastPainted > 1000/numberOfPicturesPerSecond)
        {
            Graphics2D g2d = (Graphics2D) g.create();
            Controller.getInstance().drawOffImage();
            super.paintComponent(g);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(Controller.getInstance().offImage, 25, 25, null);
            g2d.dispose();
            lastPainted = System.currentTimeMillis();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Controller controller = Controller.getInstance();
        Object o = e.getSource();
        
        if (o == startAndPauseButton)
        {
            if(loopController.getLoopState())
            {
                startAndPauseButton.setText("Start");
            }
            else
            {
                startAndPauseButton.setText("Stop");
            }
            loopController.switchLoopState();
            mouseTrainingLevelSpinner.setEnabled(false);
            numberOfSimulationsSpinner.setEnabled(false);
        }
        else if(o == restartButton)
        {
            controller.reset();
            loopController.stopLooping();
            settingModifier.resetRemainingNumberOfSimulations();
            settingModifier.clearSearchTime();
            mouseTrainingLevelSpinner.setEnabled(true);
            numberOfSimulationsSpinner.setEnabled(true);
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        Object o = e.getSource();
        if(o==mouseTrainingLevelSpinner)
        {
            if( 0 > Double.parseDouble(mouseTrainingLevelSpinner.getValue().toString()))
            {
                mouseTrainingLevelSpinner.setValue(0.0);
            }
            else if( 1 < Double.parseDouble(mouseTrainingLevelSpinner.getValue().toString()))
            {
                mouseTrainingLevelSpinner.setValue(1.0);
            }
            double mouseTrainingLevel = Double.parseDouble(mouseTrainingLevelSpinner.getValue().toString());
            settingModifier.setMouseTrainingLevel(mouseTrainingLevel);
        }
        if(o==numberOfSimulationsSpinner)
        {
            if( 0 > Double.parseDouble(mouseTrainingLevelSpinner.getValue().toString()))
            {
                numberOfSimulationsSpinner.setValue(0.0);
            }
            else if( 1 < Double.parseDouble((mouseTrainingLevelSpinner).getValue().toString()))
            {
                numberOfSimulationsSpinner.setValue(1.0);
            }
            int numberOfSimulations = Integer.parseInt(numberOfSimulationsSpinner.getValue().toString());
            settingModifier.setRemainingAndTotalNumberOfSimulations(numberOfSimulations);
        }
    }
    
    public void setStartAndPauseButtonText(String text)
    {
        startAndPauseButton.setText(text);
    }
}
