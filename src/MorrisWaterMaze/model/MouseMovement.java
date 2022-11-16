package MorrisWaterMaze.model;

import MorrisWaterMaze.util.Calculations;
import MorrisWaterMaze.graphics.Paintable;
import MorrisWaterMaze.parameter.MouseParameterAccessor;
import MorrisWaterMaze.util.Point;

import java.awt.geom.Ellipse2D;
import java.sql.SQLOutput;
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
		
		double simulationRunTimeSoFar = durationOfCurrentSimulationStep + getSumOfAllPreviousSimulationsSteps();
		
		/*
		if(Double.compare(durationOfCurrentSimulationStep, getSimulationRunTimeLeft())==0) //isSimulationRunOverAfter(simulationRunTimeSoFar)
		{
			System.out.println("Differenz: " + (Math.abs(maximumSwimmingDuration - simulationRunTimeSoFar)));
			mouse.stopSwimming();
		}*/
		
		timeSteps.add(simulationRunTimeSoFar);
	}
	
	private boolean isSimulationRunOverAfter(double simulationRunTimeSoFar)
	{
		
		return true; // Calculations.fuzzyEqualsForDouble(maximumSwimmingDuration, simulationRunTimeSoFar, 0.001);
	}
	
	private double getDurationOfNextSimulationStep()
	{
		double maximumDurationOfNextSimulationStep = calculateRandomSimulationStepDuration();
		if(hasReachedSwimmingTimeLimitAfter(maximumDurationOfNextSimulationStep))
		{
			mouse.stopSwimming();
			return getSimulationRunTimeLeft();
		}
		return maximumDurationOfNextSimulationStep;
	}
	
	private double calculateRandomSimulationStepDuration()
	{
		return Calculations.calculateRandomDuration(stepLengthBias);
	}
	
	private double getSimulationRunTimeLeft()
	{
		return maximumSwimmingDuration - getSumOfAllPreviousSimulationsSteps();
	}
	
	private boolean hasReachedSwimmingTimeLimitAfter(double maximumDurationOfNextSimulationStep)
	{
		return hasMaximumSwimmingTimeBeenSet()
			&& maximumDurationOfNextSimulationStep + getSumOfAllPreviousSimulationsSteps() > maximumSwimmingDuration;
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
	
	private boolean hasMaximumSwimmingTimeBeenSet()
	{
		return maximumSwimmingDuration != 0;
	}
}
