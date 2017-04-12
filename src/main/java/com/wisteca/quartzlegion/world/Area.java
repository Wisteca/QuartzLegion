package com.wisteca.quartzlegion.world;

import java.util.ArrayList;

import org.bukkit.Location;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Serializer;

/**
 * Représente une zone dans laquelle des priorités peuvent être établies.
 * @author Wisteca
 */

public class Area implements Serializer {
	
	private ArrayList<Location> myBorders = new ArrayList<>();
	private int myBlocksBreakRate, myReplaceRadius, myReplaceTime;
	private PvPMode myPvPMode;
	private String myName;
	//private ArrayList<Location> myInteractiveObjects = new ArrayList<>();  A REVOIR ??
	
	public Area(String name)
	{
		myName = name;
	}
	
	@Override
	public void serialize(Element toWrite)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deserialize(Element element)
	{
		// TODO Auto-generated method stub
		
	}
	
	public boolean isInArea(Location loc)
	{
		return false;
	}
	
	public void addBorder(Location loc)
	{
		myBorders.add(loc);
	}
	
	public void removeBorder(Location loc)
	{
		myBorders.remove(loc);
	}
	
	public ArrayList<Location> getBorders()
	{
		return new ArrayList<>(myBorders);
	}
	
	public void setBlocksBreakRate(int rate)
	{
		myBlocksBreakRate = rate;
	}
	
	public int getBlocksBreakRate()
	{
		return myBlocksBreakRate;
	}
	
	public void setReplaceRadius(int radius)
	{
		myReplaceRadius = radius;
	}
	
	public int getReplaceRadius()
	{
		return myReplaceRadius;
	}
	
	public void setReplaceTime(int time)
	{
		myReplaceTime = time;
	}
	
	public int getReplaceTime()
	{
		return myReplaceTime;
	}
	
	public void setPvPMode(PvPMode mode)
	{
		myPvPMode = mode;
	}
	
	public PvPMode getPvPMode()
	{
		return myPvPMode;
	}
	
	public void setName(String name)
	{
		myName = name;
	}
	
	public String getName()
	{
		return myName;
	}
	
	public static enum PvPMode {
		
		ACTIVATED,
		DEFAULT,
		DISABLED;
		
	}
}
