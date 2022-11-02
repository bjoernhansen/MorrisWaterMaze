package MorrisWaterMaze.gui;

import MorrisWaterMaze.control.SimulationControllerWithGui;
import MorrisWaterMaze.graphics.Graphics2DAdapter;
import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.graphics.painter.ImagePainter;
import MorrisWaterMaze.model.SettingModifier;
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
import java.awt.GridLayout;
import java.awt.Image;
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
    
    private final SimulationControllerWithGui
        simulationController;
    
    private final ImagePainter
        imagePainter;
    
    private final Paintable
        paintableEntity;
    
    
    
    public SimulationPanel(SettingModifier settingModifier, ParameterAccessor parameterAccessor, SimulationControllerWithGui simulationController, ImagePainter imagePainter, Paintable paintableEntity)
    {
        this.simulationController = simulationController;
        this.settingModifier = settingModifier;
        this.imagePainter = imagePainter;
        this.paintableEntity = paintableEntity;
        
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
    public void paintComponent(Graphics graphics)
    {
        if(numberOfPicturesPerSecond != 0 && System.currentTimeMillis() - lastPainted > 1000/numberOfPicturesPerSecond)
        {
            super.paintComponent(graphics);
            GraphicsAdapter graphicsAdapter = Graphics2DAdapter.of(graphics);
            graphicsAdapter.turnAntialiasingOn();
            Image image = imagePainter.paintImageOf(paintableEntity);
            graphicsAdapter.drawImage(image, 25, 25, null);
            lastPainted = System.currentTimeMillis();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object o = e.getSource();
        
        if (o == startAndPauseButton)
        {
            if(simulationController.getLoopState())
            {
                startAndPauseButton.setText("Start");
            }
            else
            {
                startAndPauseButton.setText("Stop");
            }
            simulationController.switchLoopState();
            mouseTrainingLevelSpinner.setEnabled(false);
            numberOfSimulationsSpinner.setEnabled(false);
        }
        else if(o == restartButton)
        {
            simulationController.reset();
            simulationController.stopLooping();
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
