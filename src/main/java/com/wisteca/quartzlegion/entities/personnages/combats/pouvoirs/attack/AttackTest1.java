package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Particle;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AreaPouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.ItemType;

public class AttackTest1 extends AreaPouvoir {
	
	public AttackTest1(Personnage attacker)
	{
		super(attacker);
	}
	
	@Override
	public void doTime()
	{}

	@Override
	protected void launchAt(Location toLaunch)
	{
		getAttacker().sendMessage(toLaunch.toString());
		getAttacker().getWorld().spawnParticle(Particle.CRIT_MAGIC, toLaunch, 54);
	}
	
	@Override
	public Item getRepresentativeItem()
	{
		return new Item(ItemType.ANVIL);
	}

	@Override
	public String getName()
	{
		return "AttackTest1";
	}

	@Override
	public String getDescription()
	{
		return "LALALALALA";
	}

	@Override
	public int getLoadingTime()
	{
		return 20;
	}

	@Override
	public int getEnergyCost()
	{
		return 21;
	}
	
	@Override
	public Classe getRequiredClasse()
	{
		return null;
	}

	@Override
	public boolean isAutoOnSelf()
	{
		return false;
	}

	@Override
	public WeaponType getWeaponType()
	{
		return WeaponType.DEMONIAQUE;
	}

	@Override
	public HashMap<Skill, Integer> getRequirements()
	{
		return new HashMap<>();
	}

	@Override
	public int getDuration()
	{
		return 20;
	}

	@Override
	public int getMaxLaunchDistance()
	{
		return 50;
	}
}
