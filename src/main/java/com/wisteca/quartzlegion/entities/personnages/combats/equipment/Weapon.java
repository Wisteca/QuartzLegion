package com.wisteca.quartzlegion.entities.personnages.combats.equipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.Utils;

public class Weapon extends Equipment {
	
	private WeaponType myWeaponType;
	private HashMap<DamageType, WeaponDamage> myDamages = new HashMap<>();
	private int myCriticalLuck, myCriticalDamages;
	
	public Weapon(ItemType type, WeaponType weapon, Classe requiredCLasse, HashMap<DamageType, WeaponDamage> damages,
			int criticalLuck, int criticalDamage, String name, String description)
	{
		super(type, requiredCLasse, name, description);
		myWeaponType = weapon;
		myCriticalLuck = criticalLuck;
		myCriticalDamages = criticalDamage;
		
		if(damages == null)
		{
			for(DamageType damage : DamageType.values())
				myDamages.put(damage, new WeaponDamage(0, 0));
		}
		else
		{
			myDamages = new HashMap<>(damages);
			for(DamageType damage : DamageType.values())
				if(myDamages.containsKey(damage) == false)
					myDamages.put(damage, new WeaponDamage(0, 0));
		}
		
		updateLore();
	}
	
	public Weapon(Element element)
	{
		super(element);
	}
	
	@Override
	protected void updateLore()
	{
		HashMap<Integer, StringBuilder> loreBuilder = new HashMap<>();
		List<String> lore = new ArrayList<>(Arrays.asList("§cType: §4" + getWeaponType().getCleanName(),
				"§fQualité: §a" + calculQualityLevel(), "§cClasse requise: §c§l" + getRequiredClasse().getCleanName(),
				"§5Chances de critiques: §f" + getCriticalLuck(), "§5Dégâts critiques: §f" + getCriticalDamages(), "",
				"§7Dégâts:" + (getRequirements().isEmpty() ? "" : "                    §cRequis:")));
		
		int i = 0;
		for(DamageType damage : DamageType.values())
		{
			loreBuilder.put(i, new StringBuilder("   §e" + damage.getSpaceName() + ": §b" + myDamages.get(damage).getMinDamage()
					+ " - " + myDamages.get(damage).getMaxDamage()));
			i++;
		}
		
		i = 0;
		for(Skill skill : Skill.values())
		{
			if(getRequirement(skill) == 0)
				continue;
			
			StringBuilder sb = loreBuilder.containsKey(i) ? loreBuilder.get(i) : new StringBuilder("                        ");
			sb.append("     §c" + getRequirement(skill) + " §een " + skill.getCompleteName());
			loreBuilder.put(i, sb);
			i++;
		}
		
		for(StringBuilder strBuilder : loreBuilder.values())
			lore.add(strBuilder.toString());
		
		lore.add("");
		
		if(getIncreases().isEmpty() == false)
		{
			lore.add("§6Augmentations:");
			for(Skill skill : Skill.values())
			{
				if(getIncrease(skill) == 0)
					continue;
				
				lore.add("   §6" + getIncrease(skill) + " §een " + skill.getCompleteName());
			}
		}
		
		if(myDescription != null)
			for(String str : Utils.getTextWithLineFeed(myDescription, 50))
				lore.add("§b" + str);
		
		this.setLore(lore);
	}
	
	@Override
	protected int calculQualityLevel()
	{
		int total = 0;
		for(DamageType damage : DamageType.values())
			total += ((myDamages.get(damage).getMaxDamage() - myDamages.get(damage).getMinDamage()) / 2)
					+ myDamages.get(damage).getMinDamage();
		return total / DamageType.values().length;
	}
	
	public WeaponType getWeaponType()
	{
		return myWeaponType;
	}
	
	public void setWeaponType(WeaponType type)
	{
		myWeaponType = type;
		updateLore();
	}
	
	public void setDamage(DamageType type, WeaponDamage damages)
	{
		myDamages.put(type, damages);
		updateLore();
	}
	
	public WeaponDamage getDamages(DamageType type)
	{
		return myDamages.get(type);
	}
	
	public int getRandomDamages(DamageType type)
	{
		return new Random().nextInt(myDamages.get(type).getMaxDamage() - myDamages.get(type).getMinDamage())
				+ myDamages.get(type).getMinDamage();
	}
	
	public void setCriticalLuck(int luck)
	{
		myCriticalLuck = luck;
		updateLore();
	}
	
	public void setCriticalDamages(int damages)
	{
		myCriticalDamages = damages;
		updateLore();
	}
	
	public int getCriticalLuck()
	{
		return myCriticalLuck;
	}
	
	public int getCriticalDamages()
	{
		return myCriticalDamages;
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
		
		toWrite.setAttribute("weaponType", getWeaponType().toString());
		toWrite.setAttribute("criticalLuck", Integer.toString(getCriticalLuck()));
		toWrite.setAttribute("criticalDamages", Integer.toString(getCriticalDamages()));
		
		Element damages = toWrite.getOwnerDocument().createElement("damages");
		toWrite.appendChild(damages);
		
		for(DamageType type : DamageType.values())
			damages.setAttribute(type.toString(), Integer.toString(getDamages(type).getMinDamage()) + "-" + Integer.toString(getDamages(type).getMaxDamage()));
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		myWeaponType = WeaponType.valueOf(element.getAttribute("weaponType"));
		myCriticalLuck = Integer.valueOf(element.getAttribute("criticalLuck"));
		myCriticalDamages = Integer.valueOf(element.getAttribute("criticalDamages"));
		
		Element damages = (Element) element.getElementsByTagName("damages").item(0);
		
		myDamages = new HashMap<>();
		for(DamageType type : DamageType.values())
		{
			String[] minAndMax = damages.getAttribute(type.toString()).split("-");
			myDamages.put(type, new WeaponDamage(Integer.valueOf(minAndMax[0]), Integer.valueOf(minAndMax[1])));
		}
		
		updateLore();
	}
	
	public static class WeaponDamage {
		
		private int myMinDamage, myMaxDamage;
		
		public WeaponDamage(int minDamage, int maxDamage)
		{
			myMinDamage = minDamage;
			myMaxDamage = maxDamage;
		}
		
		public int getMinDamage()
		{
			return myMinDamage;
		}
		
		public int getMaxDamage()
		{
			return myMaxDamage;
		}
	}
	
	public static enum WeaponType {
		
		CORDE("Corde"), 
		FEU("Feu"),
		LAME("Lame"), 
		ARME_LONGUE("Arme longue"), 
		ARME_LOURDE("Arme lourde"),
		DEMONIAQUE("Démoniaque"),
		ELEMENTAIRE("Élémentaire"), 
		SANG("Sang");
		
		private String myCleanName;
		
		private WeaponType(String cleanName)
		{
			myCleanName = cleanName;
		}
		
		public String getCleanName()
		{
			return myCleanName;
		}
	}
}
