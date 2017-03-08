package com.wisteca.quartzlegion.utils;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Une classe static qui regroupe des méthodes utiles.
 * @author Wisteca
 */

public class Utils {
	
	/**
	 * Récupérer une chaîne de caractères sous forme de collection, avec pour chaque element de la collection une partie de la chaîne de caractère qui ne dépasse pas la limite d'espaces
	 * passée en paramètre, sans couper les mots.
	 * @param text le texte à découper
	 * @param lineSize la taille maximum d'une ligne
	 * @return une collection contenant la chaîne de départ découpée
	 */
	
	public static List<String> getTextWithLineFeed(String text, int lineSize)
	{
		List<String> textWithLineFeed = new ArrayList<String>();
		String[] split = text.split(" ");
		StringBuilder lineFeed = new StringBuilder();
		
		for(String str : split)
		{
			lineFeed.append(str + " ");
			
			if(lineFeed.length() >= lineSize || split[split.length - 1].equals(str))
			{
				textWithLineFeed.add(lineFeed.toString());
				lineFeed = new StringBuilder();
			}
		}
		
		return textWithLineFeed;
	}
	
	/**
	 * Supprimer un élément si il existe dans ce noeud.
	 * @param parent le noeud parent, dans lequel l'élément à supprimer se touve
	 * @param name le nom de l'élément à supprimer
	 */
	
	public static void removeElementIfExist(Element parent, String name)
	{
		NodeList list = parent.getChildNodes();
		for(int i = 0 ; i < list.getLength() ; i++)
		{
			Node node = list.item(i);
			if(node.getNodeName().equals(name))
				parent.removeChild(node);
		}				
	}
}
