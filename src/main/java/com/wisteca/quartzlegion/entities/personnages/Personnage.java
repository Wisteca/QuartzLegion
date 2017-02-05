package com.wisteca.quartzlegion.entities.personnages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.combats.Damage;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Armor;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AttackPouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.SkillsBuff;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.SpacePouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.HabilitySkill;

public abstract class Personnage extends PassivePersonnage {

	protected int myHealth, myEnergy, myLevel;
	private HashMap<Skill, Integer> mySkills = new HashMap<>();
	private ArrayList<SpacePouvoir> myCurrentSpacePouvoirs = new ArrayList<>();
	private AttackPouvoir[] myAttackPouvoirs = new AttackPouvoir[8];
	private IntelligentPersonnage[] myPets = new IntelligentPersonnage[3];
	private Personnage mySelectedPersonnage; // non sérializé
	
	public Personnage(UUID uuid, Race race, Classe classe, Weapon[] weapons, Armor[] armors, HashMap<Skill, Integer> skills, AttackPouvoir[] pouvoirs, int level)
	{
		super(race, classe, uuid, weapons, armors);
		
		for(Skill skill : Skill.values())
		{
			if(skills == null)
				mySkills.put(skill, 1);
			else if(skills.containsKey(skill))
				mySkills.put(skill, skills.get(skill));
			else
				mySkills.put(skill, 1);
		}
		
		myHealth = getSkillFix(ClasseSkill.VIE_TOTALE);
		myEnergy = getSkillFix(ClasseSkill.ENERGIE_TOTALE);
		myAttackPouvoirs = pouvoirs.clone();
		myLevel = level;
	}
	
