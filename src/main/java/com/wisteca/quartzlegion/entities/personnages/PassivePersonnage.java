package com.wisteca.quartzlegion.entities.personnages;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Serializer;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Race;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Armor;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;
import com.wisteca.quartzlegion.utils.Utils;
import com.wisteca.quartzlegion.utils.effects.EffectInterface.Part;

/**
 * Classe de base de chaque entit�s, repr�sente une entit� d�corative et incapable de se battre, poss�de des armes pour la d�coration !
 * @author Wisteca
 */

public abstract class PassivePersonnage implements Entity, Serializer {
	
	private Weapon[] myWeapons = new Weapon[2];
	private Armor[] myArmors = new Armor[4];
	private Race myRace;
	private Classe myClasse;
	private UUID myUniqueId;
	
	/**
	 * Construire un PassivePersonnage avec des valeurs par d�faut.
	 * @param uuid l'uuid du passivePersonnage
	 */
	
	public PassivePersonnage(UUID uuid)
	{
		myUniqueId = uuid;
		myRace = Race.AUCUN;
		myClasse = Classe.AUCUN;
		
		PersonnageManager.getInstance().addPersonnage(this);
	}
	
	/**
	 * Construire un objet en sp�cifiant chaque param�tres.
	 * @param race race du personnage
	 * @param classe classe du personnage
	 * @param uniqueId uuid du personnage
	 * @param weapons armes du personnage, ce param�tre peut �tre null
	 * @param armors armures du personnage, ce param�tre peut �tre null
	 * @see Race
	 * @see Classe
	 * @see Weapon
	 * @see Armor
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
		
		PersonnageManager.getInstance().addPersonnage(this);
	}
	
	/**
	 * Construire un objet en le d�serializant.
	 * @param element
	 */
	
	public PassivePersonnage(Element element)
	{
		deserialize(element);
		PersonnageManager.getInstance().addPersonnage(this);
	}
	
	/**
	 * @return l'uuid de l'entit�
	 */
	
	@Override
	public UUID getUniqueId()
	{
		return myUniqueId;
	}
	
	/**
	 * @return la race de l'entit�
	 * @see Race
	 */
	
	public Race getRace()
	{
		return myRace;
	}
	
	/**
	 * @return la classe de l'entit�
	 * @see Classe
	 */
	
	public Classe getClasse()
	{
		return myClasse;
	}
	
	/**
	 * @param slot le slot o� l'armure doit �tre plac�e, l'index 0 correspond au casque,jusqu'� l'index 3 qui est les bottes
	 * @param armor l'armure � placer dans ce slot
	 * @throws ArrayIndexOutOfBoundsException si le slot est plus petit que 0 ou sup�rieur � 3
	 */
	
	public void setArmor(int slot, Armor armor)
	{
		myArmors[slot] = armor;
	}
	
	/**
	 * @param slot le slot de l'armure
	 * @return l'armure correspondant au slot sp�cifi� ou null si le personnage n'a pas d'armure dans ce slot
	 * @throws ArrayIndexOutOfBoundsException si le slot n'est pas compris entre 0 et 3
	 * @see Armor
	 */
	
	public Armor getArmor(int slot)
	{
		return myArmors[slot];
	}
	
	/**
	 * @return un tableau de toutes les armures du personnage, en partant de la t�te aux pieds, des slots peuvent �tre null
	 * @see Armor
	 */
	
	public Armor[] getArmors()
	{
		return myArmors;
	}
	
	/**
	 * @param slot le slot dans lequel l'arme doit �tre plac�e, compris entre 0 et 2
	 * @param weapon l'arme � placer
	 * @see Weapon
	 * @throws ArrayIndexOutOfBoundsException si le slot n'est pas compris entre 0 et 2
	 */
	
	public void setWeapon(int slot, Weapon weapon)
	{
		myWeapons[slot] = weapon;
	}
	
	/**
	 * @param slot le slot o� aller chercher l'arme
	 * @return l'arme se trouvant dans le slot ou null si il n'y a pas d'arme dans ce slot
	 * @throws ArrayIndexOutOfBoundsException si le slot n'est pas compris entre 0 et 2
	 * @see Weapon
	 */
	
	public Weapon getWeapon(int slot)
	{
		return myWeapons[slot];
	}
	
	/**
	 * @return un tableau contenant les 3 armes du personnage, les armes peuvent �tre null si le slot est vide
	 * @see Weapon
	 */
	
	public Weapon[] getWeapons()
	{
		return myWeapons;
	}
	
	/**
	 * R�cup�rer <strong>APPROXIMATIVEMENT</strong> la location de la partie du corps du personnage.
	 * @param part la partie du corps � r�cup�rer
	 * @return la location APPROXIMATIVE de la partie du corps
	 */
	
	public abstract Location getCurrentLocation(Part part);
	
	/**
	 * M�thode appel�e automatiquement lorsqu'un �v�nement se d�clenche, {@link PersonnageManager} pour voir la liste d'�v�nements.
	 * Pour �couter un �v�nement : if(e instanceof PlayerInteractAtEntityEvent) { // on sait que le personnage vient d'interagir avec une entit� }
	 * @param e l'�v�nement en question
	 */
	
	public abstract void onEvent(Event e);
	
	/**
	 * M�thode appel�e chaque ticks (20 fois par secondes), peut �tre red�finie pour faire des checks r�gulier ou autre.
	 * <strong>Ne pas appeler cette m�thode, cela pourrait fausser des compteurs comme celui des buffs ! elle est appel�e chaque ticks dans l'entit� NMS du personnage.</strong>
	 */
	
	public void doTime()
	{}
	
	/**
	 * @return la position des yeux de l'entit�
	 */
	
	public abstract Location getEyeLocation();
	
	@Override
	public void serialize(Element toWrite)
	{
		toWrite.setAttribute("uuid", myUniqueId.toString());
		toWrite.setAttribute("race", myRace.toString());
		toWrite.setAttribute("classe", myClasse.toString());
		
		Utils.removeElementIfExist(toWrite, "weapons");
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
		
		Utils.removeElementIfExist(toWrite, "armors");
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
