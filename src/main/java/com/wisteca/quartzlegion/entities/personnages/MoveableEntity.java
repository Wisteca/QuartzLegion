package com.wisteca.quartzlegion.entities.personnages;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.wisteca.quartzlegion.utils.Item;

/**
 * Représente l'apparence d'une entité.
 * @author Wisteca
 */

public interface MoveableEntity {
	
	/**
	 * @return l'entité org.bukkit.entity.Entity correspondant à cette interface
	 */
	
	public Entity getBukkitEntity();
	
	/**
	 * Faire marcher l'entité jusqu'à la position donnée, sa vitesse sera ClasseSkill.VITESSE_MARCHE.
	 * @param loc la position où doit aller l'entité
	 */
	
	public void moveTo(Location to);
	
	/**
	 * Faire marcher l'entité jusqu'à la position donnée à la vitesse donnée.
	 * @param loc la position où doit aller l'entité
	 * @param speed la vitesse de déplacement
	 */
	
	public void moveTo(Location to, int speed);
	
	/**
	 * Changer la position de la tête.
	 */
	
	public void setHeadPosition(int yaw, int pitch);
	
	/**
	 * Faire un mouvement de bras pour représenter l'attaque de l'entité.
	 */
	
	public void attack();
	
	/**
	 * Faire apparaître des particules de sang.
	 */
	
	public void damage();
	
	/**
	 * Mettre un item dans la main de l'entité (ne fonctionne que si l'entité est prévue pour avoir des items en main).
	 * @param item l'item à mettre dans la main
	 */
	
	public void setItemInHand(Item item);
	
	/**
	 * @return l'item se trouvant dans la main de l'entité, ou null si elle n'en a pas.
	 */
	
	public Item getItemInHand();
}
