package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Channel;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;

public abstract class AttackPouvoir implements OfficialPouvoir {
	
	protected Personnage myAttacker;
	
	public AttackPouvoir(Personnage attacker)
	{
		myAttacker = attacker;
	}
	
	@Override
	public boolean launch()
	{
		for(Skill skill : getRequirements().keySet())
		{
			if(getRequirements().get(skill) > myAttacker.getTemporarySkill(skill))
			{
				myAttacker.sendMessage(Channel.COMBAT, "Vous n'avez pas les compétences requises pour utiliser ce pouvoir.");
				return false;
			}
		}
		
		if(getEnergyCost() > myAttacker.getEnergy())
		{
			myAttacker.sendMessage(Channel.COMBAT, "Vous n'avez pas assez d'énergie !");
			return false;
		}
		
		return true;
	}
	
	public abstract boolean isAutoOnSelf();
	
	public abstract WeaponType getWeaponType();
	
	public abstract HashMap<DamageType, Integer> getDamages();
	
	public abstract Item getRequiredItem();
	
	public abstract String toString();
}