	public Personnage(Element element)
	{
		super(element);
		deserialize(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		// TODO Auto-generated method stub
		super.serialize(toWrite);
	}
	
	@Override
	public void deserialize(Element element)
	{
		// TODO Auto-generated method stub
		super.deserialize(element);
	}
	
	public void changeHealth(int health)
	{
		myHealth += health;
		healthChanged();
	}
	
	public void setHealth(double percent)
	{
		myHealth = (int) Math.floor(percent * (double) getSkillFix(ClasseSkill.VIE_TOTALE));
		healthChanged();
	}
	
	public double getHealth()
	{
		return myHealth;
	}
	
	protected abstract void healthChanged();
	
	public void changeEnergy(int energy)
	{
		myEnergy += energy;
		energyChanged();
	}
	
	public void setEnergy(double percent)
	{
		myEnergy = (int) Math.floor(percent * (double) getSkillFix(ClasseSkill.ENERGIE_TOTALE));
		energyChanged();
	}
	
	public int getEnergy()
	{
		return myEnergy;
	}
	
	protected abstract void energyChanged();
	
	public boolean changeSkillFix(Skill skill, int value)
	{
		if(getMaxValue(skill) > mySkills.get(skill) + value || value < 0) 
		{
			mySkills.put(skill, mySkills.get(skill) + value);
			return true;
		}
		
		return false;
	}
	
	public int getSkillFix(Skill skill)
	{
		return mySkills.get(skill);
	}
	
	public int getSkillWithDependencies(Skill skill)
	{
		if(skill instanceof ClasseSkill)
		{
			ClasseSkill cSkill = (ClasseSkill) skill;
			// calcul des dépendances
			int ajout = 0;
			for(HabilitySkill hSkill : HabilitySkill.values())
				ajout += (int) Math.floor((double) cSkill.getDependencie(hSkill) / 100 * getSkillFix(hSkill));
			
			return getSkillFix(skill) + ajout;
		}
		
		return getSkillFix(skill);
	}
	
	public int getTemporarySkill(Skill skill)
	{
		// augmentation
		int boost = 0;
		for(SpacePouvoir buff : myCurrentSpacePouvoirs)
			if(buff instanceof SkillsBuff)
				boost += ((SkillsBuff) buff).getModification(skill);
		
		// dépendance
		int ajout = 0;
		if(skill instanceof ClasseSkill)
		{
			ClasseSkill cSkill = (ClasseSkill) skill;
			// calcul des dépendances
			for(HabilitySkill hSkill : HabilitySkill.values())
				ajout += (int) Math.floor((double) cSkill.getDependencie(hSkill) / 100 * getTemporarySkill(hSkill));
		}
		
		return getSkillFix(skill) + boost + ajout;
	}
	
	public int getMaxValue(Skill skill)
	{
		return getLevel() * 5; // CHANGERA PAR LA SUITE
	}
	
	public void setLevel(int level)
	{
		myLevel = level;
	}
	
	public int getLevel()
	{
		return myLevel;
	}
	
	public void setSelectedPersonnage(Personnage perso)
	{
		mySelectedPersonnage = perso;
	}
	
	public Personnage getSelectedPersonnage()
	{
		return mySelectedPersonnage;
	}
	
	public Damage getAttackPower(AttackPouvoir pouvoir)
	{
		Weapon currentWeapon = null;
		for(Weapon w : getWeapons())
			if(w.getWeaponType().equals(pouvoir.getWeaponType()))
			{
				currentWeapon = w; // on choisit l'arme du bon type
				break;
			}
		
		Damage power = new Damage();
		
		HashMap<DamageType, Integer> damages = new HashMap<>();
		for(DamageType type : DamageType.values())
			damages.put(type, pouvoir.getDamages().get(type) + currentWeapon.getRandomDamages(type));
			// dégâts = pouvoir + arme
		HashMap<Skill, Integer> skills = new HashMap<>();
		for(Skill skill : Skill.values())
			skills.put(skill, getTemporarySkill(skill));
		
		power.setDamages(damages);
		power.setSkills(skills);
		power.setCriticalDamages(currentWeapon.getCriticalDamages());
		power.setCriticalLuck(currentWeapon.getCriticalLuck());
		power.setWeaponType(currentWeapon.getWeaponType());
		
		return power;
	}
	
	public int damage(Damage damage)
	{
		// évitement
		switch(damage.getWeaponType())
		{
			case CORDE:
				if(checkEvade(ClasseSkill.EVADE, damage.getSkills().get(HabilitySkill.PRECISION)))
					return 0;
				break;
			case FEU:
				if(checkEvade(ClasseSkill.EVADE, damage.getSkills().get(HabilitySkill.PRECISION)))
					return 0;
				break;
				
			case DEMONIAQUE:
				if(checkEvade(ClasseSkill.RESIS_MAGIQUE, damage.getSkills().get(HabilitySkill.INTELLIGENCE)))
					return 0;
				break;
			case ELEMENTAIRE:
				if(checkEvade(ClasseSkill.RESIS_MAGIQUE, damage.getSkills().get(HabilitySkill.INTELLIGENCE)))
					return 0;
				break;
			case SANG:
				if(checkEvade(ClasseSkill.RESIS_MAGIQUE, damage.getSkills().get(HabilitySkill.INTELLIGENCE)))
					return 0;
				break;
				
			case ARME_LONGUE:
				if(checkEvade(ClasseSkill.PARADE, damage.getSkills().get(HabilitySkill.FORCE)))
					return 0;
				break;
			case ARME_LOURDE:
				if(checkEvade(ClasseSkill.PARADE, damage.getSkills().get(HabilitySkill.FORCE)))
					return 0;
				break;
			case LAME:	
				if(checkEvade(ClasseSkill.PARADE, damage.getSkills().get(HabilitySkill.FORCE)))
					return 0;
				break;
		}
		
		// calcul des dégâts
		int totalDamage = 0;
		for(DamageType type : DamageType.values())
			totalDamage += damage.getDamages().get(type) - getProtection(type);
		totalDamage /= DamageType.values().length;
		
		// critique                                                    MAX VALUE
		if(new Random().nextInt(100) <= damage.getCriticalLuck() / 100 * 10000)
			totalDamage += damage.getCriticalDamages() / 10;
		
		if(totalDamage < 0)
			totalDamage = 0;
		
		changeHealth(totalDamage);
		return totalDamage;
	}
	
	@Override
	public boolean isDead()
	{
		return myHealth == 0;
	}
	
	private boolean checkEvade(ClasseSkill skill, int compare)
	{
		return new Random().nextInt(getTemporarySkill(skill)) > compare;
	}
	
	public abstract void damageInfo(int damages, Personnage target);
	
	public int getProtection(DamageType protection)
	{
		int moyenne = 0;
		for(Armor armor : getArmors())
			moyenne += armor.getProtection(protection);
		
		return moyenne / getArmors().length + getTemporarySkill(ClasseSkill.ARMURE);
	}
	
	public void setPouvoir(int index, AttackPouvoir pouvoir)
	{
		for(Weapon w : getWeapons())
		{
			if(w.getWeaponType().equals(pouvoir.getWeaponType()) == false)
				continue;
			
			myAttackPouvoirs[index] = pouvoir;
			return;
		}
		
		sendMessage("Vous n'avez pas l'arme requise pour vous équiper de ce pouvoir.");
	}
	
	public AttackPouvoir getPouvoir(int index)
	{
		return index >= myAttackPouvoirs.length ? null : myAttackPouvoirs[index];
	}
	
	public AttackPouvoir[] getPouvoirs()
	{
		return myAttackPouvoirs.clone();
	}
	
	public void setPet(int index, IntelligentPersonnage pet)
	{
		if(index < myPets.length)
			myPets[index] = pet;
	}
	
	public IntelligentPersonnage getPet(int index)
	{
		return index < myPets.length ? myPets[index] : null;
	}
	
	public IntelligentPersonnage[] getPets()
	{
		return myPets.clone();
	}
	
	public void addSpacePouvoir(SpacePouvoir sp)
	{
		myCurrentSpacePouvoirs.add(sp);
	}
	
	public void removeSpacePouvoir(SpacePouvoir sp)
	{
		myCurrentSpacePouvoirs.remove(sp);
	}
	
	public ArrayList<SpacePouvoir> getSpacePouvoirs()
	{
		return new ArrayList<>(myCurrentSpacePouvoirs);
	}
	
	public void sendMessage(Channel channel, String msg)
	{
		sendMessage(channel.getPrefix() + msg);
	}
	
	public static enum Channel {
		
		COMBAT("§7§o[C] - §4§o"),
		ECONOMIE("§7§o[E] - §2§o"),
		GENERAL("§7§o[G] - §c§o"),
		GROUPE("§7§o[Groupe] - §9§o"),
		GUILDE("§7§o[Guilde] - §6§o"),
		ALENTOUR("§7§o[Alentour] - §e§o"),
		INFO("§8§o[?] §7§o"),
		PRIVE("§7§o[Privé] - §5§o");
			
		private String myPrefix;
		
		Channel(String prefix)
		{
			myPrefix = prefix;
		}
			
		public String getPrefix()
		{
			return myPrefix;
		}
	}
	
	public static enum Race {
		
		ELFE("Elfe"),
		NAIN("Nain"),
		ORC("Orc"),
		DRAKEIDE("Drakéide"),
		HUMAIN("Humain"),
		AUCUN("Non spécifié");
		
		private String myCleanName;
		
		private Race(String cleanName)
		{
			myCleanName = cleanName;
		}
		
		public String getCleanName()
		{
			return myCleanName;
		}
	}
	
	public static enum Classe {
		
		ARCHER("Archer"),
		ASSASSIN("Assassin"),
		GUERRIER("Guerrier"),
		VOLEUR("Voleur"),
		BARBARE("Barbare"),
		CHEVALIER("Chevalier"),
		SORCIER("Sorcier"),
		MOINE("Moine"),
		MAGE("Mage"),
		GUERISSEUR("Guérisseur"),
		PALADIN("Paladin"),
		AUCUN("Non spécifié");
		
		private String myCleanName;
		
		private Classe(String cleanName)
		{
			myCleanName = cleanName;
		}
		
		public String getCleanName()
		{
			return myCleanName;
		}
	}
}
