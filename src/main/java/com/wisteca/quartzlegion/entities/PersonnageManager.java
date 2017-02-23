package com.wisteca.quartzlegion.entities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import com.wisteca.quartzlegion.MainClass;
import com.wisteca.quartzlegion.entities.personnages.PassivePersonnage;

public class PersonnageManager implements Listener {
	
	/**
	 * Singletone qui contient tous les personnages et qui se charge d'appeler leurs �v�nements.
	 * @author Wisteca
	 */
	
	private static PersonnageManager myInstance;
	private HashMap<UUID, PassivePersonnage> myPersonnages = new HashMap<>();
	
	/**
	 * L'instance est construite au d�marrage du plugin, ne pas appeler le constructeur.
	 */
	
	public PersonnageManager()
	{
		myInstance = this;
		Bukkit.getPluginManager().registerEvents(this, MainClass.getInstance());
	}
	
	/**
	 * Les personnages s'ajoutent automatiquement � leur construction.
	 * @param perso le personnage en question
	 */
	
	public void addPersonnage(PassivePersonnage perso)
	{
		myPersonnages.put(perso.getUniqueId(), perso);
	}
	
	/**
	 * Supprimer un personnage de la liste, appeler automatiquement.
	 * @param uuid
	 */
	
	public void removePersonnage(UUID uuid)
	{
		myPersonnages.remove(uuid);
	}
	
	/**
	 * @param uuid l'uuid du personnage que l'on souhaite r�cup�rer
	 * @return le personnage comportant l'uuid sp�cifi�, ou null si il n'existe pas
	 */
	
	public PassivePersonnage getPersonnage(UUID uuid)
	{
		return myPersonnages.get(uuid);
	}
	
	/**
	 * @return l'instance de PersonnageManager construite au d�marrage du plugin
	 */
	
	public static PersonnageManager getInstance()
	{
		return myInstance;
	}
	
	private void onEvent(Entity entity, Event e)
	{
		for(PassivePersonnage pp : myPersonnages.values())
		{
			if(pp.getUniqueId().equals(entity.getUniqueId()))
			{
				pp.onEvent(e);
				break;
			}
		}
	}
	
	/**
	 * M�thode appel� par l'api bukkit lorsqu'un event se d�clenche, les personnages seront ensuite it�r�s et le onEvent du personnage sera appel�.
	 */
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e)
	{
		onEvent(e.getPlayer(), e);
	}
}
