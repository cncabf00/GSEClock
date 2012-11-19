package edu.njucs.timer;

public interface GSEClockListener {
	public void paused();
	
	public void resumed();
	
	public void phaseChanged(int currentPhase);
	
	public void end();
	
	public void exit();
}
