package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.utils.effects.Effect;
import com.wisteca.quartzlegion.utils.effects.EffectInterface.Part;

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
	
	public OverTimePouvoir(Personnage perso, int totalTime, int betweenTime, int size, String name, Effect effect, Part part, int effectBetweenTime)
	{
		super(perso, totalTime, size, name, effect, part, effectBetweenTime);
		myBetweenTime = betweenTime;
		myCurrentTime = 0;
	}
	
	@Override
	public void serialize(Element toWrite)
	{
		super.serialize(toWrite);
		
		Element dot = null;
		NodeList list = toWrite.getChildNodes();
		for(int i = 0 ; i < list.getLength() ; i++)
			if(list.item(i).getNodeType() == Node.ELEMENT_NODE && ((Element) list.item(i)).getAttribute("name").equals(getName()))
				dot = (Element) list.item(i);
		
		dot.setAttribute("currentTime", Integer.toString(myCurrentTime));
	}
	
	@Override
	public void deserialize(Element element)
	{
		myCurrentTime = Integer.valueOf(element.getAttribute("currentTime"));
		setRemainingTime(Integer.valueOf(element.getAttribute("remainingTime")));
		// tous les autres attributs sont d�j� d�finis par la classe fille
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		if(isRunning() == false)
			return;
		
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
