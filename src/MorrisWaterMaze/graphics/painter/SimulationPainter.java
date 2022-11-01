package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.Simulation;
import MorrisWaterMaze.SimulationController;

import java.awt.Color;
import java.awt.Graphics2D;

class SimulationPainter extends Painter<Simulation>
{
    @Override
    public void paint(Graphics2D graphicsAdapter, Simulation simulation)
    {
        paintBackground(graphicsAdapter);
        PAINT_MANAGER.paint(graphicsAdapter, simulation.getPool());
        PAINT_MANAGER.paint(graphicsAdapter, simulation.getPlatform());
        PAINT_MANAGER.paint(graphicsAdapter, simulation.getMouseMovement());
    }
    
    private void paintBackground(Graphics2D offGraphics)
    {
        offGraphics.setColor(Color.white);
        offGraphics.fillRect(0, 0, (int) SimulationController.ZOOM_FACTOR * SimulationController.IMAGE_SIZE, (int) SimulationController.ZOOM_FACTOR * SimulationController.IMAGE_SIZE);
    }
}
