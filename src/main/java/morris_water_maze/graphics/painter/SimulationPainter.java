package morris_water_maze.graphics.painter;

import morris_water_maze.graphics.adapter.GraphicsAdapter;
import morris_water_maze.model.simulation.WaterMorrisMazeSimulation;


final class SimulationPainter extends Painter<WaterMorrisMazeSimulation>
{
    @Override
    public void paint(GraphicsAdapter graphics, WaterMorrisMazeSimulation simulation)
    {
        paintEntity(graphics, simulation.getPool());
        paintEntity(graphics, simulation.getMouseMovement());
        paintEntity(graphics, simulation.getPlatform());
    }
}
