package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;

public abstract class AttackPouvoir implements OfficialPouvoir {
	
	private Personnage myAttacker;
	private int myCurrentTime = 0;
	
	/**
	 * @param attacker le personnage qui utilisera ce pouvoir pour attaquer
	 */
	
	public AttackPouvoir(Personnage attacker)
	{
		myAttacker = attacker;
	}
	
	/**
	 * @param element l'élément dans lequel le pouvoir a été sérializé auparavant
	 */
	
	public AttackPouvoir(Element element)
	{
		deserialize(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		toWrite.setAttribute("name", getName());
		toWrite.setAttribute("attacker", myAttacker.getUniqueId().toString());
	}
	
	@Override
	public void deserialize(Element element)
	{
		myAttacker = (Personnage) PersonnageManager.getInstance().getPersonnage(UUID.fromString(element.getAttribute("attacker")));
	}
	
	@Override
	public boolean launch()
	{
		return checkLaunch(myAttacker);
	}
	
	@Override
	public void doTime()
	{
		if(myCurrentTime > 0)
			myCurrentTime--;
	}
	
	@Override
	public int getRemainingLoadingTime()
	{
		return myCurrentTime;
	}
	
	/**
	 * @return true si le pouvoir cible automatiquement le personnage qui l'utilise
	 */
	
	public abstract boolean isAutoOnSelf();
	
	/**
	 * @return le type d'arme à utiliser avec ce pouvoir
	 */
	
	public abstract WeaponType getWeaponType();
	
	/**
	 * @return une description du pouvoir
	 */
	
	public abstract String toString();
	
	/**
	 * @return le temps qu'il faut au pouvoir pour effectuer ses effets, ce temps est toujours le temps minimum de rechargement du pouvoir
	 */
	
	public abstract int getDuration();
	
	/**
	 * @return l'attaquant utilisant ce pouvoir
	 */
	
	public Personnage getAttacker()
	{
		return myAttacker;
	}
	
	/**
	 * Récupérer un pouvoir d'attaque d'après son nom puis retourner une instance de ce-dernier avec comme attaquant le personnage en paramètre.
	 * Cette méthode itère la liste des pouvoirs de la classe {@link Constants}, si elle retourne null et que le pouvoir existe, c'est qu'il n'a pas été ajouté dans la liste.
	 * @param name le nom du pouvoir
	 * @param launcher l'attaquant passer dans le constructeur du pouvoir
	 * @return une instance du pouvoir portant le nom en paramètre, null si aucun pouvoir n'a été trouvé.
	 */
	
	public static AttackPouvoir getPouvoirByName(String name, Personnage launcher)
	{
		AttackPouvoir pouvoir;
		for(Class<? extends AttackPouvoir> type : Constants.ATTACK_POUVOIR_LIST)
		{
			try {
				
				pouvoir = type.getConstructor(Personnage.class).newInstance(launcher);
				if(pouvoir.getName().equals(name))
					return pouvoir;
			
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return null;
	}
}
