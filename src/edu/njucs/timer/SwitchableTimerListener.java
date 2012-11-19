package edu.njucs.timer;

public interface SwitchableTimerListener {
	public void timeTicked(SwitchableTimer sender,int id, long currentTimeInMs);
	
	public void switched(SwitchableTimer sender,int toId, long currentTimeInMs);
	
	public void pauseChanged(SwitchableTimer sender,boolean paused);
}
