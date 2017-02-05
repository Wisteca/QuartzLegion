package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import com.wisteca.quartzlegion.entities.personnages.Personnage;

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
			if(myAttacker.getSelectedPersonnage() == null)
			{
				myAttacker.sendMessage("Vous n'avez aucune cible !");
				return false;
			}
			
			launchOn(myAttacker.getSelectedPersonnage());
			return true;
		}	
		
		return false;
	}
	
	protected abstract void launchOn(Personnage toAttack);
}
