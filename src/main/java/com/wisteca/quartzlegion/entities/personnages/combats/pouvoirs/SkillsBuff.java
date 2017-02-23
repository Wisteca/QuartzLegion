package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.effects.Effect;

/**
 * Repr�sente un buff des comp�tences du personnage durant un certain temps.
 * @author Wisteca
 */

public class SkillsBuff extends SpacePouvoir {
	
	private HashMap<Skill, Integer> myModifiedSkills;
	
	/**
	 * Construire un buff en pr�cisant chaque attributs.
	 * @param target le personnage qui b�n�ficiera du buff
	 * @param totalTime le temps total en ticks pendant lequel le pouvoir aura effet
	 * @param size la taille du pouvoir dans la r�serve du personnage
	 * @param effect l'effet de particules qui appara�tra quand le pouvoir tourne, ou null pour aucun effet
	 * @param modifications les comp�tences modifi�es en bien ou en mal
	 * @param name le nom du pouvoir
	 */
	
	public SkillsBuff(Personnage target, int totalTime, int size, Effect effect, HashMap<Skill, Integer> modifications, String name)
	{
		super(target, totalTime, size, name, effect);
		myModifiedSkills = new HashMap<>(modifications);
	}
	
	/**
	 * Construire un buff en le d�serializant
	 * @param element l'�l�ment dans lequel le pouvoir � �t� s�rializ� auparavant
	 */
	
	public SkillsBuff(Element element)
	{
		super(element);
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		Element skills = (Element) ((Element) Constants.POUVOIRS_DOCUMENT.getElementsByTagName(element.getNodeName()).item(0)).getElementsByTagName("skills").item(0);
		myModifiedSkills = new HashMap<>();
		for(Skill skill : Skill.values())
		{
			if(skills.hasAttribute(skill.toString()) == false)
				continue;
			
			myModifiedSkills.put(skill, Integer.valueOf(skills.getAttribute(skill.toString())));
		}
	}
	
	/**
	 * Change la cible du pouvoir SI et seulement SI le pouvoir ne tourne pas d�j� sur un autre personnage.
	 * @param target la nouvelle cible du pouvoir.
	 */
	
	public void setTarget(Personnage target)
	{
		if(isRunning() == false)
			myTarget = target;
	}
	
	/**
	 * @return une map des comp�tences modifi�es par ce pouvoir
	 */
	
	public HashMap<Skill, Integer> getModifiedSkills()
	{
		return new HashMap<>(myModifiedSkills);
	}
	
	/**
	 * @param skill la comp�tence dont on veut r�cup�rer la valeur
	 * @return la modification de cette comp�tence
	 */
	
	public int getModification(Skill skill)
	{
		return myModifiedSkills.get(skill);
	}
}
