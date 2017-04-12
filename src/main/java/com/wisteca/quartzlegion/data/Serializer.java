package com.wisteca.quartzlegion.data;

import org.w3c.dom.Element;

/**
 * Permet de s�rializer et d�serializer des objets sous forme XML.
 * @author Wisteca
 */

public interface Serializer {
	
	/**
	 * Permet de s�rializer un objet dans l'�l�ment pass� en param�tre.
	 * @param toWrite l'�l�ment dans lequel l'objet devra se s�rializer
	 */
	
	public void serialize(Element toWrite);
	
	/**
	 * Permet de d�serializer un objet, <strong>il faut que l'objet se soit s�rializ� dans l'�l�ment avant d'appeler cette m�thode !</strong>
	 * @param element l'�l�ment dans lequel l'objet a �t� s�rializ�
	 */
	
	public void deserialize(Element element);
	
}
