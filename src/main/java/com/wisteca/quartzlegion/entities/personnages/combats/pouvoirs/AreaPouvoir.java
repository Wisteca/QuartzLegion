package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.w3c.dom.Element;

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
			for(int distance = 0 ; distance <= 50 ; distance++)
			{
				Location loc = getAttacker().getEyeLocation().getDirection().multiply(distance).toLocation(getAttacker().getWorld());
				if(loc.getBlock().equals(Material.AIR) == false)
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
	 * M�thode � red�finir dans les classes filles appel�e quand un pouvoir est lanc�.
	 * @param toLaunch la position ou le personnage regarde et veut que le pouvoir se d�clenche
	 */
	
	protected abstract void launchAt(Location toLaunch);
	
}
