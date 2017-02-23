package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.effects.Effect;

/**
 * Représente un buff de compétence officiel, achetable et utilisable par les joueurs.
 * @author Wisteca
 */

public class OfficialSkillsBuff extends SkillsBuff implements OfficialPouvoir {
	
	private Personnage myLauncher;
	private String myDescription;
	private Item myItem;
	private int myLoadingTime, myCurrentTime = 0, myEnergyCoast;
	private HashMap<Skill, Integer> myRequirements;
	private Classe myRequiredClasse;
	
	/**
	 * Construire un buff officiel en précisant chaque attributs.
	 * @param launcher le personnage qui lancera le buff (celui sur lequel le buff s'appliquera sera la cible sélectionnée du launcher)
	 * @param totalTime le temps total en ticks pendant lequel le pouvoir aura effet une fois lancé
	 * @param size la taille du pouvoir dans la réserve du personnage
	 * @param effect l'effet de particules qui apparaîtra quand le pouvoir tournera ou null si il n'y en a pas
	 * @param modifications les modifications des compétences que provoque le pouvoir quand il tourne
	 * @param name le nom du pouvoir
	 * @param item l'item qui représente le pouvoir
	 * @param description la description du pouvoir
	 * @param loadinTime le temps de rechargement du pouvoir
	 * @param energyCoast le coût en énergie du pouvoir lorsqu'il est lancé
	 * @param requirements les compétences requises au launcher pour lancer le pouvoir
	 * @param requiredClasse la classe requise pour lancer le pouvoir ou null pour que tout le monde puisse
	 */
	
	public OfficialSkillsBuff(Personnage launcher, int totalTime, int size, Effect effect, HashMap<Skill, Integer> modifications,
			String name, Item item, String description, int loadingTime, int energyCoast, HashMap<Skill, Integer> requirements, Classe requiredClasse)
	{
		super(null, totalTime, size, effect, modifications, name);
		
		myDescription = description;
		myItem = item;
		myLoadingTime = loadingTime;
		myEnergyCoast = energyCoast;
		myRequirements = requirements;
		myRequiredClasse = requiredClasse;
	}
	
	/**
	 * Construire un buff officiel en le déserializant.
	 * @param element l'élément dans lequel le pouvoir à été sérializé auparavant
	 */
	
	public OfficialSkillsBuff(Element element)
	{
		super(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		super.serialize(toWrite);
		
		toWrite.setAttribute("description", myDescription);
		toWrite.setAttribute("loadingTime", Integer.toString(myLoadingTime));
		toWrite.setAttribute("energyCoast", Integer.toString(myEnergyCoast));
		toWrite.setAttribute("requiredClasse", myRequiredClasse.toString());
		
		Element item = toWrite.getOwnerDocument().createElement("item");
		toWrite.appendChild(item);
		myItem.serialize(item);
		
		Element requirements = toWrite.getOwnerDocument().createElement("requirements");
		toWrite.appendChild(requirements);
		for(Skill skill : myRequirements.keySet())
			requirements.setAttribute(skill.toString(), Integer.toString(myRequirements.get(skill)));
	}
	
	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		
		myDescription = element.getAttribute("description");
		myLoadingTime = Integer.valueOf(element.getAttribute("loadingTime"));
		myEnergyCoast = Integer.valueOf(element.getAttribute("energyCoast"));
		myRequiredClasse = Classe.valueOf(element.getAttribute("requiredClasse"));
		myItem = new Item((Element) element.getElementsByTagName("item").item(0));
		
		myRequirements = new HashMap<>();
		Element requirements = (Element) element.getElementsByTagName("requirements").item(0);
		for(Skill skill : Skill.values())
		{
			if(requirements.hasAttribute(skill.toString()) == false)
				continue;
			
			myRequirements.put(skill, Integer.valueOf(requirements.getAttribute(skill.toString())));
		}
	}
	
	@Override
	public boolean launch()
	{
		if(isRunning() == false && checkLaunch(myLauncher)) // on peut lancer sans problème, le pouvoir ne tourne pas et le launcher a l'autorisation de lancer
		{
			myTarget = myLauncher.getSelectedPersonnage() == null ? myLauncher : myLauncher.getSelectedPersonnage(); // définition du personnage touché par le pouvoir
			return super.launch();
		}
		
		return false;
	}
	
	@Override
	public void stop()
	{
		super.stop();
		myTarget = null;
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		if(myCurrentTime > 0)
			myCurrentTime--;
	}
	
	/**
	 * @return le lanceur du pouvoir, ne pas confondre avec getPersonnage() qui retourne la cible du lanceur !
	 */
	
	public Personnage getLauncher()
	{
		return myLauncher;
	}
	
	@Override
	public Item getRepresentativeItem()
	{
		return myItem;
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
