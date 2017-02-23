package com.wisteca.quartzlegion.utils;

import java.util.ArrayList;
import java.util.List;

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
}
