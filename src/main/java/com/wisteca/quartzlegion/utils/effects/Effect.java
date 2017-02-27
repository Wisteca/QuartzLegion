package com.wisteca.quartzlegion.utils.effects;

import org.bukkit.Location;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.data.Serializer;

public interface Effect extends Serializer {
	
	/**
	 * Lancer l'effet à la position demandée.
	 * @param center la position où l'effet sera lancé
	 */
	
	public void launch(Location center);
	
	/**
	 * Cette méthode doit être appelée chaque ticks pour que l'effet puisse de développer sur le temps.
	 */
	
	public void doTime();
	
	/**
	 * @return le nom de l'effet
	 */
	
	public String getName();
	
	/**
	 * Va chercher dans le fichier pouvoirs.xml l'effet portant le nom passé en paramètre, créer ensuite une instance de cet effet avec les attributs précisés dans le fichier.
	 * @param name le nom de l'effet
	 * @return une instance de l'effet ou null si aucun effet ne porte le nom passé en paramètre
	 */
	
	public static Effect getEffectByName(String name)
	{
		NodeList list = Constants.POUVOIRS_DOCUMENT.getElementsByTagName("effects").item(0).getChildNodes();
		for(int i = 0 ; i < list.getLength() ; i++)
		{
			if((list.item(i) instanceof Element) == false)
				continue;
			
			Element element = (Element) list.item(i);
			if(element.getNodeName().equals(name))
			{
				for(Class<? extends Effect> type : Constants.EFFECTS_LIST)
				{
					try {
						
						if(type.getSimpleName().equals(element.getAttribute("type")))
							return type.getConstructor(Element.class).newInstance(element);
				
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		
		return null;
	}
}
