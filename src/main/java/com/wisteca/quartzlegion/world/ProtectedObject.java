package com.wisteca.quartzlegion.world;

import org.bukkit.Location;

/**
 * Représente un bloc, un ArmorStand,... qui peut être cassé dans le monde et qui doit être réparé.
 * @author Wisteca
 */

public interface ProtectedObject {
	
	/**
	 * Réinitialiser l'objet comme il l'est par défaut.
	 */
	
	public void replace();
	
	/**
	 * @return la zone dans laquelle se trouve l'objet
	 */
	
	public Area getArea();
	
	/**
	 * @return le monde dans lequel se trouve l'obje
	 */
	
	public ProtectedWorld getWorld();
	
	/**
	 * @return la position de l'objet
	 */
	
	public Location getLocation();
}
