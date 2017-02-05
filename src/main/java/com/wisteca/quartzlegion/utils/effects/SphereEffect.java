package com.wisteca.quartzlegion.utils.effects;

import org.bukkit.Location;
import org.bukkit.Particle;

public class SphereEffect extends AOEffect {
	
	private double myExtension = 20;
	
	public void setExtension(double extension)
	{
		myExtension = extension;
		
	}
	
	public double getExtension()
	{
		return myExtension;
	}
	
	@Override
	public void launch(Location center)
	{
		//Random r = new Random();
		for(int i = 0; i < 10 * myExtension + 1; i++)
		{
			System.out.println("caca");
			Location loc = new Location(center.getWorld(), center.getX() + ((180 * Math.sin(i) / Math.PI) / myExtension),
					center.getY(), center.getZ() + ((180 * Math.cos(i) / Math.PI) / myExtension));
			
			center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1);
		}
	}
	
	@Override
	public void doTime()
	{
		
	}
}
