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

/**
 * Repr�sente une armure sous forme d'item.
 * @author Wisteca
 */

public class Armor extends Equipment {
	
	private HashMap<DamageType, Integer> myProtections = new HashMap<>();
	
	/**
	 * Construire une armure en sp�cifiant chaque attributs.
	 * @param type le type de l'item
	 * @param requiredClasse la classe requise pour pouvoir s'�quiper de l'armure ou null pour que tout le monde puisse l'�quiper
	 * @param protections une HashMap contenant les protections de l'armure face aux d�g�ts
	 * @param name le nom de l'armure
	 * @param description la description de l'armure
	 * @param requirements les comp�tences requises pour pouvoir s'�quiper de l'armure
	 * @param increases les modifications que provoque l'armure sur les comp�tences du personnage qui l'�quipe
	 * @param requiredLevel le niveau requis pour s'�quiper de l'armure
	 */
	
	public Armor(ItemType type, Classe requiredClasse, HashMap<DamageType, Integer> protections, HashMap<Skill, Integer> requirements, HashMap<Skill, Integer> increases,
			int requiredLevel, String name, String description)
	{
		super(type, requiredClasse, requirements, increases, requiredLevel, name, description);
		
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
	
	/**
	 * D�s�rializer une armure.
	 * @param element la cha�ne XML dans laquelle une armure avait �t� s�rializ�e auparavant
	 */
	
	public Armor(Element element)
	{
		super(element);
	}
	
	/**
	 * Changer la protection de l'armure
	 * @param protection le type de d�g�t � changer
	 * @param value la nouvelle valeur du d�g�t
	 */
	
	public void setProtection(DamageType protection, int value)
	{
		myProtections.put(protection, value);
		updateLore();
	}
	
	/**
	 * R�cup�rer la protection de l'armure.
	 * @param protection le type de d�g�t auquel r�cup�rer la protection
	 * @return la protection que poss�de l'armure pour ce type de d�g�t
	 */
	
	public int getProtection(DamageType protection)
	{
		return myProtections.get(protection);
	}
	
	@Override
	protected void updateLore()
	{
		HashMap<Integer, StringBuilder> loreBuilder = new HashMap<>();
		ArrayList<String> lore = new ArrayList<>(Arrays.asList("�fQualit�: �a" + calculQualityLevel(),
				"�cClasse requise: �c�l" + getRequiredClasse().getCleanName(), "",
				"�7Protection:" + (getRequirements().isEmpty() ? "" : "             �cRequis:")));
		
		int i = 0;
		for(DamageType damage : DamageType.values())
		{
			loreBuilder.put(i, new StringBuilder("   �e" + damage.getSpaceName() + ": �b" + myProtections.get(damage)));
			i++;
		}
		
		i = 0;
		for(Skill skill : Skill.values())
		{
			if(getRequirement(skill) == 0)
				continue;
			
			StringBuilder sb = loreBuilder.containsKey(i) ? loreBuilder.get(i) : new StringBuilder("                    ");
			sb.append("     �b" + getRequirement(skill) + " �een " + skill.getCompleteName());
			loreBuilder.put(i, sb);
			i++;
		}
		
		for(StringBuilder strBuilder : loreBuilder.values())
			lore.add(strBuilder.toString());
		
		lore.add("");
		
		if(getIncreases().isEmpty() == false)
		{
			lore.add("�6Augmentations:");
			for(Skill skill : Skill.values())
			{
				if(getIncrease(skill) == 0)
					continue;
				
				lore.add("   �6" + getIncrease(skill) + " �een " + skill.getCompleteName());
			}
		}
		
		if(myDescription != null)
			for(String str : Utils.getTextWithLineFeed(myDescription, 45))
				lore.add("�b" + str);
			
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
