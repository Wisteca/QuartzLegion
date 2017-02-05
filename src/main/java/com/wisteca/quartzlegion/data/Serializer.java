package com.wisteca.quartzlegion.data;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

/**
 * 
 * Permet de s�rializer et d�serializer des objets sous forme XML
 * @author Wisteca
 *
 */

public interface Serializer {
	
	/**
	 * Permet de s�rializer un objet dans l'�l�ment pass� en param�tre
	 * @param toWrite l'�l�ment dans lequel l'objet devra se s�rializer
	 * @throws ParserConfigurationException si cette exeption est lev�e, il y a un s�rieux probl�me...
	 */
	public void serialize(Element toWrite) throws ParserConfigurationException;
	
	/**
	 * Permet de d�serializer un objet, <strong>il faut que l'objet se soit s�rializ� dans l'�l�ment avant d'appeler cette m�thode !</strong>
	 * @param element l'�l�ment dans lequel l'objet a �t� s�rializ�
	 */
	public void deserialize(Element element);
	
}
