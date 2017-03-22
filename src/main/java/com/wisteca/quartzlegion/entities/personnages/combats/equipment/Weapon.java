package com.wisteca.quartzlegion.entities.personnages.combats.equipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
	private int myCriticalLuck, myCriticalDamages;
	private HashMap<DamageType, int[]> myDamages;
	private Random myRandom = new Random();
	
	/**
	 * Construire une arme en pr�cisant tous ses attributs.
	 * @param type le type d'item
	 * @param weapon le type d'arme
	 * @param requiredCLasse la classe requise pour s'�quiper de l'arme
	 * @param damages les d�g�ts de l'arme
	 * @param requirements les comp�tences requises pour pouvoir �quiper l'arme
	 * @param increases les augmentations des comp�tences que provoque l'arme quand elle est �quip�e
	 * @param requiredLevel le niveau requis pour �quiper l'arme
	 * @param criticalLuck les chances de faire un coup critique, {@link Constants} pour voir la valeur maximal
	 * @param criticalDamage les d�g�ts en plus si un coup critique se d�clenche
	 * @param name le nom de l'arme
	 * @param description la description de l'arme
	 */
	
	public Weapon(ItemType type, WeaponType weapon, Classe requiredCLasse, HashMap<Skill, Integer> requirements, HashMap<DamageType, int[]> damages, HashMap<Skill, Integer> increases,
			int requiredLevel, int criticalLuck, int criticalDamage, String name, String description)
	{
		super(type, requiredCLasse, requirements, increases, requiredLevel, name, description);
		myWeaponType = weapon;
		myCriticalLuck = criticalLuck;
		myCriticalDamages = criticalDamage;
		
		if(damages == null)
		{
			myDamages = new HashMap<>();
			for(DamageType damage : DamageType.values())
				myDamages.put(damage, new int[2]);
		}
		else
		{
			myDamages = new HashMap<>(damages);
			for(DamageType damage : DamageType.values())
				if(myDamages.containsKey(damage) == false)
					myDamages.put(damage, new int[2]);
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
		List<String> lore = new ArrayList<>(Arrays.asList(
				"�cType: �4" + getWeaponType().getCleanName(),
				"�fQualit�: �a" + calculQualityLevel(), 
				"�cClasse requise: �c�l" + getRequiredClasse().getCleanName(),
				"�5Chances de critiques: �f" + getCriticalLuck(),
				"�5D�g�ts critiques: �f" + getCriticalDamages(), "",
				"�7D�g�ts:" + (getRequirements().isEmpty() ? "" : "                    �cRequis:")));
		
		int i = 0;
		for(DamageType damage : DamageType.values())
		{
			loreBuilder.put(i, new StringBuilder("   �e" + damage.getSpaceName() + ": �b" + myDamages.get(damage)[0] + " - " + myDamages.get(damage)[1]));
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
			total += (((myDamages.get(damage)[1] - myDamages.get(damage)[0])) / 2) + myDamages.get(damage)[0];
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
	 * @param type le type de d�g�t � changer
	 * @param min les d�g�ts minimum de l'arme
	 * @param max les d�g�ts maximum de l'arme
	 * @throws IllegalArgumentException si les d�g�ts minimum sont plus �lev�s que les d�g�ts maximum
	 */
	
	public void setDamages(DamageType type, int min, int max)
	{
		if(min < max)
			myDamages.put(type, new int[]{min, max});
		else
			throw new IllegalArgumentException("Les d�g�ts minimum sont plus �lev�s que les d�g�ts maximum...");
	}
	
	/**
	 * R�cup�rer dans un tableau la plage de d�g�ts de l'arme
	 * @param type le type de d�g�t
	 * @return un tableau contenant en [0] les d�g�ts minimum et en [1] les d�g�ts maximum
	 */
	
	public int[] getDamages(DamageType type)
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
		if(myDamages.get(type)[1] > 0)
			return myRandom.nextInt(myDamages.get(type)[1] - myDamages.get(type)[0]) + myDamages.get(type)[0];
		else
			return 0;
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
		{
			if(myDamages.get(type)[0] == 0 && myDamages.get(type)[1] == 0)
				continue;
			
			Element damageElement = toWrite.getOwnerDocument().createElement(type.toString());
			damages.appendChild(damageElement);
			
			damageElement.setAttribute("min", Integer.toString(myDamages.get(type)[0]));
			damageElement.setAttribute("max", Integer.toString(myDamages.get(type)[1]));
		}
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		myWeaponType = WeaponType.valueOf(element.getAttribute("weaponType"));
		myCriticalLuck = Integer.valueOf(element.getAttribute("criticalLuck"));
		myCriticalDamages = Integer.valueOf(element.getAttribute("criticalDamages"));
		
		myDamages = new HashMap<>();
		NodeList list = element.getElementsByTagName("damages").item(0).getChildNodes();
		for(int i = 0 ; i < list.getLength() ; i++)
		{
			if(list.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			Element damage = (Element) list.item(i);
			myDamages.put(DamageType.valueOf(damage.getNodeName()), new int[]{Integer.valueOf(damage.getAttribute("min")), Integer.valueOf(damage.getAttribute("max"))});
		}
		
		for(DamageType type : DamageType.values())
			if(myDamages.containsKey(type) == false)
				myDamages.put(type, new int[]{0, 0});
		
		updateLore();
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
		SANG("Sang"),
		AUCUN("Aucun");
		
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
