package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Channel;

/**
 * Représente un pouvoir qui affecte une zone, ciblée par le personnage ou sur lui-même.
 * @author Wisteca
 */

public abstract class AreaPouvoir extends AttackPouvoir {
	
	public AreaPouvoir(Personnage attacker)
	{
		super(attacker);
	}
	
	public AreaPouvoir(Element element)
	{
		super(element);
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
				Location loc = getAttacker().getEyeLocation().add(getAttacker().getEyeLocation().getDirection().multiply(distance));
				getAttacker().sendMessage("block : " + loc.getBlock().getType());
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
		
		launchAt(launchLoc);
		return true;
	}
	
	/**
	 * @return la distance maximum à laquelle le personnage peut lancer le pouvoir
	 */
	
	public abstract int getMaxLaunchDistance();
	
	/**
	 * Méthode à redéfinir dans les classes filles appelée quand un pouvoir est lancé.
	 * @param toLaunch la position ou le personnage regarde et veut que le pouvoir se déclenche
	 */
	
	protected abstract void launchAt(Location toLaunch);
	
}
