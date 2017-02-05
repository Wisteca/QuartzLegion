package com.wisteca.quartzlegion.entities.personnages;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Serializer;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Race;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Armor;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;

/**
 * 
 * @author Wisteca
 *
 */

public abstract class PassivePersonnage implements Entity, Serializer {
	
	private Weapon[] myWeapons = new Weapon[3];
	private Armor[] myArmors = new Armor[4];
	private Race myRace;
	private Classe myClasse;
	private UUID myUniqueId;
	
	/**
	 * Construire un objet en spécifiant chaque paramètres
	 * @param race
	 * @param classe
	 * @param uniqueId
	 * @param weapons
	 * @param armors
	 */
	
	public PassivePersonnage(Race race, Classe classe, UUID uniqueId, Weapon[] weapons, Armor[] armors)
	{
		myRace = race;
		myClasse = classe;
		myUniqueId = uniqueId;
		
		if(weapons != null)
		{
			for(int i = 0 ; i < myWeapons.length ; i++)
			{
				if(i < weapons.length)
					myWeapons[i] = weapons[i];
			}
		}
		
		if(armors != null)
		{
			for(int i = 0 ; i < myArmors.length ; i++)
			{
				if(i < armors.length)
					myArmors[i] = armors[i];
			}
		}
	}
	
	/**
	 * Construire un objet en le déserializant
	 * @param element
	 */
	
	public PassivePersonnage(Element element)
	{
		deserialize(element);
	}
	
	/**
	 * @return l'uuid de l'entité
	 */
	
	@Override
	public UUID getUniqueId()
	{
		return myUniqueId;
	}
	
	public Race getRace()
	{
		return myRace;
	}
	
	public Classe getClasse()
	{
		return myClasse;
	}
	
	public void setArmor(int slot, Armor armor)
	{
		myArmors[slot] = armor;
	}
	
	public Armor getArmor(int slot)
	{
		return slot > 4 ? null : myArmors[slot];
	}
	
	public Armor[] getArmors()
	{
		return myArmors;
	}
	
	public void setWeapon(int slot, Weapon weapon)
	{
		myWeapons[slot] = weapon;
	}
	
	public Weapon getWeapon(int slot)
	{
		return slot > 3 ? null : myWeapons[slot];
	}
	
	public Weapon[] getWeapons()
	{
		return myWeapons;
	}
	
	public void onEvent(Event e)
	{}
	
	public void doTime()
	{}
	
	public abstract Location getEyeLocation();
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		toWrite.setAttribute("uuid", getUniqueId().toString());
		toWrite.setAttribute("race", getRace().toString());
		toWrite.setAttribute("classe", getClasse().toString());
		
		Element weapons = toWrite.getOwnerDocument().createElement("weapons");
		toWrite.appendChild(weapons);
		
		for(Weapon weapon : getWeapons())
		{
			if(weapon == null)
				continue;
			
			Element weaponElement = toWrite.getOwnerDocument().createElement("weapon");
			weapons.appendChild(weaponElement);
			weapon.serialize(weaponElement);
		}
		
		Element armors = toWrite.getOwnerDocument().createElement("armors");
		toWrite.appendChild(armors);
		
		for(Armor armor : getArmors())
		{
			if(armor == null)
				continue;
			
			Element armorElement = toWrite.getOwnerDocument().createElement("armor");
			armors.appendChild(armorElement);
			armor.serialize(armorElement);
		}
	}
	
	@Override
	public void deserialize(Element element)
	{
		myUniqueId = UUID.fromString(element.getAttribute("uuid"));
		myRace = Race.valueOf(element.getAttribute("race"));
		myClasse = Classe.valueOf(element.getAttribute("classe"));
		
		Element weapons = (Element) element.getElementsByTagName("weapons").item(0);
		NodeList weaponList = weapons.getElementsByTagName("weapon");
		for(int i = 0 ; i < weaponList.getLength() ; i++)
			setWeapon(i, new Weapon((Element) weaponList.item(i)));
		
		Element armors = (Element) element.getElementsByTagName("armors").item(0);
		NodeList armorList = armors.getElementsByTagName("armor");
		for(int i = 0 ; i < armorList.getLength() ; i++)
			setArmor(i, new Armor((Element) armorList.item(i)));
	}
}
