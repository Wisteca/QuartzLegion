package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage;

/**
 * Repr�sente un pouvoir qui affecte au autre personnage.
 * @author Wisteca
 */

public abstract class TargetedPouvoir extends AttackPouvoir {

	public TargetedPouvoir(Personnage attacker)
	{
		super(attacker);
	}
	
	public TargetedPouvoir(Element element)
	{
		super(element);
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
			
			launchOn(getAttacker().getSelectedPersonnage());
			return true;
		}	
		
		return false;
	}
	
	/**
	 * M�thode � red�finir dans les classes filles appel�e lorsque le pouvoir est lanc�.
	 * @param toAttack le personnage pris pour cible par l'attaquant
	 */
	
	protected abstract void launchOn(Personnage toAttack);
}
