package com.wisteca.quartzlegion.entities.personnages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.soap.Node;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Armor;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AttackPouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.OverTimePouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.SkillsBuff;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.SpacePouvoir;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.HabilitySkill;
import com.wisteca.quartzlegion.utils.Utils;

/**
 * Le personnage est capable de se battre, il peux utiliser ses armes et avoir des pouvoirs.
 * @author Wisteca
 */

public abstract class Personnage extends PassivePersonnage {

	private int myHealth, myEnergy, myLevel, myCapacity, myRegenTime; // vie, �nergie, niveau et capacit� de stockage des buffs
	private HashMap<Skill, Integer> mySkills = new HashMap<>(); // skills de base
	private ConcurrentLinkedQueue<SpacePouvoir> myCurrentPouvoirs = new ConcurrentLinkedQueue<>(); // pouvoirs "passifs"
	private AttackPouvoir[] myAttackPouvoirs = new AttackPouvoir[8]; // pouvoirs d'attaque
	private IntelligentPersonnage[] myPets = new IntelligentPersonnage[3]; // "animaux" de compagnie
	private Personnage mySelectedPersonnage; // personnage s�l�ctionn�, non s�rializ�
	
	/**
	 * Construire un personnage avec des valeurs pas d�faut.
	 * @param uuid l'uuid du personnage
	 */
	
	public Personnage(UUID uuid)
	{
		super(uuid);
		
		myHealth = 20;
		myEnergy = 20;
		myLevel = 1;
		myCapacity = 100;
		
		for(Skill skill : Skill.values())
		{
			if(skill.equals(ClasseSkill.VIE_TOTALE) || skill.equals(ClasseSkill.ENERGIE_TOTALE))
				mySkills.put(skill, 20);
			else
				mySkills.put(skill, 1);
		}
	}
	
	/**
	 * Construire un personnage en sp�cifiant chaque attribut
	 * @param uuid l'uuid du personnage
	 * @param race la race du personnage
	 * @param classe la classe du personnage
	 * @param weapons les armes du personnage (peut �tre null)
	 * @param armors les armures du personnage (peut �tre null)
	 * @param skills les comp�tences du personnage
	 * @param pouvoirs les pouvoirs du personnage
	 * @param level le niveau du personnage
	 * @param capacity la capacit� de stockage des buffs du personnage
	 */
	
	public Personnage(UUID uuid, Race race, Classe classe, Weapon[] weapons, Armor[] armors, HashMap<Skill, Integer> skills, Class<? extends AttackPouvoir>[] pouvoirs, int level, int capacity)
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
		myLevel = level;
		myCapacity = capacity;
		
		if(pouvoirs == null)
			return;
		
