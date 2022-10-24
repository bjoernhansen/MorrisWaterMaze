package MorrisWaterMaze.parameter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum ParameterSource
{
    COMMAND_LINE(ParameterAccessorFromArgs::new),
    PROPERTIES_FILE(ParameterAccessorFromPropertiesFile::new);
    
    
    private final Function<List<String>, ParameterAccessor> parameterAccessorFunction;
    
    
    ParameterSource(Function<List<String>, ParameterAccessor> parameterAccessorFunction)
    {
        this.parameterAccessorFunction = parameterAccessorFunction;
    }
    
    public ParameterAccessor makeParameterAccessorInstance(String[] commandLineArguments)
    {
        return parameterAccessorFunction.apply(Arrays.asList(commandLineArguments));
    }
}
