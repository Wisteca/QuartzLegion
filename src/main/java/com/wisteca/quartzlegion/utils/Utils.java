package com.wisteca.quartzlegion.utils;

import java.util.ArrayList;
import java.util.List;

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
}
