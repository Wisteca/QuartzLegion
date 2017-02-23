package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Channel;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;

/**
 * Représente un pouvoir "officiel", il peut être acheté et utilisé par les joueurs, il apparaît dans leur liste des pouvoirs.
 * @author Wisteca
 */

public interface OfficialPouvoir extends Pouvoir {
	
	/**
	 * @return l'item qui apparaîtra dans l'inventaire du joueur qui a le pouvoir
	 */
	
	public Item getRepresentativeItem();
	
	/**
	 * @return le nom du pouvoir, il doit être unique, deux pouvoirs ne peuvent pas avoir le même nom !
	 */
	
	public String getName();
	
	/**
	 * @return une description du pouvoir qui apparaîtra dans le lore de l'item par exemple
	 */
	
	public String getDescription();
	
	/**
	 * @return le temps de rechargement du pouvoir en ticks
	 */
	
	public int getLoadingTime();
	
	/**
	 * @return le côut d'énergie du pouvoir quand il est lancé
	 */
	
	public int getEnergyCost();
	
	/**
	 * @return les compétences requises pour utiliser  le pouvoir
	 */
	
	public HashMap<Skill, Integer> getRequirements();
	
	/**
	 * @return la classe requise pour utiliser le pouvoir ou null pour que tout le monde puisse l'utiliser
	 */
	
	public Classe getRequiredClasse();
	
	/**
	 * @return le temps de rechargement restant avant de pouvoir relancer le pouvoir
	 */
	
	public int getRemainingLoadingTime();
	
	/**
	 * @return true si le temps de rechargement est à 0
	 */
	
	public default boolean canLaunch()
	{
		return getRemainingLoadingTime() == 0;
	}
	
	/**
	 * Check si un personnage a les compétences et l'énergie requise pour lancer le pouvoir.
	 * @param launcher le personnage à checker
	 * @return true si il a l'autorisation de lancer
	 */
	
	public default boolean checkLaunch(Personnage launcher)
	{
		if(canLaunch() == false)
		{
			launcher.sendMessage(Channel.COMBAT, "Ce pouvoir n'est pas encore rechargé.");
		}
		
		for(Skill skill : getRequirements().keySet())
		{
			if(getRequirements().get(skill) > launcher.getTemporarySkill(skill))
			{
				launcher.sendMessage(Channel.COMBAT, "Vous n'avez pas les compétences requises pour utiliser ce pouvoir.");
				return false;
			}
		}
		
		if(getEnergyCost() > launcher.getEnergy())
		{
			launcher.sendMessage(Channel.COMBAT, "Vous n'avez pas assez d'énergie.");
			return false;
		}
		
		return true;
	}	
}
