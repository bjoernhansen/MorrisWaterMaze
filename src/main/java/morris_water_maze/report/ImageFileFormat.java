package morris_water_maze.report;

public enum ImageFileFormat
{
    PNG,
    SVG;
    
    public String getName()
    {
        return this.name().toLowerCase();
    }
}
