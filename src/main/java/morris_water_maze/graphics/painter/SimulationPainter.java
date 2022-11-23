package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.GraphicsAdapter;
import morris_water_maze.model.simulation.WaterMorrisMazeSimulation;


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
