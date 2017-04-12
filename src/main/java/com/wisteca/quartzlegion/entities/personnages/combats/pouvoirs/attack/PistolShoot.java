package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.ViewTargetPouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.ItemType;

/**
 * Simple tire au pistolet.
 * @author Wisteca
 */

public class PistolShoot extends ViewTargetPouvoir {

	public PistolShoot(Personnage attacker)
	{
		super(attacker);
	}

	@Override
	protected void launchOn(List<Personnage> targets)
	{
		getAttacker().getWorld().playSound(getAttacker().getLocation(), Sound.ITEM_FIRECHARGE_USE, 1, 1);
		Location bulletLoc = null;
		for(double d = 1 ; d < getMaxViewDistance() ; d += 0.5)
		{
			bulletLoc = getAttacker().getEyeLocation().add(getAttacker().getEyeLocation().getDirection().normalize().multiply(d));
			if(targets.size() > 0 && targets.get(0).getLocation().distance(bulletLoc) < 2)
				break;
			getAttacker().getWorld().spawnParticle(Particle.TOWN_AURA, bulletLoc, 1);
		}
		
		if(targets.size() > 0)
		{
			Personnage target = targets.get(0);
			Location attackerLoc = getAttacker().getLocation();
			
			Damage damage = getAttacker().getAttackPower(getWeaponType());
			damage.addDamages(DamageType.PERFORATION, 10);
			
			target.damage(damage);
			target.getWorld().spawnParticle(Particle.BLOCK_CRACK, bulletLoc, 20, new MaterialData(Material.REDSTONE_BLOCK));
			target.setVelocity(target.getLocation().toVector().subtract(new Vector(attackerLoc.getX(), attackerLoc.getY(), attackerLoc.getZ())).normalize().setY(0.3));
		}
	}
	
	@Override
	public Item getRepresentativeItem()
	{
		return new Item(ItemType.GOLD_SPADE, 1, false, getName(), null, true, false);
	}

	@Override
	public String getName()
	{
		return "§2Tir au pistolet";
	}

	@Override
	public String getDescription()
	{
		return "Tirer sur l'ennemi avec ce flingue";
	}

	@Override
	public int getLoadingTime()
	{
		return 10;
	}

	@Override
	public int getEnergyCost()
	{
		return 1;
	}

	@Override
	public HashMap<Skill, Integer> getRequirements()
	{
		return null;
	}

	@Override
	public Classe getRequiredClasse()
	{
		return Classe.AUCUN;
	}

	@Override
	public Item getBullet()
	{
		return new Item(ItemType.SPIDER_EYE);
	}

	@Override
	public int getMaxViewDistance()
	{
		return 100;
	}

	@Override
	public WeaponType getWeaponType()
	{
		return WeaponType.FEU;
	}

	@Override
	public int getDuration()
	{
		return 2;
	}
	
	@Override
	public boolean canCrossThroughBlocks()
	{
		return false;
	}
}
