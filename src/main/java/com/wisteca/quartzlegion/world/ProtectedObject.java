package com.wisteca.quartzlegion.world;

import org.bukkit.Location;

/**
 * Repr�sente un bloc, un ArmorStand,... qui peut �tre cass� dans le monde et qui doit �tre r�par�.
 * @author Wisteca
 */

public interface ProtectedObject {
	
	/**
	 * R�initialiser l'objet comme il l'est par d�faut.
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
