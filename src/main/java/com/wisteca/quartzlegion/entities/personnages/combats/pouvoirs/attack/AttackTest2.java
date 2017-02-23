package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack;

import java.util.HashMap;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.TargetedPouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;

public class AttackTest2 extends TargetedPouvoir {

	public AttackTest2(Personnage attacker)
	{
		super(attacker);
		// TODO Auto-generated constructor stub
	}
	
	public AttackTest2(Element element)
	{
		super(element);
	}

	@Override
	public Item getRepresentativeItem()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "AttackTest2";
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoadingTime()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEnergyCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HashMap<Skill, Integer> getRequirements()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Classe getRequiredClasse()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doTime()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void launchOn(Personnage toAttack)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAutoOnSelf()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WeaponType getWeaponType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDuration()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
