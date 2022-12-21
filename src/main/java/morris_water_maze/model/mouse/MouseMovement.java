package morris_water_maze.model.mouse;

import morris_water_maze.graphics.Paintable;
import morris_water_maze.parameter.MouseParameterAccessor;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class MouseMovement implements Paintable
{
	private final double
		maximumSwimmingDuration;	// maximale Schwimmzeit; default: 0 (no restriction)
	
	private final double
		stepLengthBias;				// bestimmt wie oft die Maus die Richtung wechselt; jeder Schritt der Maus verlängert sich um ln(step_length_bias); default: 5
	
	private final ArrayList<Double>
		timeSteps = new ArrayList<>();
	
	private final EscapeRoute
		escapeRoute;
	
	private final Mouse
		mouse;
	
	
	public MouseMovement(MouseParameterAccessor parameterAccessor, Mouse mouse)
	{
		this.mouse = mouse;
		escapeRoute = new EscapeRoute(mouse.getCoordinates());
		maximumSwimmingDuration = parameterAccessor.getMaximumMouseSwimmingDuration();
		stepLengthBias = parameterAccessor.getStepLengthBias();
	}
	
	public void performNextStep()
	{
		double durationOfCurrentSimulationStep = getDurationOfNextSimulationStep();
		mouse.moveFor(durationOfCurrentSimulationStep);
		escapeRoute.addNextSectionToward(mouse.getCoordinates());
		
		double simulationRunTimeSoFar = getSimulationRunTimeSoFarIncluding(durationOfCurrentSimulationStep);
		timeSteps.add(simulationRunTimeSoFar);
		if(isSimulationRunOverAfter(simulationRunTimeSoFar))
		{
			mouse.stopSwimming();
		}
	}
	
	private double getSimulationRunTimeSoFarIncluding(double durationOfCurrentSimulationStep)
	{
		return durationOfCurrentSimulationStep + getSumOfAllPreviousSimulationsSteps();
	}
	
	private boolean isSimulationRunOverAfter(double simulationRunTimeSoFar)
	{
		return simulationRunTimeSoFar >= maximumSwimmingDuration;
	}
	
	private double getDurationOfNextSimulationStep()
	{
		double maximumDurationOfNextStep = calculateRandomSimulationStepDuration();
		double simulationRunTimeSoFarIncludingNextStep = getSimulationRunTimeSoFarIncluding(maximumDurationOfNextStep);
		if(isSimulationRunOverAfter(simulationRunTimeSoFarIncludingNextStep))
		{
			return getSimulationRunTimeLeft();
		}
		return maximumDurationOfNextStep;
	}
	
	private double calculateRandomSimulationStepDuration()
	{
		return calculateRandomDuration(stepLengthBias);
	}
	
	private double calculateRandomDuration(double durationBias)
	{
		return Math.log(durationBias / nonZeroRandom());
	}
	
	private double nonZeroRandom()
	{
		return 1.0 - Math.random();
	}
	
	private double getSimulationRunTimeLeft()
	{
		return maximumSwimmingDuration - getSumOfAllPreviousSimulationsSteps();
	}
	
	public double getSumOfAllPreviousSimulationsSteps()
	{
		return timeSteps.get(timeSteps.size()-1);
	}
	
	public void resetForNextEscapeRun()
	{
		escapeRoute.reset();
		resetTimeSteps();
		mouse.reset();
		mouse.startSwimming();
	}
	
	private void resetTimeSteps()
	{
		timeSteps.clear();
		timeSteps.add(0.0);
	}
	
	public void setMouseTrainingLevel(double trainingLevel) {
		mouse.setTrainingLevel(trainingLevel);
	}
	
	public boolean isMouseSwimming() {
		return mouse.isSwimming();
	}
	
	public void forEachEscapeRouteSection(Consumer<? super EscapeRouteSection> action)
	{
		escapeRoute.forEachSection(action);
	}
}
