package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Bukkit;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;

public class SkillsBuffLauncher implements OfficialPouvoir {
	
	private Personnage myLauncher;
	private SkillsBuff myCurrentPouvoir;
	
	private String myDescription;
	private Item myItem;
	private int myLoadingTime, myCurrentTime = 0, myEnergyCoast;
	private HashMap<Skill, Integer> myRequirements;
	private Classe myRequiredClasse;
	
	public SkillsBuffLauncher(String pouvoirName, Personnage launcher)
	{
		myLauncher = launcher;
		setPouvoir(pouvoirName);
	}
	
	public Personnage getLauncher()
	{
		return myLauncher;
	}
	
	public void setPouvoir(String pouvoirName)
	{
		Bukkit.broadcastMessage(Constants.POUVOIRS_DOCUMENT.toString());
		Element state = (Element) Constants.POUVOIRS_DOCUMENT.getElementsByTagName(pouvoirName.replace(' ', '_')).item(0);
		
		myDescription = state.getAttribute("description");
		myLoadingTime = Integer.valueOf(state.getAttribute("loadingTime"));
		myEnergyCoast = Integer.valueOf(state.getAttribute("energyCoast"));
		myRequiredClasse = Classe.valueOf(state.getAttribute("requiredClasse"));
		myItem = new Item(state);
		
		myRequirements = new HashMap<>();
		Element requirements = (Element) state.getElementsByTagName("requirements").item(0);
		for(Skill skill : Skill.values())
		{
			if(requirements.hasAttribute(skill.toString()) == false)
				continue;
			
			myRequirements.put(skill, Integer.valueOf(requirements.getAttribute(skill.toString())));
		}
		
		Element stateCopy = (Element) state.cloneNode(true);
		stateCopy.setAttribute("remainingTime", "0");
		
		myCurrentPouvoir = new SkillsBuff(stateCopy);
	}
	
	public String getPouvoir()
	{
		return myCurrentPouvoir.getName();
	}
	
	public SkillsBuff getCurrentPouvoir()
	{
		return myCurrentPouvoir;
	}
	
	@Override
	public boolean launch()
	{
		if(checkLaunch(myLauncher))
		{
			myCurrentPouvoir.setTarget(myLauncher.getSelectedPersonnage() == null ? myLauncher : myLauncher.getSelectedPersonnage());
			myCurrentTime = getLoadingTime();
			myLauncher.changeEnergy(-getEnergyCost());
			setPouvoir(getPouvoir());
			
			return true;
		}
		
		return false;
	}

	@Override
	public void doTime()
	{
		if(myCurrentTime > 0)
			myCurrentTime--;
	}

	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		myCurrentPouvoir.serialize(toWrite);
	}

	@Override
	public void deserialize(Element element)
	{
		myCurrentPouvoir.deserialize(element);
	}

	@Override
	public Item getRepresentativeItem()
	{
		return myItem;
	}

	@Override
	public String getName()
	{
		return myCurrentPouvoir.getName();
	}

	@Override
	public String getDescription()
	{
		return myDescription;
	}

	@Override
	public int getLoadingTime()
	{
		return myLoadingTime;
	}

	@Override
	public int getEnergyCost()
	{
		return myEnergyCoast;
	}

	@Override
	public HashMap<Skill, Integer> getRequirements()
	{
		return myRequirements;
	}

	@Override
	public Classe getRequiredClasse()
	{
		return myRequiredClasse;
	}

	@Override
	public int getRemainingLoadingTime()
	{
		return myCurrentTime;
	}
}
