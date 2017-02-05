package com.wisteca.quartzlegion.entities.personnages.combats.equipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.Utils;

public class Armor extends Equipment {
	
	private HashMap<DamageType, Integer> myProtections = new HashMap<>();
	
	public Armor(ItemType type, Classe requiredClasse, HashMap<DamageType, Integer> protections, String name, String description)
	{
		super(type, requiredClasse, name, description);
		
		if(protections == null)
		{
			for(DamageType damage : DamageType.values())
				myProtections.put(damage, 0);
		}
		else
		{
			myProtections = new HashMap<>(protections);
			for(DamageType damage : DamageType.values())
				if(myProtections.containsKey(damage) == false)
					myProtections.put(damage, 0);
		}
		
		updateLore();
	}
	
	public Armor(Element element)
	{
		super(element);
	}
	
	public void setProtection(DamageType protection, int value)
	{
		myProtections.put(protection, value);
		updateLore();
	}
	
	public int getProtection(DamageType protection)
	{
		return myProtections.get(protection);
	}
	
	@Override
	protected void updateLore()
	{
		HashMap<Integer, StringBuilder> loreBuilder = new HashMap<>();
		ArrayList<String> lore = new ArrayList<>(Arrays.asList("§fQualité: §a" + calculQualityLevel(),
				"§cClasse requise: §c§l" + getRequiredClasse().getCleanName(), "",
				"§7Protection:" + (getRequirements().isEmpty() ? "" : "             §cRequis:")));
		
		int i = 0;
		for(DamageType damage : DamageType.values())
		{
			loreBuilder.put(i, new StringBuilder("   §e" + damage.getSpaceName() + ": §b" + myProtections.get(damage)));
			i++;
		}
		
		i = 0;
		for(Skill skill : Skill.values())
		{
			if(getRequirement(skill) == 0)
				continue;
			
			StringBuilder sb = loreBuilder.containsKey(i) ? loreBuilder.get(i) : new StringBuilder("                    ");
			sb.append("     §b" + getRequirement(skill) + " §een " + skill.getCompleteName());
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
			for(String str : Utils.getTextWithLineFeed(myDescription, 45))
				lore.add("§b" + str);
			
		this.setLore(lore);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
		
		Element protection = toWrite.getOwnerDocument().createElement("protection");
		toWrite.appendChild(protection);
		
		for(DamageType type : DamageType.values())
			protection.setAttribute(type.toString(), Integer.toString(getProtection(type)));
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		Element protection = (Element) element.getElementsByTagName("protection").item(0);
		
		myProtections = new HashMap<>();
		for(DamageType type : DamageType.values())
			myProtections.put(type, Integer.valueOf(protection.getAttribute(type.toString())));
		
		updateLore();
	}
	
	@Override
	protected int calculQualityLevel()
	{
		int total = 0;
		for(DamageType damage : DamageType.values())
			total += myProtections.get(damage);
		return total / DamageType.values().length;
	}
}
