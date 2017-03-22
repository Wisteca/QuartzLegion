package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import com.wisteca.quartzlegion.entities.personnages.Personnage;

/**
 * Représente un pouvoir qui affecte au autre personnage.
 * @author Wisteca
 */

public abstract class TargetedPouvoir extends AttackPouvoir {

	public TargetedPouvoir(Personnage attacker)
	{
		super(attacker);
	}
	
	@Override
	public boolean launch()
	{
		if(super.launch())
		{
			if(getAttacker().getSelectedPersonnage() == null)
			{
				getAttacker().sendMessage("Vous n'avez aucune cible !");
				return false;
			}
			
			getAttacker().changeEnergy(-getEnergyCost());
			launchOn(getAttacker().getSelectedPersonnage());
			return true;
		}	
		
		return false;
	}
	
	/**
	 * Méthode à redéfinir dans les classes filles appelée lorsque le pouvoir est lancé.
	 * @param toAttack le personnage pris pour cible par l'attaquant
	 */
	
	protected abstract void launchOn(Personnage toAttack);
}
