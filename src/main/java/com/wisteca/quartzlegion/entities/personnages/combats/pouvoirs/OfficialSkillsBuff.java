package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;

public class OfficialSkillsBuff extends SkillsBuff implements OfficialPouvoir {

	public OfficialSkillsBuff(Personnage perso, int duration)
	{
		super(perso, duration, 0, null, null, "");
	}

	@Override
	public Item getRepresentativeItem()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoadingTime()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEnergyCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HashMap<Skill, Integer> getRequirements()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Classe getRequiredClasse()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
