package com.wisteca.quartzlegion;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.PassivePersonnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Armor;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.SkillsBuffLauncher;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.HabilitySkill;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.effects.SphereEffect;

/**
 * Classe faite n'importe comment pour divers tests, peut être modifié à tous moment, rien d'important se trouve dedans.
 * @author Wisteca
 */

public class Test implements CommandExecutor {
	
	private SkillsBuffLauncher myLauncher;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if((sender instanceof Player && args.length > 0) == false)
		{
			sender.sendMessage("NOP !");
			return false;
		}
		
		Player p = (Player) sender;
		Personnage perso = (Personnage) PersonnageManager.getInstance().getPersonnage(p.getUniqueId());
		
		switch(args[0])
		{
			case "launch" :
				
				if(myLauncher == null)
				{
					p.sendMessage("OLALALA");
					return true;
				}
				
				p.sendMessage(myLauncher.getLauncher().getName());
				myLauncher.launch();
				
				break;
			
			case "SkillsBuff" :
				
				try {
					
					for(PassivePersonnage pp : PersonnageManager.getInstance().getPersonnages().values())
						Bukkit.broadcastMessage(pp.getName());
					
					myLauncher = new SkillsBuffLauncher("Iron_Speed", (Personnage) PersonnageManager.getInstance().getPersonnage(p.getUniqueId()));
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element pouvoir = doc.createElement("test");
					doc.appendChild(pouvoir);
					myLauncher.serialize(pouvoir);
					
					final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			        final DOMSource source = new DOMSource(doc);
			        final StreamResult sortie = new StreamResult(new File(Constants.getServerFolderPath() + "/test.xml"));
			        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");         
			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			        transformer.transform(source, sortie);
				
				} catch(ParserConfigurationException | TransformerException ex) {
					ex.printStackTrace();
				}
				
				break;
			
			case "seeSkills" :	
				
				for(Skill skill : Skill.values())
				{
					p.sendMessage(skill.getCompleteName() + " : " + perso.getTemporarySkill(skill));
				}
				
				break;
				
			case "changeSkill" :
				
				if(args.length != 3)
				{
					p.sendMessage("changeSkill skill value");
					return true;
				}
				
				perso.setLevel(100);
				Skill skill = Skill.valueOf(args[1]);
				perso.changeSkillFix(skill, Integer.valueOf(args[2]));
				perso.sendMessage(skill + " a été changé à : " + perso.getSkillFix(skill));
				
				break;
			
			case "changeEnergie" :
				
				perso.changeEnergy(Integer.valueOf(args[1]));
				
				break;
				
			case "particle":
				new SphereEffect().launch(p.getLocation().add(0, 1, 0));
				break;
			
			case "weapon":
				
				Weapon w = new Weapon(ItemType.DIAMOND_SWORD, WeaponType.LAME, Classe.ASSASSIN, null, null, null, 5, 8, 0, "The GOOD",
						"Arme forgée avec un rateau. Oui je sais c'est débile !");
				w.setShiny(true);
				w.setFlags(true);
				Bukkit.broadcastMessage(args[1].contains("true") + "");
				w.setPermanent(args[1].contains("true"));
				w.addRequirement(ClasseSkill.ARME_LONGUE, 45);
				w.addRequirement(HabilitySkill.FORCE, 15);
				w.addRequirement(HabilitySkill.ENDURANCE, 15);
				w.addRequirement(HabilitySkill.INTELLIGENCE, 15);
				w.addRequirement(HabilitySkill.PRECISION, 15);
				w.addRequirement(HabilitySkill.SANTE, 15);
				w.addRequirement(ClasseSkill.CORDE, 15);
				w.addRequirement(ClasseSkill.REGEN_ENERGIE, 15);
				w.addRequirement(ClasseSkill.SORTS_PROTECTION, 15);
				w.addIncrease(HabilitySkill.AGILITE, 56);
				w.addIncrease(HabilitySkill.PRECISION, 56);
				w.addIncrease(HabilitySkill.FORCE, 56);
				w.addIncrease(HabilitySkill.ENDURANCE, 56);
				w.addIncrease(HabilitySkill.SANTE, 56);
				p.getInventory().addItem(w.toItemStack());
				
				break;
			case "armor":
				
				Armor a = new Armor(ItemType.DIAMOND_CHESTPLATE, Classe.BARBARE, null, null, null, 0, "L'ARMURE DU CIECLE",
						"Cette armure est cassée et adjkfbadkj adfhkabd  adbaskdbj dkahsbdask khashdbask");
				a.setFlags(true);
				a.addRequirement(ClasseSkill.ARME_LONGUE, 45);
				a.addRequirement(HabilitySkill.FORCE, 15);
				a.addRequirement(HabilitySkill.ENDURANCE, 15);
				a.addRequirement(HabilitySkill.INTELLIGENCE, 15);
				a.addRequirement(HabilitySkill.PRECISION, 15);
				a.addRequirement(HabilitySkill.SANTE, 15);
				a.addRequirement(ClasseSkill.CORDE, 15);
				a.addRequirement(ClasseSkill.REGEN_ENERGIE, 15);
				a.addRequirement(ClasseSkill.SORTS_PROTECTION, 15);
				p.getInventory().addItem(a.toItemStack());
				
				break;
		}
		
		return true;
	}
}
