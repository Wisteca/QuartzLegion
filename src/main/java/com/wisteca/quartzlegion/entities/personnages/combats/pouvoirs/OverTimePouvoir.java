package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.utils.effects.Effect;

/**
 * Repr�sente un DOT (Damage Over Time), un pouvoir qui se d�clenche tous les temps de temps.
 * @author Wisteca
 */

public abstract class OverTimePouvoir extends SpacePouvoir {

	private int myCurrentTime, myBetweenTime;
	
	/**
	 * Construire un DOT en pr�cisant chaque attributs.
	 * @param perso le presonnage sur lequel le DOT s'applique
	 * @param totalTime le temps total en ticks pendant lequel le DOT aura effet
	 * @param betweenTime le temps en ticks entre chaque appel de la m�thode action()
	 * @param size la taille du pouvoir dans la r�serve du personnage
	 * @param name le nom du pouvoir
	 * @param effect l'effet de particules qui appara�tra quand le pouvoir tourne sur le personnage
	 */
	
	public OverTimePouvoir(Personnage perso, int totalTime, int betweenTime, int size, String name, Effect effect)
	{
		super(perso, totalTime, size, name, effect);
		myBetweenTime = betweenTime;
		myCurrentTime = 0;
	}
	
	/**
	 * Construire un DOT en le d�serializant
	 * @param element l'�l�ment dans lequel le pouvoir � �t� s�rializ� auparavant
	 */
	
	public OverTimePouvoir(Element element)
	{
		super(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
		((Element) toWrite.getElementsByTagName(getName().replace(' ', '_')).item(0)).setAttribute("currentTime", Integer.toString(myCurrentTime));
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		myCurrentTime = Integer.valueOf(((Element) element.getElementsByTagName(getName().replace(' ', '_')).item(0)).getAttribute("currentTime"));
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
	 * M�thode appel�e tout les temps de ticks pour effectuer une action sur le personnage.
	 */
	
	protected abstract void action();
	
	/**
	 * It�re tous les DOT de la liste de la classe {@link Constants} et retourne une instance d�s�rialis�e du pouvoir correspondant � l'�l�ment.
	 * @param name le nom du pouvoir � d�s�rialis�
	 * @param target le personnage cibl� par le DOT
	 * @return une instance d�s�rialis�e du pouvoir ou null si aucun pouvoir ne porte le nom
	 */
	
	public static OverTimePouvoir getPouvoirByName(String name, Personnage target)
	{
		for(Class<? extends OverTimePouvoir> clazz : Constants.OVERTIME_LIST)
		{
			try {
				
				OverTimePouvoir op = clazz.getConstructor(Personnage.class).newInstance(target);
				if(op.getName().equals(name))
					return op;
			
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return null;
	}
}
