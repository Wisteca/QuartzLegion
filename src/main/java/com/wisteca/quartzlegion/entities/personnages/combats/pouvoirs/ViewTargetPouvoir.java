package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.wisteca.quartzlegion.entities.personnages.Joueur;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Channel;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.Utils;

/**
 * Représente un pouvoir qui cible les personnages se trouvant dans la trajectoire de vision de l'attaquant.
 * @author Wisteca
 */

public abstract class ViewTargetPouvoir extends AttackPouvoir {

	public ViewTargetPouvoir(Personnage attacker)
	{
		super(attacker);
	}
	
	@Override
	public boolean launch()
	{
		if(super.launch())
		{
			List<Personnage> targets = new ArrayList<>();
			
			for(int distance = 2 ; distance < getMaxViewDistance() ; distance++)
			{
				Location loc = getAttacker().getEyeLocation().add(getAttacker().getEyeLocation().getDirection().normalize().multiply(distance));
				
				if(canCrossThroughBlocks() == false && loc.getBlock().getType().isTransparent() == false)
					break;
				
				for(Personnage perso : getAttacker().getNearbyPersonnages(getMaxViewDistance()))
				{
					if(Utils.isInside(loc, perso.getLocation().add(0.5, 0, 0.5), perso.getLocation().add(-0.5, 2, -0.5)))
						targets.add(perso);
				}
			}
			
			if(getBullet() != null && getAttacker() instanceof Joueur)
			{
				Joueur attacker = (Joueur) getAttacker();
				for(ItemStack item : attacker.getInventory().getContents())
				{
					if(item != null && item.isSimilar(getBullet().toItemStack()))
					{
						if(item.getAmount() == 1)
							attacker.getInventory().remove(item);
						else
							item.setAmount(item.getAmount() - 1);
						
						getAttacker().changeEnergy(-getEnergyCost());
						launchOn(targets);
						return true;
					}
				}
				
				getAttacker().sendMessage(Channel.COMBAT, "Vous n'avez plus de munitions !");
				return false;
			}
			
			getAttacker().changeEnergy(-getEnergyCost());
			launchOn(targets);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Appelé lorsque le pouvoir se lance.
	 * @param targets 
	 */
	
	protected abstract void launchOn(List<Personnage> targets);
	
	/**
	 * L'item requit au personnage qui lance le pouvoir pour pouvoir le lancer, si il l'a,, il le perd en lançant le pouvoir.
	 * @return l'item requit pour lancer le pouvoir
	 */
	
	public abstract Item getBullet();
	
	/**
	 * @return la distance maximum à laquelle le personnage peut "tirer"
	 */
	
	public abstract int getMaxViewDistance();
	
	/**
	 * @return true si les personnages se trouvant derrière des blocs sont touchés parf l'attaque
	 */
	
	public abstract boolean canCrossThroughBlocks();
	
	@Override
	public boolean isAutoOnSelf()
	{
		return false;
	}
}
