package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import com.wisteca.quartzlegion.entities.personnages.Personnage;

public abstract class OverTimePouvoir extends SpacePouvoir {

	private int myCurrentTime, myBetweenTime;
	
	public OverTimePouvoir(Personnage perso, int totalTime, int betweenTime, int size, String name)
	{
		super(perso, totalTime, size, name);
		myBetweenTime = betweenTime;
		myCurrentTime = 0;
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		if(myCurrentTime == myBetweenTime)
		{
			action();
			myCurrentTime = 0;
		}
		
		myCurrentTime++;
	}
	
	protected abstract void action();
}
