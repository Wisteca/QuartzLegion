package com.wisteca.quartzlegion.entities.personnages;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

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

/**
 * Classe de base de chaque entités, représente une entité décorative et incapable de se battre, possède des armes pour la décoration !
 * @author Wisteca
 */

public abstract class PassivePersonnage implements Entity, Serializer {
	
	private Weapon[] myWeapons = new Weapon[3];
	private Armor[] myArmors = new Armor[4];
	private Race myRace;
	private Classe myClasse;
	private UUID myUniqueId;
	
	/**
	 * Construire un objet en spécifiant chaque paramètres.
	 * @param race race du personnage
	 * @param classe classe du personnage
	 * @param uniqueId uuid du personnage
	 * @param weapons armes du personnage, ce paramètre peut être null
	 * @param armors armures du personnage, ce paramètre peut être null
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
	 * Construire un objet en le déserializant.
	 * @param element
	 */
	
	public PassivePersonnage(Element element)
	{
		deserialize(element);
		PersonnageManager.getInstance().addPersonnage(this);
	}
	
	/**
	 * @return l'uuid de l'entité
	 */
	
	@Override
	public UUID getUniqueId()
	{
		return myUniqueId;
	}
	
	/**
	 * @return la race de l'entité
	 * @see Race
	 */
	
	public Race getRace()
	{
		return myRace;
	}
	
	/**
	 * @return la classe de l'entité
	 * @see Classe
	 */
	
	public Classe getClasse()
	{
		return myClasse;
	}
	
	/**
	 * @param slot le slot où l'armure doit être placée, l'index 0 correspond au casque,jusqu'à l'index 3 qui est les bottes
	 * @param armor l'armure à placer dans ce slot
	 * @throws ArrayIndexOutOfBoundsException si le slot est plus petit que 0 ou supérieur à 3
	 */
	
	public void setArmor(int slot, Armor armor)
	{
		myArmors[slot] = armor;
	}
	
	/**
	 * @param slot le slot de l'armure
	 * @return l'armure correspondant au slot spécifié ou null si le personnage n'a pas d'armure dans ce slot
	 * @throws ArrayIndexOutOfBoundsException si le slot n'est pas compris entre 0 et 3
	 * @see Armor
	 */
	
	public Armor getArmor(int slot)
	{
		return myArmors[slot];
	}
	
	/**
	 * @return un tableau de toutes les armures du personnage, en partant de la tête aux pieds, des slots peuvent être null
	 * @see Armor
	 */
	
	public Armor[] getArmors()
	{
		return myArmors;
	}
	
	/**
	 * @param slot le slot dans lequel l'arme doit être placée, compris entre 0 et 2
	 * @param weapon l'arme à placer
	 * @see Weapon
	 * @throws ArrayIndexOutOfBoundsException si le slot n'est pas compris entre 0 et 2
	 */
	
	public void setWeapon(int slot, Weapon weapon)
	{
		myWeapons[slot] = weapon;
	}
	
	/**
	 * @param slot le slot où aller chercher l'arme
	 * @return l'arme se trouvant dans le slot ou null si il n'y a pas d'arme dans ce slot
	 * @throws ArrayIndexOutOfBoundsException si le slot n'est pas compris entre 0 et 2
	 * @see Weapon
	 */
	
	public Weapon getWeapon(int slot)
	{
		return myWeapons[slot];
	}
	
	/**
	 * @return un tableau contenant les 3 armes du personnage, ou null si il n'a pas d'arme
	 * @see Weapon
	 */
	
	public Weapon[] getWeapons()
	{
		return myWeapons;
	}
	
	/**
	 * Méthode appelée automatiquement lorsqu'un événement se déclenche, {@link PersonnageManager} pour voir la liste d'événements.
	 * Pour écouter un événement : if(e instanceof PlayerInteractAtEntityEvent) { // on sait que le personnage vient d'interagir avec une entité }
	 * @param e l'événement en question
	 */
	
	public void onEvent(Event e)
	{}
	
	/**
	 * Méthode appelée chaque ticks (20 fois par secondes), peut être redéfinie pour faire des checks régulier ou autre.
	 * <strong>Ne pas appeler cette méthode, cela pourrait fausser des compteurs comme celui des buffs ! elle est appelée chaque ticks dans l'entité NMS du personnage.</strong>
	 */
	
	public void doTime()
	{}
	
	/**
	 * @return la position des yeux de l'entité
	 */
	
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
