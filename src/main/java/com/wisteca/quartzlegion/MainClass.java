package com.wisteca.quartzlegion;

import org.bukkit.plugin.java.JavaPlugin;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;

public class MainClass extends JavaPlugin {
	
	private static MainClass myInstance;
	
	@Override
	public void onEnable()
	{
		myInstance = this;
		
		Constants.init();
		new PersonnageManager();
		
		getCommand("test").setExecutor(new Test());
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public static MainClass getInstance()
	{
		return myInstance;
	}
}
