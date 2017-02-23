package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack;

import java.util.HashMap;

import org.bukkit.Location;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AreaPouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;

public class AttackTest1 extends AreaPouvoir {
	
	public AttackTest1(Personnage attacker)
	{
		super(attacker);
		// TODO Auto-generated constructor stub
	}
	
	public AttackTest1(Element element)
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
		return "AttackTest1";
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
	protected void launchAt(Location toLaunch)
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
	public HashMap<Skill, Integer> getRequirements()
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
