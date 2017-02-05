package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage;

public abstract class SpacePouvoir implements Pouvoir {
	
	private int myTotalTime, myRemainingTime, mySize;
	private Personnage myPersonnage;
	private String myName;
	
	public SpacePouvoir(Personnage perso, int totalTime, int size, String name)
	{
		myPersonnage = perso;
		myTotalTime = totalTime;
		myRemainingTime = 0;
		mySize = size;
		myName = name;
	}
	
	public SpacePouvoir(Element element)
	{
		deserialize(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		toWrite.setAttribute("name", myName);
		toWrite.setAttribute("remainingTime", Integer.toString(myRemainingTime));
	}
	
	@Override
	public void deserialize(Element element)
	{
		
	}
	
	@Override
	public boolean launch()
	{
		if(myRemainingTime == 0)
		{
			myRemainingTime = myTotalTime;
			myPersonnage.addSpacePouvoir(this);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void doTime()
	{
		if(myRemainingTime == 0)
		{
			myPersonnage.removeSpacePouvoir(this);
			end();
		}
		
		myRemainingTime--;
	}
	
	protected abstract void end();
	
	public int getTotalTime()
	{
		return myTotalTime;
	}
	
	public int getRemainingTime()
	{
		return myRemainingTime;
	}
	
	public int getSize()
	{
		return mySize;
	}
	
	public Personnage getPersonnage()
	{
		return myPersonnage;
	}
	
	public String getName()
	{
		return myName;
	}
}
