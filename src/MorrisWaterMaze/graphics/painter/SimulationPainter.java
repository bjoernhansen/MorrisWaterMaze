package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.model.Simulation;
import MorrisWaterMaze.control.SimulationController;

import java.awt.Color;
import java.awt.Graphics2D;

class SimulationPainter extends Painter<Simulation>
{
    @Override
    public void paint(Graphics2D graphicsAdapter, Simulation simulation)
    {
        paintBackground(graphicsAdapter);
        paintEntity(graphicsAdapter, simulation.getPool());
        paintEntity(graphicsAdapter, simulation.getPlatform());
        paintEntity(graphicsAdapter, simulation.getMouseMovement());
    }
    
    private void paintBackground(Graphics2D offGraphics)
    {
        offGraphics.setColor(Color.white);
        offGraphics.fillRect(0, 0, (int) SimulationController.ZOOM_FACTOR * SimulationController.IMAGE_SIZE, (int) SimulationController.ZOOM_FACTOR * SimulationController.IMAGE_SIZE);
    }
}
