package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import javax.xml.soap.Node;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.effects.Effect;
import com.wisteca.quartzlegion.utils.effects.EffectInterface.Part;

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
	
	public SkillsBuff(Personnage target, String name, int totalTime, int size, HashMap<Skill, Integer> modifications, Effect effect, Part part, int betweenTime)
	{
		super(target, totalTime, size, name, effect, part, betweenTime);
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
	
	/**
	 * Consrtuire un buff en fonction de son nom, il se désérialisera en prenant les attributs spécifiés dans le fichier pouvoirs.xml.
	 * @param target la cible du pouvoir
	 * @param name le nom du pouvoir tel qu'il est dans le fichier pouvoir.xml
	 */
	
	public SkillsBuff(Personnage target, String name)
	{
		super(target, name);
		initMap();
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		initMap();
	}
	
	private void initMap()
	{
		Element skills = null;
		NodeList buffs = Constants.POUVOIRS_DOCUMENT.getDocumentElement().getElementsByTagName("buffs").item(0).getChildNodes();
		for(int i = 0 ; i < buffs.getLength() ; i++)
			if(buffs.item(i).getNodeType() == Node.ELEMENT_NODE && ((Element) buffs.item(i)).getAttribute("name").equals(getName()))
				skills = (Element) ((Element) buffs.item(i)).getElementsByTagName("skills").item(0);
		
		myModifiedSkills = new HashMap<>();
		for(Skill skill : Skill.values())
		{
			int modification;
			if(skills.hasAttribute(skill.toString()) == false)
			
				modification = 0;
			else
				modification = Integer.valueOf(skills.getAttribute(skill.toString()));
			
			myModifiedSkills.put(skill, modification);
		}
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
