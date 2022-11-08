package MorrisWaterMaze.graphics.painter;

import MorrisWaterMaze.graphics.GraphicsAdapter;
import MorrisWaterMaze.model.simulation.WaterMorrisMazeSimulation;


final class SimulationPainter extends Painter<WaterMorrisMazeSimulation>
{
    @Override
    public void paint(GraphicsAdapter graphicsAdapter, WaterMorrisMazeSimulation simulation)
    {
        paintEntity(graphicsAdapter, simulation.getPool());
        paintEntity(graphicsAdapter, simulation.getPlatform());
        paintEntity(graphicsAdapter, simulation.getMouseMovement());
    }
}
