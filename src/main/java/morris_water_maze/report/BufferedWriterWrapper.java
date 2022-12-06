package morris_water_maze.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


final class BufferedWriterWrapper
{
    private final BufferedWriter
        bufferedWriter;
    
    
    public BufferedWriterWrapper(String fileName)
    {
        bufferedWriter = makeBufferedWriter(fileName);
    }
    
    private BufferedWriter makeBufferedWriter(String fileName)
    {
        try
        {
            return new BufferedWriter(new FileWriter(fileName));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void write(String text)
    {
        String textWithLineSeparator = extendWithLineSeparator(text);
        try
        {
            bufferedWriter.write(textWithLineSeparator);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private String extendWithLineSeparator(String text)
    {
        return text + System.getProperty("line.separator");
    }
    
    public void close()
    {
        try
        {
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
