package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import com.wisteca.quartzlegion.data.Serializer;

/**
 * L'interface dont tous les pouvoirs héritent.
 * @author Wisteca
 */

public interface Pouvoir extends Serializer {
	
	/**
	 * Permet de lancer le pouvoir.
	 * @return true si le pouvoir a pû se lancer et false si un problème est survenu (pas assez d'énergie, pas les compétences requises,...)
	 */
	
	public boolean launch();
	
	/**
	 * Méthode appelée chaque ticks par le personnage pour effectuer des actions régulières.
	 */
	
	public void doTime();
	
}
