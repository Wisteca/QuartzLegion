package com.wisteca.quartzlegion.entities.personnages.combats.equipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.Utils;

/**
 * Repr�sente une arme sous forme d'item.
 * @author Wisteca
 */

public class Weapon extends Equipment {
	
	private WeaponType myWeaponType;
	private HashMap<DamageType, WeaponDamage> myDamages = new HashMap<>();
	private int myCriticalLuck, myCriticalDamages;
	
	/**
	 * Construire une arme en pr�cisant tous ses attributs.
	 * @param type le type d'item
	 * @param weapon le type d'arme
	 * @param requiredCLasse la classe requise pour s'�quiper de l'arme ou null pour que tout le monde puisse l'�quiper
	 * @param damages les d�g�ts de l'arme
	 * @param requirements les comp�tences requises pour pouvoir �quiper l'arme
	 * @param increases les augmentations des comp�tences que provoque l'arme quand elle est �quip�e
	 * @param requiredLevel le niveau requis pour �quiper l'arme
	 * @param criticalLuck les chances de faire un coup critique, {@link Constants} pour voir la valeur maximal
	 * @param criticalDamage les d�g�ts en plus si un coup critique se d�clenche
	 * @param name le nom de l'arme
	 * @param description la description de l'arme
	 */
	
	public Weapon(ItemType type, WeaponType weapon, Classe requiredCLasse, HashMap<DamageType, WeaponDamage> damages, HashMap<Skill, Integer> requirements, HashMap<Skill, Integer> increases,
			int requiredLevel, int criticalLuck, int criticalDamage, String name, String description)
	{
		super(type, requiredCLasse, requirements, increases, requiredLevel, name, description);
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
	
	/**
	 * Construire une arme en la d�serializant.
	 * @param element
	 */
	
	public Weapon(Element element)
	{
		super(element);
	}
	
	@Override
	protected void updateLore()
	{
		HashMap<Integer, StringBuilder> loreBuilder = new HashMap<>();
		List<String> lore = new ArrayList<>(Arrays.asList("�cType: �4" + getWeaponType().getCleanName(),
				"�fQualit�: �a" + calculQualityLevel(), "�cClasse requise: �c�l" + getRequiredClasse().getCleanName(),
				"�5Chances de critiques: �f" + getCriticalLuck(), "�5D�g�ts critiques: �f" + getCriticalDamages(), "",
				"�7D�g�ts:" + (getRequirements().isEmpty() ? "" : "                    �cRequis:")));
		
		int i = 0;
		for(DamageType damage : DamageType.values())
		{
			loreBuilder.put(i, new StringBuilder("   �e" + damage.getSpaceName() + ": �b" + myDamages.get(damage).getMinDamage()
					+ " - " + myDamages.get(damage).getMaxDamage()));
			i++;
		}
		
		i = 0;
		for(Skill skill : Skill.values())
		{
			if(getRequirement(skill) == 0)
				continue;
			
			StringBuilder sb = loreBuilder.containsKey(i) ? loreBuilder.get(i) : new StringBuilder("                        ");
			sb.append("     �c" + getRequirement(skill) + " �een " + skill.getCompleteName());
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
			for(String str : Utils.getTextWithLineFeed(myDescription, 50))
				lore.add("�b" + str);
		
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
	
	/**
	 * @return le type de l'arme
	 */
	
	public WeaponType getWeaponType()
	{
		return myWeaponType;
	}
	
	/**
	 * @param type le nouveau type de l'arme
	 */
	
	public void setWeaponType(WeaponType type)
	{
		myWeaponType = type;
		updateLore();
	}
	
	/**
	 * Changer des d�g�ts existants ou ajouter de nouveaux d�g�ts.
	 * @param type le type de d�g�t
	 * @param damages les nouveaux d�g�ts
	 */
	
	public void setDamage(DamageType type, WeaponDamage damages)
	{
		myDamages.put(type, damages);
		updateLore();
	}
	
	/**
	 * @param type le type de d�g�t � r�cup�rer
	 * @return un objet {@link WeaponDamage} contenant les d�gats minimum et maximum
	 */
	
	public WeaponDamage getDamages(DamageType type)
	{
		return myDamages.get(type);
	}
	
	/**
	 * R�cup�rer un nombre de d�g�ts al�atoire entre le maximum et le minimum
	 * @param type le type de d�g�ts
	 * @return un nombre al�atoire
	 */
	
	public int getRandomDamages(DamageType type)
	{
		return new Random().nextInt(myDamages.get(type).getMaxDamage() - myDamages.get(type).getMinDamage())
				+ myDamages.get(type).getMinDamage();
	}
	
	/**
	 * @param luck les nouvelles chances de critique
	 */
	
	public void setCriticalLuck(int luck)
	{
		myCriticalLuck = luck;
		updateLore();
	}
	
	/**
	 * @param damages les nouveaux d�g�ts critique
	 */
	
	public void setCriticalDamages(int damages)
	{
		myCriticalDamages = damages;
		updateLore();
	}
	
	/**
	 * @return les chances de critique de l'arme
	 */
	
	public int getCriticalLuck()
	{
		return myCriticalLuck;
	}
	
	/**
	 * @return les d�g�ts critique de l'arme
	 */
	
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
	
	/**
	 * Simple classe contenant deux attributs, les d�g�ts maximum et minimum d'une arme.
	 * @author Wisteca
	 */
	
	public static class WeaponDamage {
		
		private int myMinDamage, myMaxDamage;
		
		/**
		 * @param minDamage les d�g�ts minimum
		 * @param maxDamage les d�g�ts maximum
		 */
		
		public WeaponDamage(int minDamage, int maxDamage)
		{
			myMinDamage = minDamage;
			myMaxDamage = maxDamage;
		}
		
		/**
		 * @return les d�g�ts minimum
		 */
		
		public int getMinDamage()
		{
			return myMinDamage;
		}
		
		/**
		 * @return les d�g�ts maxmimum
		 */
		
		public int getMaxDamage()
		{
			return myMaxDamage;
		}
	}
	
	/**
	 * Enum�ration des diff�rents types d'armes.
	 * @author Wisteca
	 */
	
	public static enum WeaponType {
		
		CORDE("Corde"), 
		FEU("Feu"),
		LAME("Lame"), 
		ARME_LONGUE("Arme longue"), 
		ARME_LOURDE("Arme lourde"),
		DEMONIAQUE("D�moniaque"),
		ELEMENTAIRE("�l�mentaire"), 
		SANG("Sang");
		
		private String myCleanName;
		
		private WeaponType(String cleanName)
		{
			myCleanName = cleanName;
		}
		
		/**
		 * @return le nom du type �crit proprement avec une majuscule au d�but
		 */
		
		public String getCleanName()
		{
			return myCleanName;
		}
		
		/**
		 * @return la comp�tence en rapport avec ce type d'arme
		 */
		
		public ClasseSkill getSkill()
		{
			return ClasseSkill.valueOf(this.toString());
		}
	}
}
