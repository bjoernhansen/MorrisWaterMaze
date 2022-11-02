package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.control.SimulationController;
import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.model.Simulation;

import java.awt.Color;


class SimulationPainter extends Painter<Simulation>
{
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, Simulation simulation)
    {
        paintBackground(graphicsAdapter);
        paintEntity(graphicsAdapter, simulation.getPool());
        paintEntity(graphicsAdapter, simulation.getPlatform());
        paintEntity(graphicsAdapter, simulation.getMouseMovement());
    }
    
    private void paintBackground(GraphicsAdapter graphicsAdapter)
    {
        graphicsAdapter.setColor(Color.white);
        graphicsAdapter.fillRect(0, 0, (int) SimulationController.ZOOM_FACTOR * SimulationController.IMAGE_SIZE, (int) SimulationController.ZOOM_FACTOR * SimulationController.IMAGE_SIZE);
    }
}
