package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.utils.effects.Effect;

/**
 * Représente un pouvoir qui a un effet de durée sur un personnage pendant lequel il aura un effet néfaste ou bienfaisant.
 * @author Wisteca
 */

public abstract class SpacePouvoir implements Pouvoir {
	
	private int myTotalTime, mySize, myRemainingTime = 0;
	protected Personnage myTarget = null;
	private String myName;
	private Effect myEffect;
	
	/**
	 * Construire un SpacePouvoir en précisant chaque attributs.
	 * @param perso le personnage touché par le pouvoir
	 * @param totalTime le temps total en ticks pendant lequel le pouvoir sera actif
	 * @param size la taille que prend le pouvoir dans la réserve du personnage
	 * @param name le nom du pouvoir qui apparaît dans la liste des pouvoirs qui tournent d'un joueur
	 * @param effect l'effet de particules qui apparaîtra pendant que le pouvoir tourne sur le personnage
	 */
	
	public SpacePouvoir(Personnage perso, int totalTime, int size, String name, Effect effect)
	{
		myTarget = perso;
		myTotalTime = totalTime;
		mySize = size;
		myName = name;
		myEffect = effect;
	}
	
	/**
	 * Construire un SpacePouvoir en le déserializant
	 * @param element l'élément dans lequel le pouvoir a été sérializé auparavant
	 */
	
	public SpacePouvoir(Element element)
	{
		deserialize(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		Element pouvoir = toWrite.getOwnerDocument().createElement(myName.replace(' ', '_'));
		toWrite.appendChild(pouvoir);
		pouvoir.setAttribute("remainingTime", Integer.toString(myRemainingTime));
		
		if(myTarget != null)
			pouvoir.setAttribute("personnage", myTarget.getUniqueId().toString());
	}
	
	@Override
	public void deserialize(Element element)
	{
		try {
			
			myName = element.getNodeName().replace('_', ' ');
			Element pouvoir = (Element) Constants.POUVOIRS_DOCUMENT.getElementsByTagName(element.getNodeName()).item(0);
			
			if(element.hasAttribute("personnage"))
				myTarget = (Personnage) PersonnageManager.getInstance().getPersonnage(UUID.fromString(element.getAttribute("personnage")));
			
			myRemainingTime = Integer.valueOf(element.getAttribute("remainingTime"));
			myTotalTime = Integer.valueOf(pouvoir.getAttribute("totalTime"));
			mySize = Integer.valueOf(pouvoir.getAttribute("size"));
			myEffect = Effect.getEffectByName(pouvoir.getAttribute("effect"));
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean launch()
	{
		if(isRunning() == false)
		{
			myRemainingTime = myTotalTime;
			myTarget.addSpacePouvoir(this);
			if(myEffect != null)
				myEffect.launch(myTarget.getLocation().add(0, 1, 0));
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void doTime()
	{
		if(myEffect != null)
			myEffect.doTime();
		
		if(isRunning())
			myRemainingTime--;
	}
	
	/**
	 * Appelé lorsque le pouvoir finit d'agir ou pour l'interrompre.
	 */
	
	public void stop()
	{
		myTarget.removeSpacePouvoir(this);
		myRemainingTime = 0;
	}
	
	/**
	 * @return le temps total pendant lequel le pouvoir aura un effet sur le personnage
	 */
	
	public int getTotalTime()
	{
		return myTotalTime;
	}
	
	/**
	 * @return le temps restant avant que le pouvoir n'ait plus d'effet sur le personnage, 0 si le pouvoir ne tourne pas sur le personnage
	 */
	
	public int getRemainingTime()
	{
		return myRemainingTime;
	}
	
	public boolean isRunning()
	{
		return myRemainingTime > 0;
	}
	
	/**
	 * @return la taille du pouvoir dans la réserve à pouvoirs du personnage
	 */
	
	public int getSize()
	{
		return mySize;
	}
	
	/**
	 * @return le personnage sur lequel le pouvoir est lancé ou est prêt à être lancé
	 */
	
	public Personnage getTarget()
	{
		return myTarget;
	}
	
	/**
	 * @return le nom du pouvoir
	 */
	
	public String getName()
	{
		return myName;
	}
	
	/**
	 * @return l'effet de particules du pouvoir ou null si il n'y en a pas
	 */
	
	public Effect getEffect()
	{
		return myEffect;
	}
}
