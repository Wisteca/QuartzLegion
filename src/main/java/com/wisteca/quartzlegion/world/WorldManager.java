package com.wisteca.quartzlegion.world;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.MainClass;
import com.wisteca.quartzlegion.data.Constants;

/**
 * Contient la liste des mondes et les initialisent au démarrage du plugin.
 * @author Wisteca
 */

public class WorldManager {
	
	private static WorldManager myInstance;
	
	private ArrayList<ProtectedWorld> myWorlds = new ArrayList<>();
	
	/**
	 * Créer l'instance de WorldManager et créer les mondes passés au paramètre.
	 * @param names les noms des mondes à initialiser
	 */
	
	public WorldManager(String... names)
	{
		myInstance = this;
		
		for(String name : names)
		{
			ProtectedWorld world = new ProtectedWorld(new WorldCreator(name).createWorld());
			myWorlds.add(world);
			
			NodeList list = Constants.WORLD_DOCUMENT.getDocumentElement().getChildNodes();
			for(int i = 0 ; i < list.getLength() ; i++)
			{
				if(list.item(i).getNodeType() == Node.ELEMENT_NODE && ((Element) list.item(i)).getAttribute("name").equals(name))
				{
					world.deserialize((Element) list.item(i));
					break;
				}
			}
		}
		
		new Timer().runTaskTimer(MainClass.getInstance(), 20, 20);
	}
	
	/**
	 * @param name le nom du monde dont on veut récupérer l'instance
	 * @return l'instance du monde ou null si il n'a pas été crée
	 */
	
	public ProtectedWorld getWorld(String name)
	{
		for(ProtectedWorld world : myWorlds)
		{
			if(world.getName().equals(name))
				return world;
		}
		
		return null;
	}
	
	/**
	 * @param world le monde bukkit à récupérer sous sa forme protégée
	 * @return le ProtectedWorld correspondant au monde bukkit
	 */
	
	public ProtectedWorld getWorld(World world)
	{
		for(ProtectedWorld pWorld : myWorlds)
		{
			if(pWorld.getName().equals(world.getName()))
				return pWorld;
		}
		
		return null;
	}
	
	/**
	 * Sérialiser tous les mondes dans l'élément passé en paramètre.
	 */
	
	public void serializeAllWorlds(Element toWrite)
	{
		for(ProtectedWorld world : myWorlds)
			world.serialize(toWrite);
	}
	
	/**
	 * @return l'instance de WorldManager
	 */
	
	public static WorldManager getInstance()
	{
		return myInstance;
	}
	
	private class Timer extends BukkitRunnable {

		@Override
		public void run()
		{
			for(ProtectedWorld world : myWorlds) // appel chaque seconde du timer des mondes
				world.doTime();
		}
	}
}
