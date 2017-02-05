package com.wisteca.quartzlegion.data;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

/**
 * 
 * Permet de sérializer et déserializer des objets sous forme XML
 * @author Wisteca
 *
 */

public interface Serializer {
	
	/**
	 * Permet de sérializer un objet dans l'élément passé en paramètre
	 * @param toWrite l'élément dans lequel l'objet devra se sérializer
	 * @throws ParserConfigurationException si cette exeption est levée, il y a un sérieux problème...
	 */
	public void serialize(Element toWrite) throws ParserConfigurationException;
	
	/**
	 * Permet de déserializer un objet, <strong>il faut que l'objet se soit sérializé dans l'élément avant d'appeler cette méthode !</strong>
	 * @param element l'élément dans lequel l'objet a été sérializé
	 */
	public void deserialize(Element element);
	
}
