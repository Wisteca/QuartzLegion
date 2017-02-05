package com.wisteca.quartzlegion.entities.personnages.combats.equipment;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.ItemType;

public abstract class Equipment extends Item {
	
	protected String myDescription;
	private int myRequiredLevel;
	private Classe myRequiredClasse;
	private HashMap<Skill, Integer> myRequirements = new HashMap<>();
	private HashMap<Skill, Integer> myIncreases = new HashMap<>();
	
	public Equipment(ItemType type, Classe requiredClasse, String name, String description)
	{
		super(type);
		this.setName(name);
		myDescription = description;
		myRequiredClasse = requiredClasse;
	}
	
	public Equipment(Element element)
	{
		super(element);
	}
	
	public void setRequirements(HashMap<Skill, Integer> requirements)
	{
		myRequirements = new HashMap<>(requirements);
		updateLore();
	}
	
	public void addRequirement(Skill skill, int value)
	{
		myRequirements.put(skill, value);
		updateLore();
	}
	
	public void removeRequirement(Skill skill)
	{
		myRequirements.remove(skill);
		updateLore();
	}
	
	public HashMap<Skill, Integer> getRequirements()
	{
		return new HashMap<>(myRequirements);
	}
	
	public int getRequirement(Skill skill)
	{
		return myRequirements.containsKey(skill) ? myRequirements.get(skill) : 0;
	}
	
	public void setRequiredLevel(int level)
	{
		myRequiredLevel = level;
		updateLore();
	}
	
	public int getRequiredLevel()
	{
		return myRequiredLevel;
	}
	
	public void setIncreases(HashMap<Skill, Integer> increases)
	{
		myIncreases = new HashMap<>(increases);
		updateLore();
	}
	
	public HashMap<Skill, Integer> getIncreases()
	{
		return new HashMap<>(myIncreases);
	}
	
	public void addIncrease(Skill skill, int value)
	{
		myIncreases.put(skill, value);
		updateLore();
	}
	
	public int getIncrease(Skill skill)
	{
		return myIncreases.get(skill) == null ? 0 : myIncreases.get(skill);
	}
	
	public void removeIncrease(Skill skill)
	{
		myIncreases.remove(skill);
		updateLore();
	}
	
	public void setRequiredClasse(Classe classe)
	{
		myRequiredClasse = classe;
		updateLore();
	}
	
	public Classe getRequiredClasse()
	{
		return myRequiredClasse;
	}
	
	public void setDescription(String description)
	{
		myDescription = description;
		updateLore();
	}
	
	public String getDescription()
	{
		return myDescription;
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
		((Element) toWrite.getElementsByTagName("item").item(0)).removeAttribute("lore");
		
		Element equipment = toWrite.getOwnerDocument().createElement("equipment");
		toWrite.appendChild(equipment);
		
		equipment.setAttribute("description", myDescription);
		equipment.setAttribute("requiredLevel", Integer.toString(getRequiredLevel()));
		equipment.setAttribute("requiredClasse", getRequiredClasse().toString());
		
		Element requirements = toWrite.getOwnerDocument().createElement("requirements");
		equipment.appendChild(requirements);
		
		for(Skill skill : myRequirements.keySet())
			requirements.setAttribute(skill.toString(), Integer.toString(myRequirements.get(skill)));
			
		Element increases = toWrite.getOwnerDocument().createElement("increases");
		equipment.appendChild(increases);
		
		for(Skill skill : myIncreases.keySet())
			increases.setAttribute(skill.toString(), Integer.toString(myIncreases.get(skill)));
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		Element equipment = (Element) element.getElementsByTagName("equipment").item(0);
		
		myDescription = equipment.getAttribute("description");
		myRequiredLevel = Integer.valueOf(equipment.getAttribute("requiredLevel"));
		myRequiredClasse = Classe.valueOf(equipment.getAttribute("requiredClasse"));
		
		Element requirementsElement = (Element) equipment.getElementsByTagName("requirements").item(0);
		HashMap<Skill, Integer> requirements = new HashMap<>();
		
		for(Skill skill : Skill.values())
		{
			if(requirementsElement.hasAttribute(skill.toString()))
				requirements.put(skill, Integer.valueOf(requirementsElement.getAttribute(skill.toString())));
		}
		
		myRequirements = requirements;
		
		Element increasesElement = (Element) equipment.getElementsByTagName("increases").item(0);
		HashMap<Skill, Integer> increases = new HashMap<>();
		
		for(Skill skill : Skill.values())
		{
			if(increasesElement.hasAttribute(skill.toString()))
				increases.put(skill, Integer.valueOf(increasesElement.getAttribute(skill.toString())));
		}
		
		myIncreases = increases;
	}
	
	protected abstract void updateLore();
	
	protected abstract int calculQualityLevel();
}
