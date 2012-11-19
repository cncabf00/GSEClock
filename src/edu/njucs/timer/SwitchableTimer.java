package edu.njucs.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SwitchableTimer {

	Timer timer;
	boolean pause=false;
	int taskPos=0;
	SwitchableTimerListener listener;
	long lastTime=0;
	int mannualDelay=100;
	Map<Integer,Long> timeStamps;
	Integer currentId;

	public SwitchableTimer() {
		timer = new Timer();
		timeStamps=new HashMap<Integer, Long>();
		currentId=0;
	}
	
	private synchronized void timeTick()
	{
		long thisTime=System.nanoTime();
		if (lastTime==0)
			lastTime=thisTime;
		if (!pause)
		{
			synchronized (currentId) {
				long lastTimePastInNano=timeStamps.get(currentId);
				long newTimePastInNano=lastTimePastInNano+thisTime-lastTime;
				timeStamps.put(currentId, newTimePastInNano);
				if (nanoToS(lastTimePastInNano)!=nanoToS(newTimePastInNano))
				{
					if (listener!=null)
						listener.timeTicked(this,currentId, nanoToMill(newTimePastInNano));
				}
			}
		}
		lastTime=thisTime;
	}
	
	public void togglePause()
	{
		if (this.pause)
			resume();
		else
			pause();
	}

	synchronized public void pause() {
		if (this.pause!=true)
		{
			if (listener!=null)
				listener.pauseChanged(this, true);
		}
		this.pause=true;
		lastTime=0;
	}

	synchronized public void resume() {
		if (this.pause!=false)
		{
			if (listener!=null)
				listener.pauseChanged(this, false);
		}
		this.pause=false;
	}
	
	synchronized public void setId(int newId)
	{
		synchronized (currentId) {
			currentId=newId;
			if (!timeStamps.containsKey(currentId))
			{
				timeStamps.put(currentId, 0l);
			}
			lastTime=0;
		}
		if(listener!=null)
			listener.switched(this, currentId, nanoToMill(timeStamps.get(currentId)));
	}
	
	public void clear()
	{
		timeStamps.clear();
	}
	
	public void destory()
	{
		this.timer.cancel();
	}

	public void start() {
		timer=new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				timeTick();
			}
		}, 0, 1);
	}
	
	public void setListener(SwitchableTimerListener listener)
	{
		this.listener=listener;
	}

	
	private int nanoToS(long nanoTime)
	{
		return (int) (nanoTime/1000000000);
	}
	
	private long nanoToMill(long nanoTime)
	{
		return nanoTime/1000000;
	}

}


