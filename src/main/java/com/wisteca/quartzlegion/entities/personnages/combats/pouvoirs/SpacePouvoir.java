package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.utils.Utils;
import com.wisteca.quartzlegion.utils.effects.Effect;
import com.wisteca.quartzlegion.utils.effects.EffectInterface;
import com.wisteca.quartzlegion.utils.effects.EffectInterface.Part;

/**
 * Représente un pouvoir qui a un effet de durée sur un personnage pendant lequel il aura un effet néfaste ou bienfaisant.
 * @author Wisteca
 */

public abstract class SpacePouvoir implements Pouvoir {
	
	private int myTotalTime, mySize, myRemainingTime = 0, myBetweenTime;
	private Personnage myTarget = null;
	private String myName;
	private EffectInterface myEffect;
	
	/**
	 * Construire un SpacePouvoir en précisant chaque attributs.
	 * @param perso le personnage touché par le pouvoir
	 * @param totalTime le temps total en ticks pendant lequel le pouvoir sera actif
	 * @param size la taille que prend le pouvoir dans la réserve du personnage
	 * @param name le nom du pouvoir qui apparaît dans la liste des pouvoirs qui tournent d'un joueur
	 * @param effect l'effet de particules qui apparaîtra pendant que le pouvoir tourne sur le personnage
	 * @param part la partie du corps du personnage où l'effet se propagera
	 * @param betweenTime le temps entre chaque lancement de l'effet
	 */
	
	public SpacePouvoir(Personnage perso, int totalTime, int size, String name, Effect effect, Part part, int betweenTime)
	{
		myTarget = perso;
		myTotalTime = totalTime;
		mySize = size;
		myName = name;
		myBetweenTime = betweenTime;
		
		if(effect != null)
			myEffect = new EffectInterface(perso, effect, part);
	}
	
	/**
	 * Construire un SpacePouvoir en le déserializant
	 * @param element l'élément dans lequel le pouvoir a été sérializé auparavant
	 */
	
	public SpacePouvoir(Element element)
	{
		deserialize(element);
	}
	
	/**
	 * Consrtuire un buff en fonction de son nom, il se désérialisera en prenant les attributs spécifiés dans le fichier pouvoirs.xml.
	 * @param target la cible du pouvoir
	 * @param name le nom du pouvoir tel qu'il est dans le fichier pouvoir.xml
	 */
	
	public SpacePouvoir(Personnage target, String name)
	{
		myTarget = target;
		myName = name;
		init();
	}
	
	private void init()
	{
		Element state = null;
		NodeList buffs = Constants.POUVOIRS_DOCUMENT.getDocumentElement().getElementsByTagName("buffs").item(0).getChildNodes();
		for(int i = 0 ; i < buffs.getLength() ; i++)
			if(buffs.item(i).getNodeType() == Node.ELEMENT_NODE && ((Element) buffs.item(i)).getAttribute("name").equals(myName))
				state = (Element) buffs.item(i);
		
		myTotalTime = Integer.valueOf(state.getAttribute("totalTime"));
		mySize = Integer.valueOf(state.getAttribute("size"));
		
		NodeList lookForEffect = state.getChildNodes();
		for(int i = 0 ; i < lookForEffect.getLength() ; i++)
		{
			if(lookForEffect.item(i).getNodeType() == Node.ELEMENT_NODE && lookForEffect.item(i).getNodeName().equals("effect"))
			{
				Element effect = (Element) lookForEffect.item(i);
				myEffect = new EffectInterface(myTarget, Effect.getEffectByName(effect.getAttribute("name")), Part.valueOf(effect.getAttribute("part")));
				myBetweenTime = Integer.valueOf(effect.getAttribute("betweenTime"));
			}
		}
	}
	
	@Override
	public void deserialize(Element element)
	{
		myName = element.getAttribute("name");
		
		if(element.hasAttribute("personnage"))
			myTarget = (Personnage) PersonnageManager.getInstance().getPersonnage(UUID.fromString(element.getAttribute("personnage")));
		
		myRemainingTime = Integer.valueOf(element.getAttribute("remainingTime"));
		init();
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		Utils.removeElementsWhoHasAttribute(toWrite, "name", myName);
		Element pouvoir = toWrite.getOwnerDocument().createElement("pouvoir");
		toWrite.appendChild(pouvoir);
		pouvoir.setAttribute("name", myName);
		pouvoir.setAttribute("remainingTime", Integer.toString(myRemainingTime));
		
		if(myTarget != null)
			pouvoir.setAttribute("personnage", myTarget.getUniqueId().toString());
	}
	
	@Override
	public boolean launch()
	{
		if(isRunning() == false)
		{
			myRemainingTime = myTotalTime;
			myTarget.addSpacePouvoir(this);
			
			if(myEffect != null)
			{
				myEffect.setPersonnage(myTarget);
				myEffect.repeat(myBetweenTime, myTotalTime);
			}
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
		{
			myRemainingTime--;
			if(isRunning() == false)
				stop();
		}
	}
	
	/**
	 * Change la cible du pouvoir SI et seulement SI le pouvoir ne tourne pas déjà sur un autre personnage.
	 * @param target la nouvelle cible du pouvoir.
	 */
	
	public void setTarget(Personnage target)
	{
		if(isRunning() == false)
			myTarget = target;
	}
	
	/**
	 * Appelé lorsque le pouvoir finit d'agir ou pour l'interrompre.
	 */
	
	public void stop()
	{
		myTarget.removeSpacePouvoir(this);
		myRemainingTime = 0;
		if(myEffect != null)
			myEffect.stop();
	}
	
	/**
	 * @return le temps total pendant lequel le pouvoir aura un effet sur le personnage
	 */
	
	public int getTotalTime()
	{
		return myTotalTime;
	}
	
	/**
	 * Change le temps restant au pouvoir seulement SI le pouvoir n'est pas en train de tourner.
	 * @param time le nouveau temps restant
	 */
	
	protected void setRemainingTime(int time)
	{
		if(isRunning() == false)
			myRemainingTime = time;
	}
	
	/**
	 * @return le temps restant avant que le pouvoir n'ait plus d'effet sur le personnage, 0 si le pouvoir ne tourne pas sur le personnage
	 */
	
	public int getRemainingTime()
	{
		return myRemainingTime;
	}
	
	/**
	 * @return true si le pouvoir tourne sur un personnage
	 */
	
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
		return myEffect.getEffect();
	}
}
