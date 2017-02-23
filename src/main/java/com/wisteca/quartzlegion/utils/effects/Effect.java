package com.wisteca.quartzlegion.utils.effects;

import org.bukkit.Location;

public interface Effect {
	
	public void launch(Location center);
	
	public void doTime();

	public String getName();
	
	public static Effect getEffectByName(String name)
	{
		return null;
	}
}
