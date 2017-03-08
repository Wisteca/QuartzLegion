package com.wisteca.quartzlegion.data;

import java.util.UUID;

import org.w3c.dom.Element;

/**
 * Cette interface sert à utiliser les données des joueurs soit par BDD sur le serveur, ou alors par fichiers pour faire des tests en local.
 * @author Wisteca
 */

public interface Accessor {
	
	/**
	 * @param uuid l'uuid du joueur
	 * @return true si le joueur est déjà enregistré dans la bdd/le fichier xml
	 */
	
	public boolean isCreated(UUID uuid);
	
	/**
	 * Insérer le joueur dans la bdd/le fichier xml.
	 * @param uuid l'uuid du joueur à insérer
	 * @param element l'élément du joueur
	 */
	
	public void create(UUID uuid, Element element);
	
	/**
	 * Remplir le champ xml de la bdd/du fichier, il faut que l'uuid soit déjà insérée dans le fichier !
	 * @param uuid l'uuid du joueur
	 * @param element l'élément à insérer
	 */
	
	public void setXML(UUID uuid, Element element);
	
	/**
	 * @param uuid l'uuid du joueur
	 * @return l'élément xml du joueur
	 */
	
	public Element getXML(UUID uuid);
	
	/**
	 * Créer un nouvel accesseur qui pourra être utilisé pour communiquer avec la bdd ou avec le fichier xml.
	 * @return un objet BDDAccessor ou XMLAccessor suivant la valeur contenue dans {@link Constants}
	 */
	
	public static Accessor createNewAccessor()
	{
		if(Constants.ACCESSOR_MODE.equals(AccessorMode.BDD))
			return new BDDAccessor();
		else
			return new XMLAccessor();
	}
	
	/**
	 * Définit les deux manières de sauvegarder les données.
	 * @author Wisteca
	 */
	
	public static enum AccessorMode {
		
		BDD,
		XML;
	}
}
