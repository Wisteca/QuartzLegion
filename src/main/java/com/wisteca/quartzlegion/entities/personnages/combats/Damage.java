package com.wisteca.quartzlegion.entities.personnages.combats;

import java.util.HashMap;

import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;

/**
 * Un simple objet qui contient des attributs qui seront transmis entre des personnages pour s'envoyer des informations en rapport au combat.
 * @author Wisteca
 */

public class Damage {
	
	private HashMap<DamageType, Integer> myDamages = new HashMap<>();
	private HashMap<Skill, Integer> mySkills = new HashMap<>();
	private int myCriticalDamages, myCriticalLuck;
	private WeaponType myWeaponType;
	
	public void setDamages(HashMap<DamageType, Integer> damages)
	{
		myDamages = new HashMap<>(damages);
	}
	
	public void setSkills(HashMap<Skill, Integer> skills)
	{
		mySkills = new HashMap<>(skills);
	}
	
	public void setCriticalDamages(int value)
	{
		myCriticalDamages = value;
	}
	
	public void setCriticalLuck(int value)
	{
		myCriticalLuck = value;
	}
	
	public HashMap<DamageType, Integer> getDamages()
	{
		return new HashMap<>(myDamages);
	}
	
	public HashMap<Skill, Integer> getSkills()
	{
		return new HashMap<>(mySkills);
	}
	
	public int getCriticalDamages()
	{
		return myCriticalDamages;
	}
	
	public int getCriticalLuck()
	{
		return myCriticalLuck;
	}
	
	public void setWeaponType(WeaponType type)
	{
		myWeaponType = type;
	}
	
	public WeaponType getWeaponType()
	{
		return myWeaponType;
	}
	
	/**
	 * Enum�ration des types de d�g�ts.
	 * @author Wisteca
	 */
	
	public static enum DamageType {
		
		BRULURE			("Br�lure",			"Br�lure       "),
		ETOUFFEMENT		("�touffement",		"�touffement    "),
		GEL				("Gel", 			"Gel           "),
		SECTIONNEMENT	("Sectionnement", 	"Sectionnement  "),
		PERFORATION		("Perforation",		"Perforation    "),
		FRACAS			("Fracas",			"Fracas        "),
		DECHIQUETEMENT	("D�chiqu�tement",	"D�chiqu�tement "),
		EMPOISONNEMENT	("Empoisonnement", 	"Empoisonnement ");
		
		private String myName, mySpaceName;
		
		DamageType(String name, String spaceName)
		{
			myName = name;
			mySpaceName = spaceName;
		}
		
		/**
		 * @return le nom du d�g�t �crit proprement avec une majuscule
		 */
		
		public String getCleanName()
		{
			return myName;
		}
		
		/**
		 * @return le nom du d�g�ts �crit proprement avec une majuscule et avec des espaces derri�re pour pouvoir environ aligner les noms dans un tableau
		 */
		
		public String getSpaceName()
		{
			return mySpaceName;
		}
	}
}
