package com.wisteca.quartzlegion.entities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wisteca.quartzlegion.MainClass;
import com.wisteca.quartzlegion.entities.personnages.Joueur;
import com.wisteca.quartzlegion.entities.personnages.PassivePersonnage;

public class PersonnageManager implements Listener {
	
	/**
	 * Singletone qui contient tous les personnages et qui se charge d'appeler leurs événements.
	 * @author Wisteca
	 */
	
	private static PersonnageManager myInstance;
	private HashMap<UUID, PassivePersonnage> myPersonnages = new HashMap<>();
	
	/**
	 * L'instance est construite au démarrage du plugin, ne pas appeler le constructeur.
	 */
	
	public PersonnageManager()
	{
		myInstance = this;
		Bukkit.getPluginManager().registerEvents(this, MainClass.getInstance());
		
		for(Player pls : Bukkit.getOnlinePlayers())
			new Joueur(pls);
	}
	
	/**
	 * Les personnages s'ajoutent automatiquement à leur construction.
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
	
	public HashMap<UUID, PassivePersonnage> getPersonnages()
	{
		return new HashMap<>(myPersonnages);
	}
	
	/**
	 * @param uuid l'uuid du personnage que l'on souhaite récupérer
	 * @return le personnage comportant l'uuid spécifié, ou null si il n'existe pas
	 */
	
	public PassivePersonnage getPersonnage(UUID uuid)
	{
		return myPersonnages.get(uuid);
	}
	
	/**
	 * @return l'instance de PersonnageManager construite au démarrage du plugin
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
	 * Méthodes appelées par l'api bukkit lorsqu'un event se déclenche, les personnages seront ensuite itérés et le onEvent du personnage sera appelé.
	 */
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		new Joueur(p);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		onEvent(e.getPlayer(), e);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e)
	{
		onEvent(e.getPlayer(), e);
	}
}
