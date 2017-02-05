package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Channel;

public abstract class AreaPouvoir extends AttackPouvoir {

	public AreaPouvoir(Personnage attacker)
	{
		super(attacker);
	}
	
	@Override
	public boolean launch()
	{
		if(super.launch() == false)
			return false;
		
		Vector lookDirection = myAttacker.getEyeLocation().getDirection();
		int distance = 50;
		lookDirection.multiply(distance);
		
		for(distance = 0 ; distance <= 50 ; distance++)
		{
			Location loc = lookDirection.toLocation(myAttacker.getWorld());
			if(loc.getBlock().equals(Material.AIR) == false)
			{
				launchAt(loc);
				break;
			}
		}
		
		if(distance == 50)
		{
			myAttacker.sendMessage(Channel.COMBAT, "Vous regardez trop loin !");
			return false;
		}
		
		return true;
	}
	
	protected abstract void launchAt(Location toLaunch);
	
}
