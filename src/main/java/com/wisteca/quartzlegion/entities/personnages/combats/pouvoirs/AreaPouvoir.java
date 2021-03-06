package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import org.bukkit.Location;
import org.bukkit.Material;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Channel;

/**
 * Repr�sente un pouvoir qui affecte une zone, cibl�e par le personnage ou sur lui-m�me.
 * @author Wisteca
 */

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
		
		Location launchLoc = null;
		
		if(isAutoOnSelf())
			launchLoc = getAttacker().getLocation();
		else
		{
			for(int distance = 1 ; distance <= getMaxLaunchDistance() ; distance++)
			{
				Location loc = getAttacker().getEyeLocation().add(getAttacker().getEyeLocation().getDirection().normalize().multiply(distance));
				
				if(loc.getBlock().getType().equals(Material.AIR) == false)
				{
					launchLoc = loc;
					break;
				}
			}
			
			if(launchLoc == null)
			{
				getAttacker().sendMessage(Channel.COMBAT, "Vous regardez trop loin !");
				return false;
			}
		}
		
		getAttacker().changeEnergy(-getEnergyCost());
		launchAt(launchLoc);
		return true;
	}
	
	/**
	 * @return la distance maximum � laquelle le personnage peut lancer le pouvoir
	 */
	
	public abstract int getMaxLaunchDistance();
	
	/**
	 * M�thode � red�finir dans les classes filles appel�e quand un pouvoir est lanc�.
	 * @param toLaunch la position ou le personnage regarde et veut que le pouvoir se d�clenche
	 */
	
	protected abstract void launchAt(Location toLaunch);
	
}
