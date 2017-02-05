package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.effects.Effect;

public class SkillsBuff extends SpacePouvoir {
	
	private HashMap<Skill, Integer> myModifiedSkills = new HashMap<>();
	private Effect myEffect;
	
	public SkillsBuff(Personnage perso, int totalTime, int size, Effect effect, HashMap<Skill, Integer> modifications, String name)
	{
		super(perso, totalTime, size, name);
		myModifiedSkills = new HashMap<>(modifications);
		myEffect = effect;
	}
	
	public SkillsBuff(Element element)
	{
		super(element);
	}
	
	public HashMap<Skill, Integer> getModifiedSkills()
	{
		return new HashMap<>(myModifiedSkills);
	}
	
	public int getModification(Skill skill)
	{
		return myModifiedSkills.get(skill);
	}
	
	public Effect getEffect()
	{
		return myEffect;
	}

	@Override
	public void doTime()
	{
		super.doTime();
		
		if(myEffect != null)
			myEffect.doTime();
	}

	@Override
	protected void end()
	{}
}
