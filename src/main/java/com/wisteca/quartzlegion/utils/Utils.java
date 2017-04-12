package com.wisteca.quartzlegion.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Une classe static qui regroupe des m�thodes utiles.
 * @author Wisteca
 */

public class Utils {
	
	/**
	 * R�cup�rer une cha�ne de caract�res sous forme de collection, avec pour chaque element de la collection une partie de la cha�ne de caract�re qui ne d�passe pas la limite d'espaces
	 * pass�e en param�tre, sans couper les mots.
	 * @param text le texte � d�couper
	 * @param lineSize la taille maximum d'une ligne
	 * @return une collection contenant la cha�ne de d�part d�coup�e
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
	 * Supprimer un �l�ment si il existe dans ce noeud.
	 * @param parent le noeud parent, dans lequel l'�l�ment � supprimer se touve
	 * @param name le nom de l'�l�ment � supprimer
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
	 * Supprimer tous les �l�ments d'un noeud qui ont un attribut portant le nom attrName et la valeur attrValue.
	 * @param parent l'�l�ment dans lequel rechercher et supprimer les �l�ments enfants
	 * @param attrName le nom de l'attribut que l'�l�ment doit avoir
	 * @param attrValue la valeur que doit avoir l'attribut, ou null pour ignorer ce param�tre
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
	
	// Maintenir le tableau � jour
	private static String[][] myTransfo = {
			{"�", "&amp;eacute;"}, 
			{"�", "&amp;#232;"},
			{" ", "&amp;#160;"},
			{"�", "&amp;#167;"},
			};
	
	/**
	 * Changer une ch�ine de caract�res contenant des espaces, des � ou � etc pour qu'elle soit valide dans un fichier XML.
	 * @param str la cha�ne de base
	 * @return la cha�ne modifi�e
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
	 * @param str la cha�ne tranform�e
	 * @return la cha�ne normale
	 */
	
	public static String changeFromXML(String str)
	{
		for(int i = 0 ; i < myTransfo.length ; i++)
			str = str.replaceAll(myTransfo[i][1], myTransfo[i][0]);
		return str;
	}
	
	/**
	 * @param inside la position � tester
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
	
	/**
	 * R�cup�rer l'objet d�clar� dans la classe donn�e, par r�flexion.
	 * @param fieldName le nom du field � r�cup�rer
	 * @param clazz la classe dans laquelle se trouve la variable que l'on veut r�cup�rer
	 * @param object l'instance de la classe contenant le field, ou null si le field est d�clar� statique dans la classe
	 * @return l'objet contenu dans l'instance pass�e en param�tre
	 */
	
	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object)
	{
		try {
			
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Rechercher les noms de toutes les variables contenant la classe pass�e en param�tre.
	 * @param lookFor la classe dans laquelle rechercher les variables
	 * @param findName r�cup�rer le nom des variables d�clar�es par cette classe
	 * @return une liste des noms des variables
	 */
	
	public static List<String> getNamesOfFields(Class<?> lookFor, Class<?> findName)
	{
		List<String> list = new ArrayList<>();
		for(Field field : lookFor.getDeclaredFields())
		{
			if(field.getType().equals(findName))
				list.add(field.getName());
		}
		
		return list;
	}
	
	/**
	 * @return le nom du package NMS.
	 */
	
	public static String getNMSPackage()
	{
		return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}
}
