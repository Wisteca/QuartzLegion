package com.wisteca.quartzlegion.entities.personnages.combats.equipment;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.Utils;

/**
 * Représente un équipement comme une arme ou une armure.
 * @author Wisteca
 */

public abstract class Equipment extends Item {
	
	protected String myDescription;
	private int myRequiredLevel;
	private Classe myRequiredClasse;
	private HashMap<Skill, Integer> myRequirements = new HashMap<>();
	private HashMap<Skill, Integer> myIncreases = new HashMap<>();
	
	/**
	 * Construire un équipement en précisant tous les attributs.
	 * @param type le type d'item
	 * @param requiredClasse la classe requise pour équiper l'item ou null pour que tout le monde puisse l'équiper
	 * @param requirements les compétences requises pour équiper l'équipement
	 * @param increases les augmentations des compétences que provoque l'équipement lorsqu'il est équipé
	 * @param requiredLevel le niveau requis pour équiper l'équipement
	 * @param name le nom de l'équipement
	 * @param description la description de l'équipement
	 */
	
	public Equipment(ItemType type, Classe requiredClasse, HashMap<Skill, Integer> requirements, HashMap<Skill, Integer> increases, int requiredLevel, String name, String description)
	{
		super(type);
		this.setName(name);
		myDescription = description;
		myRequiredClasse = requiredClasse;
		myRequirements = requirements;
		myIncreases = increases;
		myRequiredLevel = requiredLevel;
	}
	
	/**
	 * Construire un équipement en le déserializant.
	 * @param element l'élément dans lequel l'équipement a été sérializé auparavant
	 */
	
	public Equipment(Element element)
	{
		super(element);
	}
	
	/**
	 * @param requirements les nouvelles compétences requises pour équiper l'équipement
	 */
	
	public void setRequirements(HashMap<Skill, Integer> requirements)
	{
		myRequirements = new HashMap<>(requirements);
		updateLore();
	}
	
	/**
	 * @param skill la compétence à changer
	 * @param value la nouvelle valeur de la compétence
	 */
	
	public void addRequirement(Skill skill, int value)
	{
		myRequirements.put(skill, value);
		updateLore();
	}
	
	/**
	 * @param skill la compétence à supprimer
	 */
	
	public void removeRequirement(Skill skill)
	{
		myRequirements.remove(skill);
		updateLore();
	}
	
	/**
	 * @return les compétences requises pour équiper l'équipement
	 */
	
	public HashMap<Skill, Integer> getRequirements()
	{
		return new HashMap<>(myRequirements);
	}
	
	/**
	 * @param skill la compétence à récupérer
	 * @return la valeur de la compétence
	 */
	
	public int getRequirement(Skill skill)
	{
		return myRequirements.containsKey(skill) ? myRequirements.get(skill) : 0;
	}
	
	/**
	 * @param level le nouveau niveau requis
	 */
	
	public void setRequiredLevel(int level)
	{
		myRequiredLevel = level;
		updateLore();
	}
	
	/**
	 * @return le niveau requis pour équiper l'équipement
	 */
	
	public int getRequiredLevel()
	{
		return myRequiredLevel;
	}
	
	/**
	 * @param increases les nouvelles augmentations que l'équipement provoque sur les personnages qui l'équipent
	 */
	
	public void setIncreases(HashMap<Skill, Integer> increases)
	{
		myIncreases = new HashMap<>(increases);
		updateLore();
	}
	
	/**
	 * @return les augmentations que l'équipement provoque aux personnages qui l'équipe
	 */
	
	public HashMap<Skill, Integer> getIncreases()
	{
		return new HashMap<>(myIncreases);
	}
	
	/**
	 * @param skill la compétence à ajouter ou à changer
	 * @param value la valeur de la compétence
	 */
	
	public void addIncrease(Skill skill, int value)
	{
		myIncreases.put(skill, value);
		updateLore();
	}
	
	/**
	 * @param skill la compétence à récupérer
	 * @return la valeur de la compétence
	 */
	
	public int getIncrease(Skill skill)
	{
		return myIncreases.get(skill) == null ? 0 : myIncreases.get(skill);
	}
	
	/**
	 * @param skill la compétence à supprimer
	 */
	
	public void removeIncrease(Skill skill)
	{
		myIncreases.remove(skill);
		updateLore();
	}
	
	/**
	 * @param classe la classe requise pour équiper l'équipement ou null pour que tout le monde puisse l'équiper
	 */
	
	public void setRequiredClasse(Classe classe)
	{
		myRequiredClasse = classe;
		updateLore();
	}
	
	/**
	 * @return la classe requise pour équiper l'équipement ou null si tout le monde peut l'équiper
	 */
	
	public Classe getRequiredClasse()
	{
		return myRequiredClasse;
	}
	
	/**
	 * @param description la description de l'équipement qui apparaîtra dans le lore de l'item sous les caractéristiques
	 */
	
	public void setDescription(String description)
	{
		myDescription = description;
		updateLore();
	}
	
	/**
	 * @return la description de l'équipement
	 */
	
	public String getDescription()
	{
		return myDescription;
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
		((Element) toWrite.getElementsByTagName("item").item(0)).removeAttribute("lore");
		
		Utils.removeElementIfExist(toWrite, "equipment");
		Element equipment = toWrite.getOwnerDocument().createElement("equipment");
		toWrite.appendChild(equipment);
		
		equipment.setAttribute("description", myDescription);
		equipment.setAttribute("requiredLevel", Integer.toString(getRequiredLevel()));
		equipment.setAttribute("requiredClasse", getRequiredClasse().toString());
		
		Utils.removeElementIfExist(toWrite, "requirements");
		Element requirements = toWrite.getOwnerDocument().createElement("requirements");
		equipment.appendChild(requirements);
		
		for(Skill skill : myRequirements.keySet())
			requirements.setAttribute(skill.toString(), Integer.toString(myRequirements.get(skill)));
			
		Utils.removeElementIfExist(toWrite, "increases");
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
