package com.wisteca.quartzlegion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Armor;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.HabilitySkill;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.effects.SphereEffect;

public class Test implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if((sender instanceof Player && args.length > 0) == false)
		{
			sender.sendMessage("NOP !");
			return false;
		}
		
		Player p = (Player) sender;
		
		switch(args[0])
		{
			case "particle":
				new SphereEffect().launch(p.getLocation().add(0, 1, 0));
				break;
			
			case "weapon":
				
				Weapon w = new Weapon(ItemType.DIAMOND_SWORD, WeaponType.LAME, Classe.ASSASSIN, null, 5, 8, "The GOOD",
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
				
				Armor a = new Armor(ItemType.DIAMOND_CHESTPLATE, Classe.BARBARE, null, "L'ARMURE DU CIECLE",
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
