package com.wisteca.quartzlegion.commands.megacopier;

import org.bukkit.Location;

public class Copier {
	
	private Location myFirstPoint, mySecondPoint;
	
	public void setFirstPoint(Location loc)
	{
		myFirstPoint = loc;
	}
	
	public void setSecondPoint(Location loc)
	{
		mySecondPoint = loc;
	}
	
	public Location getFirstPoint()
	{
		return myFirstPoint;
	}
	
	public Location getSecondPoint()
	{
		return mySecondPoint;
	}
	
	public int getRemainingTime()
	{
		return 0;
	}
	
	public boolean start()
	{
		
		return true;
	}
	
	
}















