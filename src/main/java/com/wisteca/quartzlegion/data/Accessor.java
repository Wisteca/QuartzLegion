package com.wisteca.quartzlegion.data;

import java.util.UUID;

import org.w3c.dom.Element;

/**
 * Cette interface sert � utiliser les donn�es des joueurs soit par BDD sur le serveur, ou alors par fichiers pour faire des tests en local.
 * @author Wisteca
 */

public interface Accessor {
	
	/**
	 * @param uuid l'uuid du joueur
	 * @return true si le joueur est d�j� enregistr� dans la bdd/le fichier xml
	 */
	
	public boolean isCreated(UUID uuid);
	
	/**
	 * Ins�rer le joueur dans la bdd/le fichier xml.
	 * @param uuid l'uuid du joueur � ins�rer
	 * @param element l'�l�ment du joueur
	 */
	
	public void create(UUID uuid, Element element);
	
	/**
	 * Remplir le champ xml de la bdd/du fichier, il faut que l'uuid soit d�j� ins�r�e dans le fichier !
	 * @param uuid l'uuid du joueur
	 * @param element l'�l�ment � ins�rer
	 */
	
	public void setXML(UUID uuid, Element element);
	
	/**
	 * @param uuid l'uuid du joueur
	 * @return l'�l�ment xml du joueur
	 */
	
	public Element getXML(UUID uuid);
	
	/**
	 * Cr�er un nouvel accesseur qui pourra �tre utilis� pour communiquer avec la bdd ou avec le fichier xml.
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
	 * D�finit les deux mani�res de sauvegarder les donn�es.
	 * @author Wisteca
	 */
	
	public static enum AccessorMode {
		
		BDD,
		XML;
	}
}
