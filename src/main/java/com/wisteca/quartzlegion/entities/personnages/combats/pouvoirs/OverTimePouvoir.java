package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.utils.effects.Effect;

/**
 * Représente un DOT (Damage Over Time), un pouvoir qui se déclenche tous les temps de temps.
 * @author Wisteca
 */

public abstract class OverTimePouvoir extends SpacePouvoir {

	private int myCurrentTime, myBetweenTime;
	
	/**
	 * Construire un DOT en précisant chaque attributs.
	 * @param perso le presonnage sur lequel le DOT s'applique
	 * @param totalTime le temps total en ticks pendant lequel le DOT aura effet
	 * @param betweenTime le temps en ticks entre chaque appel de la méthode action()
	 * @param size la taille du pouvoir dans la réserve du personnage
	 * @param name le nom du pouvoir
	 * @param effect l'effet de particules qui apparaîtra quand le pouvoir tourne sur le personnage
	 */
	
	public OverTimePouvoir(Personnage perso, int totalTime, int betweenTime, int size, String name, Effect effect)
	{
		super(perso, totalTime, size, name, effect);
		myBetweenTime = betweenTime;
		myCurrentTime = 0;
	}
	
	/**
	 * Construire un DOT en le déserializant
	 * @param element l'élément dans lequel le pouvoir à été sérializé auparavant
	 */
	
	public OverTimePouvoir(Element element)
	{
		super(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
		toWrite.setAttribute("betweenTime", Integer.toString(myBetweenTime));
		toWrite.setAttribute("currentTime", Integer.toString(myCurrentTime));
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		myBetweenTime = Integer.valueOf(element.getAttribute("betweenTime"));
		myCurrentTime = Integer.valueOf(element.getAttribute("currentTime"));
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		if(myCurrentTime == myBetweenTime)
		{
			action();
			myCurrentTime = 0;
		}
		
		myCurrentTime++;
	}
	
	/**
	 * Méthode appelée tout les temps de ticks pour effectuer une action sur le personnage.
	 */
	
	protected abstract void action();
	
	/**
	 * Itère tous les DOT de la liste de la classe {@link Constants} et retourne une instance déserializée du pouvoir correspondant à l'élément.
	 * @param element l'élément dans lequel un DOT à été sérializé auparavant
	 * @return une instance déserializée du pouvoir
	 */
	
	public static OverTimePouvoir deserializePouvoir(Element element)
	{
		String name = element.getAttribute("name");
		Personnage perso = (Personnage) PersonnageManager.getInstance().getPersonnage(UUID.fromString(element.getAttribute("personnage")));
		
		for(Class<?> clazz : Constants.OVERTIME_LIST)
		{
			try {
				
				String pouvoirName = (String) clazz.getMethod("getName").invoke(clazz.getConstructor(Personnage.class).newInstance(perso));
				if(name.equals(pouvoirName))
					return (OverTimePouvoir) clazz.getConstructor(Element.class).newInstance(element);
			
			} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException | InstantiationException ex) {
				ex.printStackTrace();
			}
		}
		
		return null;
	}
}
