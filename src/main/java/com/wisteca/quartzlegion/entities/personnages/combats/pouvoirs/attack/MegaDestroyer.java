package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AreaPouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.HabilitySkill;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.Utils;

public class MegaDestroyer extends AreaPouvoir {
	
	private Location myLaunchLoc;
	private boolean myIsActivated = false;
	private double myIncrease = 7, myHauteur;
	private int myDegre, myTime;
	
	private ArrayList<Personnage> myPrisoners = new ArrayList<>();
	private ArrayList<FallingBlock> myMeteors = new ArrayList<>();
	
	public MegaDestroyer(Personnage attacker)
	{
		super(attacker);
	}
	
	@Override
	protected void launchAt(Location toLaunch)
	{
		myLaunchLoc = toLaunch;
		myIsActivated = true;
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		if(myIsActivated == false)
			return;
		
		for(Personnage perso : PersonnageManager.getInstance().getPersonnages())
		{
			if(isInArea(perso) && myPrisoners.contains(perso) == false)
				myPrisoners.add(perso);
		}
		
		if(myTime < 185)
		{
			for(Personnage perso : myPrisoners)
			{
				if(isInArea(perso) == false)
				{
					myLaunchLoc.getWorld().spawnParticle(Particle.CRIT, perso.getEyeLocation(), 20);
					myLaunchLoc.getWorld().playSound(perso.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
					perso.damage(10, DamageType.BRULURE);
					perso.setVelocity(myLaunchLoc.clone().add(0, 1, 0).toVector().subtract(perso.getLocation().toVector()).normalize().multiply(0.3));
				}
			}
		}
		
		for(double y = 0 ; y < myHauteur ; y += 0.2)
		{
			myDegre += 5;
			myLaunchLoc.getWorld().spawnParticle(Particle.DRIP_LAVA, getLocAround(myDegre).add(0, y, 0), 1);
		}
		
		if(myHauteur < 5)
			myHauteur += 0.05;		
		myIncrease -= myIncrease * 0.01;
		myTime++;
		
		if(myTime == 170)
		{
			Random rand = new Random();
			for(int i = 0 ; i < 10 ; i++)
			{
				FallingBlock meteor = myLaunchLoc.getWorld().spawnFallingBlock(myLaunchLoc.clone().add(15 - rand.nextInt(30), 30, 15 - rand.nextInt(30)), new MaterialData(Material.MAGMA));
				meteor.setVelocity(myLaunchLoc.toVector().subtract(meteor.getLocation().toVector()).normalize().multiply(2));
				meteor.setDropItem(false);
				myMeteors.add(meteor);
				myLaunchLoc.getWorld().playSound(myLaunchLoc, Sound.ENTITY_SHULKER_SHOOT, 1, 1);
			}
		}
		
		if(myTime == 185)
		{
			Random rand = new Random();
			myLaunchLoc.getWorld().createExplosion(myLaunchLoc, 5F, true);
			
			for(FallingBlock meteor : myMeteors)
				meteor.setVelocity(myLaunchLoc.clone().add(15 - rand.nextInt(30), 2, 15 - rand.nextInt(30)).toVector().subtract(meteor.getLocation().toVector()).normalize().multiply(2));
			
			for(Personnage perso : myPrisoners)
			{
				perso.setVelocity(new Vector(20 - rand.nextInt(40), 1, 20 - rand.nextInt(40)));
				perso.damage(100, DamageType.BRULURE, DamageType.FRACAS);
			}
		}
		
		if(myIncrease <= 1)
		{
			myIsActivated = false;
			myIncrease = 7;
			myHauteur = 0;
			myTime = 0;
			myPrisoners = new ArrayList<>();
		}
	}

	private boolean isInArea(Personnage perso)
	{
		Location persLoc = perso.getLocation();
		for(int i = 0 ; i <= 360 ; i += 5)
		{
			Location aroundLoc = getLocAround(i);
			if(Utils.isInside(persLoc, myLaunchLoc, aroundLoc.add(0, 5, 0)))
				return true;
		}
		return false;
	}
	
	private Location getLocAround(int degre)
	{
		return myLaunchLoc.clone().add(myIncrease * Math.cos(Math.toRadians(degre)), 0, myIncrease * Math.sin(Math.toRadians(degre)));
	}
	
	@Override
	public int getDuration()
	{
		return 200;
	}
	
	@Override
	public Item getRepresentativeItem()
	{
		return new Item(ItemType.ANVIL, 1, false, "§3Méga-Destroyer", null, false, true);
	}

	@Override
	public String getName()
	{
		return "Méga-Destroyer";
	}

	@Override
	public String getDescription()
	{
		return "Ce pouvoir tue ses ennemis dans une tornade de pierre.";
	}

	@Override
	public int getLoadingTime()
	{
		return 100;
	}

	@Override
	public int getEnergyCost()
	{
		return 20;
	}

	@Override
	public HashMap<Skill, Integer> getRequirements()
	{
		HashMap<Skill, Integer> requirements = new HashMap<>();
		requirements.put(HabilitySkill.FORCE, 25);
		return requirements;
	}

	@Override
	public Classe getRequiredClasse()
	{
		return Classe.AUCUN;
	}

	@Override
	public int getMaxLaunchDistance()
	{
		return 50;
	}

	@Override
	public boolean isAutoOnSelf()
	{
		return false;
	}

	@Override
	public WeaponType getWeaponType()
	{
		return WeaponType.AUCUN;
	}	
}
