package com.wisteca.quartzlegion.utils.effects;

import com.wisteca.quartzlegion.entities.personnages.Personnage;

/**
 * Classe faisant interface avec un effet, permet une utilisation r�p�t�e et une gestion de la position de l'effet par rapport au personnage.
 * @author Wisteca
 */

public class EffectInterface {
	
	private Personnage myPerso;
	private Effect myEffect;
	private Part myPart;
	private int myBetweenTime, myCurrentTime, myTotalTime, myRemainingTime;
	
	/**
	 * @param perso le personnage utilis� pour le calcul de la position des particules
	 * @param effect l'effet qui sera lanc� par l'interface
	 * @param part la partie du corps du personnage sur laquelle l'effet se propagera
	 */
	
	public EffectInterface(Personnage perso, Effect effect, Part part)
	{
		myPerso = perso;
		myEffect = effect;
		myPart = part;
	}
	
	/**
	 * @param newEffect le nouvel effet
	 */
	
	public void setEffect(Effect newEffect)
	{
		myEffect = newEffect;
	}
	
	/**
	 * @return l'effet utilis� par l'interface
	 */
	
	public Effect getEffect()
	{
		return myEffect;
	}
	
	/**
	 * @param part la nouvelle partie du corps sur laquelle l'effet se propagera
	 */
	
	public void setLocation(Part part)
	{
		myPart = part;
	}
	
	/**
	 * @return la partie du corps du personnage sur laquelle l'effet se propage
	 */
	
	public Part getLocation()
	{
		return myPart;
	}
	
	/**
	 * @param perso le nouveau personnage
	 */
	
	public void setPersonnage(Personnage perso)
	{
		myPerso = perso;
	}
	
	/**
	 * @return le personnage utilis� pour lancer l'effet
	 */
	
	public Personnage getPersonnage()
	{
		return myPerso;
	}
	
	/**
	 * Lance un timer qui lancera la pouvoir tout les tant de temps.
	 * @param time le temps entre chaque lancement
	 * @param totalTime la dur�e pendant laquelle l'effet se lancera
	 */
	
	public void repeat(int time, int totalTime)
	{
		myBetweenTime = time;
		myTotalTime = totalTime;
		myCurrentTime = 0;
	}
	
	/**
	 * M�thode � appeler tous les ticks, pour le timer.
	 */
	
	public void doTime()
	{
		if(myPerso != null)
			myEffect.updateLocation(myPerso.getCurrentLocation(myPart));
		
		myEffect.doTime();
		
		if(myBetweenTime == 0)
			return;
		
		myCurrentTime++;
		myRemainingTime++;
		
		if(myCurrentTime >= myBetweenTime)
		{
			myEffect.launch(myPerso.getCurrentLocation(myPart));
			myCurrentTime = 0;
		}
		
		if(myRemainingTime >= myTotalTime)
			stop();
	}
	
	/**
	 * @return true si l'effet est en train de se r�p�ter
	 */
	
	public boolean isRunning()
	{
		return myBetweenTime != 0;
	}
	
	/**
	 * M�thode appel�e automatiquement � la fin du temps total de l'effet, ou � appeler pour terminer l'effet pr�matur�ment.
	 */
	
	public void stop()
	{
		myBetweenTime = 0;
		myTotalTime = 0;
		myRemainingTime = 0;
		myCurrentTime = 0;
	}
	
	/**
	 * Enum�ration repr�sentant une partie du corps.
	 * @author Wisteca
	 */
	
	public static enum Part {
		
		HEAD,
		CENTER,
		RIGHT_SHOULDER,
		LEFT_SHOULDER,
		RIGHT_FOOT,
		LEFT_FOOT;
	}
}