		int i = 0;
		for(Class<? extends AttackPouvoir> type : pouvoirs)
		{
			myAttackPouvoirs[i] = newInstance(type);
			i++;
		}
	}
	
	/**
	 * Construire un personnage en le d�serializant
	 * @param element l'�l�ment contenant le personnage
	 */
	
	public Personnage(Element element)
	{
		super(element);
		deserialize(element);
	}
	
	private AttackPouvoir newInstance(Class<? extends AttackPouvoir> type)
	{
		try {
			
			return type.getConstructor(Personnage.class).newInstance(this);
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * S�rializer un personnage dans l'�l�ment pass� en param�tre
	 * @param toWrite l'�l�ment dans lequel le personnage devra se s�rializer
	 */
	
	@Override
	public void serialize(Element toWrite)
	{
		super.serialize(toWrite);
		
		toWrite.setAttribute("health", Integer.toString(myHealth));
		toWrite.setAttribute("energy", Integer.toString(myEnergy));
		toWrite.setAttribute("level", Integer.toString(myLevel));
		
		Utils.removeElementIfExist(toWrite, "skills");
		Element	skills = toWrite.getOwnerDocument().createElement("skills");
		toWrite.appendChild(skills);
		
		for(Skill skill : Skill.values())
			if(getSkillFix(skill) != 1)
				skills.setAttribute(skill.toString(), Integer.toString(getSkillFix(skill)));
		
		Utils.removeElementIfExist(toWrite, "skillsBuffs");
		Element skillsBuffs = toWrite.getOwnerDocument().createElement("skillsBuffs");
		toWrite.appendChild(skillsBuffs);
		
		Utils.removeElementIfExist(toWrite, "overTimePouvoirs");
		Element overTime = toWrite.getOwnerDocument().createElement("overTimePouvoirs");
		toWrite.appendChild(overTime);
		
		for(SpacePouvoir sp : myCurrentPouvoirs)
			if(sp instanceof SkillsBuff)
				sp.serialize(skillsBuffs);
			else
				sp.serialize(overTime);
		
		Utils.removeElementIfExist(toWrite, "attackPouvoirs");
		Element attackPouvoirs = toWrite.getOwnerDocument().createElement("attackPouvoirs");
		toWrite.appendChild(attackPouvoirs);
		
		for(AttackPouvoir ap : myAttackPouvoirs)
		{
			if(ap == null)
				continue;
			
			sendMessage(ap.getName());
			Element pouvoir = toWrite.getOwnerDocument().createElement("attack");
			attackPouvoirs.appendChild(pouvoir);
			ap.serialize(pouvoir);
		}
	}
	
	/**
	 * D�s�rialiser un personnage, m�thode appel�e par le constructeur
	 * @param element l'�l�ment dans lequel un personnage a �t� s�rialis� auparavant
	 */
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		myHealth = Integer.valueOf(element.getAttribute("health"));
		myEnergy = Integer.valueOf(element.getAttribute("energy"));
		myLevel = Integer.valueOf(element.getAttribute("level"));
		
		mySkills = new HashMap<>();
		Element skills = (Element) element.getElementsByTagName("skills").item(0);
		for(Skill skill : Skill.values())
			if(skills.hasAttribute(skill.toString()))
				mySkills.put(skill, Integer.valueOf(skills.getAttribute(skill.toString())));
			else
				mySkills.put(skill, 1);
		
		myCurrentPouvoirs = new ConcurrentLinkedQueue<>();
		Element skillsBuffs = (Element) element.getElementsByTagName("skillsBuffs").item(0);
		NodeList buffList = skillsBuffs.getChildNodes();
		for(int i = 0 ; i < buffList.getLength() ; i++)
			if(buffList.item(i).getNodeType() == Node.ELEMENT_NODE)
				myCurrentPouvoirs.add(new SkillsBuff((Element) buffList.item(i)));
		
		myAttackPouvoirs = new AttackPouvoir[8];
		Element attackPouvoirs = (Element) element.getElementsByTagName("attackPouvoirs").item(0);
		NodeList attackList = attackPouvoirs.getChildNodes();
		for(int i = 0 ; i < attackList.getLength() ; i++)
			if(attackList.item(i).getNodeType() == Node.ELEMENT_NODE)
				myAttackPouvoirs[i] = AttackPouvoir.getPouvoirByName(((Element) attackList.item(i)).getAttribute("name"), this);
		
		Element overTime = (Element) element.getElementsByTagName("overTimePouvoirs").item(0);
		NodeList DOTList = overTime.getChildNodes();
		for(int i = 0 ; i < DOTList.getLength() ; i++)
		{
			if(DOTList.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				Element dotElement = (Element) DOTList.item(i);
				OverTimePouvoir dot = OverTimePouvoir.getPouvoirByName(dotElement.getAttribute("name"), this);
				dot.deserialize(dotElement);
				myCurrentPouvoirs.add(dot);
			}
		}
		
		for(SpacePouvoir sp : myCurrentPouvoirs)
			sp.launch();
	}
	
	/**
	 * Changer la vie du personnage (ne prends pas en compte la protection)
	 * @param health vie en plus ou en moins
	 */
	
	public void changeHealth(int health)
	{
		if(myHealth + health >= getTemporarySkill(ClasseSkill.VIE_TOTALE))
			myHealth = getTemporarySkill(ClasseSkill.VIE_TOTALE);
		else
			myHealth += health;
		
		healthChanged();
	}
	
	/**
	 * Puisque que tous les personnages n'ont pas la m�me vie totale, cette m�thode permet de choisir un pourcentage de vie.
	 * @param percent le pourcentage de vie
	 */
	
	public void setHealth(double percent)
	{
		myHealth = (int) Math.floor(percent * (double) getSkillFix(ClasseSkill.VIE_TOTALE));
		healthChanged();
	}
	
	/**
	 * @return la vie du personnage
	 */
	
	public double getHealth()
	{
		return myHealth;
	}
	
	/**
	 * Appel� quand la vie du personnage change.
	 */
	
	protected void healthChanged()
	{
		if(myHealth <= 0)
		{
			myHealth = 0;
			die();
		}
	}
	
	/**
	 * Appel� quand le personnage se retrouve avec 0 de vie.
	 */
	
	protected abstract void die();
	
	/**
	 * Changer l'�nergie du personnage
	 * @param energy l'�nergie en plus ou en moins
	 */
	
	public void changeEnergy(int energy)
	{
		if(myEnergy + energy >= getTemporarySkill(ClasseSkill.ENERGIE_TOTALE))
			myEnergy = getTemporarySkill(ClasseSkill.ENERGIE_TOTALE);
		else
			myEnergy += energy;
		
		energyChanged();
	}
	
	/**
	 * D�finir l'�nergie du personnage � un pourcentage.
	 * @param percent le pourcentage d'�nergie
	 */
	
	public void setEnergy(double percent)
	{
		myEnergy = (int) Math.floor(percent * (double) getSkillFix(ClasseSkill.ENERGIE_TOTALE));
		energyChanged();
	}
	
	/**
	 * @return l'�nergie du personnage
	 */
	
	public int getEnergy()
	{
		return myEnergy;
	}
	
	/**
	 * Appel� � chaque fois que l'�nergie du personnage change
	 */
	
	protected abstract void energyChanged();
	
	/**
	 * Les comp�tences "fixes" ne sont pas chang�es par des pouvoirs ou autre buff. C'est les comp�tences "nettes".
	 * @param skill la comp�tence � chang�e
	 * @param value la valeur � ajout�e ou retir�e
	 * @return true si la comp�tence � �t� chang�e, false si la nouvelle valeur est trop grande par rapport au niveau du personnage
	 * @see Skill pour plus d'infos sur le syst�me de comp�tences
	 */
	
	public boolean changeSkillFix(Skill skill, int value)
	{
		if(getMaxValue(skill) > mySkills.get(skill) + value || value < 0) 
		{
			mySkills.put(skill, mySkills.get(skill) + value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param skill la comp�tence � r�cup�r�e
	 * @return la valeur "nette" de la comp�tence
	 * @see Skill pour plus d'infos sur le syst�me de comp�tences
	 */
	
	public int getSkillFix(Skill skill)
	{
		return mySkills.get(skill);
	}
	
	/**
	 * R�cup�rer une comp�tence en y additionnant sa d�pendance aux comp�tences d'habilit�es
	 * @param skill la comp�tence � r�cup�rer
	 * @return la valeur de la comp�tence (nette) + sa d�pendance aux comp�tences d'habilit�es
	 * @see Skill pour plus d'infos sur le syst�me de comp�tences
	 */
	
	public int getSkillWithDependencies(Skill skill)
	{
		if(skill instanceof ClasseSkill)
		{
			ClasseSkill cSkill = (ClasseSkill) skill;
			// calcul des d�pendances aux skills d'habilit�es
			int ajout = 0;
			for(HabilitySkill hSkill : HabilitySkill.values())
				ajout += (int) Math.floor((double) cSkill.getDependencie(hSkill) / 100 * getSkillFix(hSkill));
			
			return getSkillFix(skill) + ajout;
		}
		
		return getSkillFix(skill);
	}
	
	/**
	 * R�cup�rer une comp�tence temporaire, les comp�tences temporaires sont calcul�es en additionnant la comp�tence nette + sa d�pendance aux comp�tences d'habilit�es 
	 * + les pouvoirs qui l'augmentent + les augmentations des armes et armures.
	 * @param skill la comp�tence � r�cup�rer
	 * @return la valeur temporaire de cette comp�tence
	 * @see Skill pour plus d'infos sur le syst�me de comp�tences
	 */
	
	public int getTemporarySkill(Skill skill)
	{
		// augmentation des buffs
		int boost = 0;
		for(SpacePouvoir buff : myCurrentPouvoirs)
		{
			if(buff instanceof SkillsBuff)
				boost += ((SkillsBuff) buff).getModification(skill);
		}
		// augmentation des armures
		for(Armor a : getArmors())
			if(a != null)
				boost += a.getIncrease(skill);
		
		// augmentation des armes
		for(Weapon w : getWeapons())
			if(w != null)
				boost += w.getIncrease(skill);
		
		return getSkillWithDependencies(skill) + boost;
	}
	
	/**
	 * @param skill la comp�tence en question
	 * @return la valeur maximal de cette comp�tence pour le niveau du personnage
	 */
	
	public int getMaxValue(Skill skill)
	{
		return getLevel() * 5; // CHANGERA PAR LA SUITE
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		for(SpacePouvoir pp : myCurrentPouvoirs)
			pp.doTime();
		
		for(AttackPouvoir ap : myAttackPouvoirs)
			if(ap != null)
				ap.doTime();
		
		myRegenTime++;
		if(myRegenTime >= Constants.ENERGY_REGEN_TIME)
		{
			myRegenTime = 0;
			changeEnergy(getTemporarySkill(ClasseSkill.REGEN_ENERGIE));
		}
	}
	
	/**
	 * @param level le nouveau niveau du personnage
	 */
	
	public void setLevel(int newLevel)
	{
		if(newLevel == myLevel)
			return;
		
		int oldLevel = myLevel;
		myLevel = newLevel;
		levelChanged(oldLevel, myLevel - oldLevel);
	}
	
	/**
	 * @return le niveau du personnage
	 */
	
	public int getLevel()
	{
		return myLevel;
	}
	
	protected abstract void levelChanged(int oldLevel, int levelsNumber);
	
	/**
	 * D�finir un personnage s�l�ctionn� par le personnage courant, c'est ce personnage qu'il visera avec ses attaques, cette m�thode est g�n�ralement appel�e automatiquement par ses comportements.
	 * @param perso le personnage que le personnage courant ciblera
	 */
	
	public void setSelectedPersonnage(Personnage perso)
	{
		mySelectedPersonnage = perso;
	}
	
	/**
	 * @return r�cup�rer le personnage s�l�ctionn� par le personnage courant
	 */
	
	public Personnage getSelectedPersonnage()
	{
		return mySelectedPersonnage;
	}
	
	/**
	 * Retourne une liste de tous les personnages autour du personnage.
	 * @param distance la distance entre les personnages
	 * @return une liste de personnages
	 */
	
	public List<Personnage> getNearbyPersonnages(int distance)
	{
		List<Personnage> personnages = new ArrayList<>();
		for(Personnage perso : PersonnageManager.getInstance().getPersonnages())
		{
			if(perso.getLocation().distance(this.getLocation()) <= distance)
				personnages.add(perso);
		}
		
		return personnages;
	}
	
	/**
	 * R�cup�rer la puissance d'attaque du personnage en fonction de l'arme utilis�e. Les d�g�ts sont calcul�s comme ceci :
	 * d�g�ts arme + comp�tence / 2
	 * @param weapon le type de l'arme utilis�e pour attaquer
	 * @return un objet {@link Damage} ou null si le personnage n'a pas d'arme du type pass� en param�tre
	 */
	
	public Damage getAttackPower(WeaponType weapon)
	{
		//  arme utilis�e
		Weapon currentWeapon = null;
		for(Weapon w : getWeapons())
		{
			if(w != null)
				if(w.getWeaponType().equals(weapon))
					currentWeapon = w;
		}
		
		if(currentWeapon == null)
			return null;
		
		Damage infos = new Damage();
		
		HashMap<DamageType, Integer> damages = new HashMap<>();
		for(DamageType type : DamageType.values())
			damages.put(type, currentWeapon.getRandomDamages(type) + getTemporarySkill(weapon.getSkill()) / 2);
		
		HashMap<Skill, Integer> skills = new HashMap<>();
		for(Skill skill : Skill.values())
			skills.put(skill, getTemporarySkill(skill));
		
		infos.setDamages(damages);
		infos.setSkills(skills);
		infos.setWeaponType(weapon);
		infos.setCriticalLuck(currentWeapon.getCriticalLuck());
		infos.setCriticalDamages(currentWeapon.getCriticalDamages());
		
		return infos;
	}
	
	/**
	 * Cette m�thode est celle qui suit logiquement getAttackPower(), elle inflige au personnage les d�g�ts en prenant en compte les �vitements et la protection.
	 * @param damage l'objet contenant les informations n�cessaires au calcul des d�g�ts
	 * @return les d�g�ts subit
	 */
	
	public int damage(Damage damage)
	{
		// �vitement
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
			default:
				break;
		}
		
		// calcul des d�g�ts
		int totalDamage = 0;
		for(DamageType type : DamageType.values())
			totalDamage += damage.getDamages().get(type) - getProtection(type);
		totalDamage /= DamageType.values().length;
		
		// critique
		if(new Random().nextInt(Constants.MAX_CRITICAL_LUCK) <= damage.getCriticalLuck())
			totalDamage += damage.getCriticalDamages() / 10;
		
		if(totalDamage < 0)
			totalDamage = 0;
		
		changeHealth(-totalDamage);
		return totalDamage;
	}
	
	private boolean checkEvade(ClasseSkill skill, int compare)
	{
		return new Random().nextInt(getTemporarySkill(skill)) > compare;
	}
	
	/**
	 * Infliger tant de d�g�ts au personnage, cette m�thode peut �tre utilis�e pour les d�g�ts d'environnement, elle diminue les d�g�ts en fonction de l'armure du personnage.
	 * @param damage les d�g�ts � infliger au personnage
	 * @param types permet de sp�cifier les types de d�g�t � infliger, peut �tre null si aucun type de d�g�t ne r�pond au contexte
	 */
	
	public void damage(int damage, DamageType... types)
	{
		int moyenne = 0;
		if(types == null)
		{
			for(DamageType dt : DamageType.values())
				moyenne += getProtection(dt);
			moyenne /= DamageType.values().length;
		}
		else
		{
			for(DamageType type : types)
				moyenne += getProtection(type);
			moyenne /= types.length;
		}
		
		sendMessage("damage : " + (damage - moyenne <= 0 ? 0 : damage - moyenne));
		changeHealth(-(damage - moyenne <= 0 ? 0 : damage - moyenne));
	}
	
	/**
	 * @return true si la vie du personnage est �gal � 0
	 */
	
	@Override
	public boolean isDead()
	{
		return myHealth == 0;
	}
	
	/**
	 * Cette m�thode est utilis�e pour informer un personnage des d�g�ts qu'il a fait subir � un autre personnage.
	 * @param damages les d�g�ts subits
	 * @param target le personnage qui a �t� percut�
	 */
	
	public abstract void damageInfo(int damages, Personnage target);
	
	/**
	 * R�cup�rer la protection du personnage en fonction de ses armures.
	 * @param protection le type de d�g�ts contre lequel le personnage poss�de une protection
	 * @return la protection du personnage
	 */
	
	public int getProtection(DamageType protection)
	{
		int moyenne = 0;
		int armorsNbre = 0;
		for(Armor armor : getArmors())
		{
			if(armor != null)
			{
				moyenne += armor.getProtection(protection);
				armorsNbre++;
			}
		}
		
		return (int) Math.floor((double) moyenne / armorsNbre) + getTemporarySkill(ClasseSkill.ARMURE);
	}
	
	/**
	 * Mettre un pouvoir � l'emplacement index�, pour que le pouvoir puisse �tre �quip�, il faut que le personnage poss�de une arme du type du pouvoir.
	 * @param index l'index o� placer le pouvoir
	 * @param pouvoir le type de pouvoir � placer
	 * @throws ArrayIndexOutOfBoundsException si l'index n'est pas compris entre 0 et 7
	 * @return true si le pouvoir � �t� �quip�, false si le personnage n'avait pas l'arme du type requis
	 */
	
	public boolean setPouvoir(int index, Class<? extends AttackPouvoir> type)
	{
		AttackPouvoir pouvoir = newInstance(type);
		
		if(pouvoir.getWeaponType().equals(WeaponType.AUCUN))
		{
			myAttackPouvoirs[index] = pouvoir;
			return true;
		}
		
		for(Weapon w : getWeapons())
		{
			if(w == null)
				continue;
			
			if(w.getWeaponType().equals(pouvoir.getWeaponType()) == false)
				continue;
			
			myAttackPouvoirs[index] = pouvoir;
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param index l'index du lanceur � r�cup�rer
	 * @return le lanceur de pouvoir plac� � l'index
	 * @throws ArrayIndexOutOfBoundsException si l'index n'est pas compris entre 0 et 7
	 */
	
	public AttackPouvoir getPouvoir(int index)
	{
		return myAttackPouvoirs[index];
	}
	
	/**
	 * @return une liste des lanceurs d'attaques du personnage
	 */
	
	public AttackPouvoir[] getPouvoirs()
	{
		return myAttackPouvoirs.clone();
	}
	
	/**
	 * Mettre un animal de compagnie au personnage
	 * @param index l'index de l'animal
	 * @param pet le personnage qui deviendra l'animal de compagnie du personnage
	 * @throws ArrayIndexOutOfBoundsException si l'index n'est pas compris entre 0 et 2
	 */
	
	public void setPet(int index, IntelligentPersonnage pet)
	{
		myPets[index] = pet;
	}
	
	/**
	 * @param index l'index dans lequel l'animal devra �tre r�cup�rer
	 * @return l'animal de compagnie du personnage
	 */
	
	public IntelligentPersonnage getPet(int index)
	{
		return myPets[index];
	}
	
	/**
	 * @return un tableau de animaux de compagnie du personnage
	 */
	
	public IntelligentPersonnage[] getPets()
	{
		return myPets.clone();
	}
	
	/**
	 * Ajouter un buff au personnage.
	 * @param sp le SpacePouvoir � ajout� au personnage
	 * @return true si le pouvoir � bien �t� ajout�, false si la capacit� du personnage �tait insuffisante
	 */
	
	public boolean addSpacePouvoir(SpacePouvoir sp)
	{
		if(getCurrentSpacePouvoirsSize() + sp.getSize() > getCapacity())
			return false;
		
		myCurrentPouvoirs.add(sp);
		return true;
	}
	
	/**
	 * Supprimer un buff.
	 * @param sp le buff � supprimer
	 */
	
	public void removeSpacePouvoir(SpacePouvoir sp)
	{
		myCurrentPouvoirs.remove(sp);
	}
	
	/**
	 * @return une liste des buffs qui tournent sur le joueur
	 */
	
	public ArrayList<SpacePouvoir> getSpacePouvoirs()
	{
		return new ArrayList<>(myCurrentPouvoirs);
	}
	
	/**
	 * D�finir une capacit� de stockage des buffs au personnage
	 * @param capacity la capacit� � d�finir
	 */
	
	public void setCapacity(int capacity)
	{
		myCapacity = capacity;
	}
	
	/**
	 * @return la capacit� de stockage des buffs du personnage
	 */
	
	public int getCapacity()
	{
		return myCapacity;
	}
	
	/**
	 * @return la place que prennent les buffs qui tournent actuellement sur le personnage
	 */
	
	public int getCurrentSpacePouvoirsSize()
	{
		int space = 0;
		for(SpacePouvoir pp : myCurrentPouvoirs)
			space += pp.getSize();
		
		return space;
	}
	
	/**
	 * Envoyer un message au personnage, seul les joueurs peuvent voir les messages.
	 * @param channel le canal de discussion dans lequel le message sera envoy�
	 * @param msg le message � envoy�
	 */
	
	public void sendMessage(Channel channel, String msg)
	{
		sendMessage(channel.getPrefix() + msg);
	}
	
	/**
	 * Enum�ration repr�sentant un canal de discussion.
	 * @author Wisteca
	 */
	
	public static enum Channel {
		
		COMBAT("�7�o[C] - �4�o"),
		ECONOMIE("�7�o[E] - �2�o"),
		GENERAL("�7�o[G] - �c�o"),
		GROUPE("�7�o[Groupe] - �9�o"),
		GUILDE("�7�o[Guilde] - �6�o"),
		ALENTOUR("�7�o[Alentour] - �e�o"),
		INFO("�8�o[?] �7�o"),
		PRIVE("�7�o[Priv�] - �5�o");
			
		private String myPrefix;
		
		Channel(String prefix)
		{
			myPrefix = prefix;
		}
		
		/**
		 * @return le pr�fix du canal utilis� devant chaque message
		 */
		
		public String getPrefix()
		{
			return myPrefix;
		}
	}
	
	/**
	 * Enum�ration des races.
	 * @author Wisteca
	 * @see Skill
	 */
	
	public static enum Race {
		
		ELFE("Elfe"),
		NAIN("Nain"),
		ORC("Orc"),
		DRAKEIDE("Drak�ide"),
		HUMAIN("Humain"),
		AUCUN("Non sp�cifi�");
		
		private String myCleanName;
		
		private Race(String cleanName)
		{
			myCleanName = cleanName;
		}
		
		/**
		 * @return le nom de la race �crit proprement avec une majuscule
		 */
		
		public String getCleanName()
		{
			return myCleanName;
		}
	}
	
	/**
	 * Enum�ration des classes.
	 * @author Wisteca
	 */
	
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
		GUERISSEUR("Gu�risseur"),
		PALADIN("Paladin"),
		AUCUN("Aucune");
		
		private String myCleanName;
		
		private Classe(String cleanName)
		{
			myCleanName = cleanName;
		}
		
		/**
		 * @return le nom de la classe �crit proprement avec une majuscule
		 */
		
		public String getCleanName()
		{
			return myCleanName;
		}
	}
}
