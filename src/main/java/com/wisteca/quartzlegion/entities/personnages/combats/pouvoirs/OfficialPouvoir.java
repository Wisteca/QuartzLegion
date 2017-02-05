package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;

public interface OfficialPouvoir extends Pouvoir {
	
	public Item getRepresentativeItem();
	
	public String getName();
	
	public String getDescription();
	
	public int getLoadingTime();
	
	public int getEnergyCost();
	
	public HashMap<Skill, Integer> getRequirements();
	
	public Classe getRequiredClasse();
	
}
