package com.wisteca.quartzlegion.utils.effects;

import org.bukkit.Location;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.data.Serializer;

public interface Effect extends Serializer {
	
	/**
	 * Lancer l'effet � la position demand�e.
	 * @param center la position o� l'effet sera lanc�
	 */
	
	public void launch(Location center);
	
	/**
	 * Cette m�thode doit �tre appel�e chaque ticks pour que l'effet puisse de d�velopper sur le temps.
	 */
	
	public void doTime();
	
	/**
	 * @return le nom de l'effet
	 */
	
	public String getName();
	
	/**
	 * Va chercher dans le fichier pouvoirs.xml l'effet portant le nom pass� en param�tre, cr�er ensuite une instance de cet effet avec les attributs pr�cis�s dans le fichier.
	 * @param name le nom de l'effet
	 * @return une instance de l'effet ou null si aucun effet ne porte le nom pass� en param�tre
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
