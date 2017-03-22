package com.wisteca.quartzlegion;

import org.bukkit.plugin.java.JavaPlugin;

import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Joueur;

/**
 * La classe principale du plugin.
 * @author Wisteca
 */

public class MainClass extends JavaPlugin {
	
	private static MainClass myInstance;
	
	@Override
	public void onEnable()
	{
		myInstance = this;
		
		try {
			
			Class.forName("com.wisteca.quartzlegion.data.Constants"); // initialisation de la classe
			
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		new PersonnageManager(); // création du singletone
		
		getCommand("test").setExecutor(new Test());
	}
	
	@Override
	public void onDisable()
	{
		for(Joueur j : PersonnageManager.getInstance().getOnlinePlayers())
			j.disconnect(); // appel de la méthode de déconnexion de chaque joueurs
	}
	
	/**
	 * @return l'instance unique de la classe
	 */
	
	public static MainClass getInstance()
	{
		return myInstance;
	}
}
