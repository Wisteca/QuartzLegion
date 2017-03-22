package com.wisteca.quartzlegion.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
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
	
	/**
	 * Supprimer tous les éléments d'un noeud qui ont un attribut portant le nom attrName et la valeur attrValue.
	 * @param parent l'élément dans lequel rechercher et supprimer les éléments enfants
	 * @param attrName le nom de l'attribut que l'élément doit avoir
	 * @param attrValue la valeur que doit avoir l'attribut, ou null pour ignorer ce paramètre
	 */
	
	public static void removeElementsWhoHasAttribute(Element parent, String attrName, String attrValue)
	{
		NodeList childs = parent.getChildNodes();
		for(int i = 0 ; i < childs.getLength() ; i++)
		{
			Node node = childs.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			Element child = (Element) node;
			if(child.hasAttribute(attrName) && child.getAttribute(attrName).equals(attrValue))
				parent.removeChild(child);
		}
	}
	
	// Maintenir le tableau à jour
	private static String[][] myTransfo = {
			{"é", "&amp;eacute;"}, 
			{"è", "&amp;#232;"},
			{" ", "&amp;#160;"},
			{"§", "&amp;#167;"},
			};
	
	/**
	 * Changer une châine de caractères contenant des espaces, des é ou è etc pour qu'elle soit valide dans un fichier XML.
	 * @param str la chaîne de base
	 * @return la chaîne modifiée
	 */
	
	public static String changeForXML(String str)
	{
		for(int i = 0 ; i < myTransfo.length ; i++)
			str = str.replaceAll(myTransfo[i][0], myTransfo[i][1]);
		System.out.println(str);
		return str;
	}
	
	/**
	 * Inverse de changeForXML.
	 * @param str la chaîne tranformée
	 * @return la chaîne normale
	 */
	
	public static String changeFromXML(String str)
	{
		for(int i = 0 ; i < myTransfo.length ; i++)
			str = str.replaceAll(myTransfo[i][1], myTransfo[i][0]);
		return str;
	}
	
	/**
	 * @param inside la position à tester
	 * @param point1 le point 1 du rectangle
	 * @param point2 le point 2 du rectangle
	 * @return true si la position inside est entre le point 1 et 2
	 */
	
	public static boolean isInside(Location inside, Location point1, Location point2) 
	{
		double x1 = Math.min(point1.getX(), point2.getX());
		double y1 = Math.min(point1.getY(), point2.getY());
		double z1 = Math.min(point1.getZ(), point2.getZ());
		double x2 = Math.max(point1.getX(), point2.getX());
		double y2 = Math.max(point1.getY(), point2.getY());
		double z2 = Math.max(point1.getZ(), point2.getZ());
 
        return inside.getX() >= x1 && inside.getX() <= x2 && inside.getY() >= y1 && inside.getY() <= y2 && inside.getZ() >= z1 && inside.getZ() <= z2;
	}
}
