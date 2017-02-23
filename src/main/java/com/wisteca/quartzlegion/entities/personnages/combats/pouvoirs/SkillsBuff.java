package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.effects.Effect;

/**
 * Représente un buff des compétences du personnage durant un certain temps.
 * @author Wisteca
 */

public class SkillsBuff extends SpacePouvoir {
	
	private HashMap<Skill, Integer> myModifiedSkills;
	
	/**
	 * Construire un buff en précisant chaque attributs.
	 * @param target le personnage qui bénéficiera du buff
	 * @param totalTime le temps total en ticks pendant lequel le pouvoir aura effet
	 * @param size la taille du pouvoir dans la réserve du personnage
	 * @param effect l'effet de particules qui apparaîtra quand le pouvoir tourne, ou null pour aucun effet
	 * @param modifications les compétences modifiées en bien ou en mal
	 * @param name le nom du pouvoir
	 */
	
	public SkillsBuff(Personnage target, int totalTime, int size, Effect effect, HashMap<Skill, Integer> modifications, String name)
	{
		super(target, totalTime, size, name, effect);
		myModifiedSkills = new HashMap<>(modifications);
	}
	
	/**
	 * Construire un buff en le déserializant
	 * @param element l'élément dans lequel le pouvoir à été sérializé auparavant
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
	 * Change la cible du pouvoir SI et seulement SI le pouvoir ne tourne pas déjà sur un autre personnage.
	 * @param target la nouvelle cible du pouvoir.
	 */
	
	public void setTarget(Personnage target)
	{
		if(isRunning() == false)
			myTarget = target;
	}
	
	/**
	 * @return une map des compétences modifiées par ce pouvoir
	 */
	
	public HashMap<Skill, Integer> getModifiedSkills()
	{
		return new HashMap<>(myModifiedSkills);
	}
	
	/**
	 * @param skill la compétence dont on veut récupérer la valeur
	 * @return la modification de cette compétence
	 */
	
	public int getModification(Skill skill)
	{
		return myModifiedSkills.get(skill);
	}
}
