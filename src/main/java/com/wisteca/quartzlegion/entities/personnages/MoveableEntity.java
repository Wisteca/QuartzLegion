package com.wisteca.quartzlegion.entities.personnages;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.wisteca.quartzlegion.utils.Item;

/**
 * Repr�sente l'apparence d'une entit�.
 * @author Wisteca
 */

public interface MoveableEntity {
	
	/**
	 * @return l'entit� org.bukkit.entity.Entity correspondant � cette interface
	 */
	
	public Entity getBukkitEntity();
	
	/**
	 * Faire marcher l'entit� jusqu'� la position donn�e, sa vitesse sera ClasseSkill.VITESSE_MARCHE.
	 * @param loc la position o� doit aller l'entit�
	 */
	
	public void moveTo(Location to);
	
	/**
	 * Faire marcher l'entit� jusqu'� la position donn�e � la vitesse donn�e.
	 * @param loc la position o� doit aller l'entit�
	 * @param speed la vitesse de d�placement
	 */
	
	public void moveTo(Location to, int speed);
	
	/**
	 * Changer la position de la t�te.
	 */
	
	public void setHeadPosition(int yaw, int pitch);
	
	/**
	 * Faire un mouvement de bras pour repr�senter l'attaque de l'entit�.
	 */
	
	public void attack();
	
	/**
	 * Faire appara�tre des particules de sang.
	 */
	
	public void damage();
	
	/**
	 * Mettre un item dans la main de l'entit� (ne fonctionne que si l'entit� est pr�vue pour avoir des items en main).
	 * @param item l'item � mettre dans la main
	 */
	
	public void setItemInHand(Item item);
	
	/**
	 * @return l'item se trouvant dans la main de l'entit�, ou null si elle n'en a pas.
	 */
	
	public Item getItemInHand();
}
