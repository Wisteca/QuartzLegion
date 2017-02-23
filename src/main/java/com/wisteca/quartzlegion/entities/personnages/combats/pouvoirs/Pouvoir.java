package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import com.wisteca.quartzlegion.data.Serializer;

/**
 * L'interface dont tous les pouvoirs h�ritent.
 * @author Wisteca
 */

public interface Pouvoir extends Serializer {
	
	/**
	 * Permet de lancer le pouvoir.
	 * @return true si le pouvoir a p� se lancer et false si un probl�me est survenu (pas assez d'�nergie, pas les comp�tences requises,...)
	 */
	
	public boolean launch();
	
	/**
	 * M�thode appel�e chaque ticks par le personnage pour effectuer des actions r�guli�res.
	 */
	
	public void doTime();
	
}
