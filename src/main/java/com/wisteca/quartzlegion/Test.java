package com.wisteca.quartzlegion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Accessor;
import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Joueur;
import com.wisteca.quartzlegion.entities.personnages.PassivePersonnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.Personnage.Classe;
import com.wisteca.quartzlegion.entities.personnages.combats.Damage.DamageType;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Armor;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon;
import com.wisteca.quartzlegion.entities.personnages.combats.equipment.Weapon.WeaponType;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AttackPouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.OverTimePouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.SkillsBuff;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.SkillsBuffLauncher;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack.MegaDestroyer;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack.PistolShoot;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;
import com.wisteca.quartzlegion.entities.personnages.skills.Skill.HabilitySkill;
import com.wisteca.quartzlegion.utils.ItemType;
import com.wisteca.quartzlegion.utils.effects.Effect;
import com.wisteca.quartzlegion.utils.effects.SphereEffect;

/**
 * Classe faite n'importe comment pour divers tests, peut Ítre modifiÈ ‡ tous moment, rien d'important se trouve dedans.
 * @author Wisteca
 */

public class Test implements CommandExecutor {
	
	private SkillsBuffLauncher myLauncher;
	private SphereEffect myEffect = (SphereEffect) Effect.getEffectByName("Gris scintillant");
	
	private Location myL1, myL2;

	public Test()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.getInstance(), new Runnable() {
			
			@Override
			public void run()
			{
				myEffect.doTime();
			}
			
		}, 0, 1);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if((sender instanceof Player && args.length > 0) == false)
		{
			sender.sendMessage("NOP !");
			return false;
		}
		
		if(sender.isOp() == false)
			return true;
		
		Player p = (Player) sender;
		Joueur perso = (Joueur) PersonnageManager.getInstance().getPersonnage(p.getUniqueId());
		
		switch(args[0])
		{		
			case "velo" :
				Random rand = new Random();
				perso.setVelocity(perso.getLocation().clone().add(15 - rand.nextInt(30), 1, 15 - rand.nextInt(30)).toVector().subtract(perso.getLocation().toVector()).normalize().multiply(10));
				break;
			
			case "inArea" :
				
				if(args[1].equals("1"))
				{
					myL1 = perso.getLocation();
				}
				
				if(args[1].equals("2"))
				{
					myL2 = perso.getLocation();
				}
				
				if(args[1].equals("test"))
				{
					Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.getInstance(), new Runnable() {
						
						@Override
						public void run()
						{
							Location loc = perso.getLocation();
							if(isInside(loc, myL1, myL2))
							{
								perso.sendMessage("cest juste !!");
								return;
							}
							
							perso.sendMessage("NOPla");
						}
						
					}, 0, 1);
				}
				
				break;
			
			case "particle" :
				
				for(int i = 0 ; i < 10 ; i++)
				{
					Location loc = perso.getLocation().getDirection().normalize().multiply(i).toLocation(perso.getWorld()).add(perso.getLocation());
					perso.spawnParticle(Particle.HEART, loc, 1);
				}
				
				
				break;
			
			case "effect" :
				
				myEffect.launch(perso.getLocation().add(0, 1, 0));
				
				break;
			
			case "dot" :
				
				OverTimePouvoir op = OverTimePouvoir.getPouvoirByName("POUVOIR DE TEST", perso);
				op.launch();
				
				break;
				
			case "buff" :
				
				SkillsBuff buff = new SkillsBuff(perso, args[1].replace('_', ' '));
				buff.launch();
				
				break;
			
			case "save" :
				
				perso.saveProgress();
			
			break;
			
			case "addAttack" :
				
				perso.setPouvoir(1, MegaDestroyer.class);
				perso.setPouvoir(2, PistolShoot.class);
			
			break;
			
			case "setHealthScale" :
			
				p.setHealthScale(Double.valueOf(args[1]));
			
			break;
			
			case "setHealthScaled" :
				
				p.setHealthScaled(Boolean.valueOf(args[1]));
			
			break;
			
			case "getHealthScale" :
				
				p.sendMessage(p.getHealthScale() + "");
			
			break;
			
			case "isHealthScaled" :
				
				p.sendMessage(p.isHealthScaled() + "");
			
			break;
			
			case "changeHealth" :
				
				perso.changeHealth(Integer.valueOf(args[1]));
				perso.sendMessage("vie : " + perso.getHealth());
			
			break;
			
			case "giveExp" :
				
				perso.giveExp(Integer.valueOf(args[1]));
				
				break;
			
			case "giveExpLevels" :
				
				perso.giveExpLevels(Integer.valueOf(args[1]));
				
				break;
				
			case "getArme" :
				
				Weapon ww;
				HashMap<DamageType, int[]> damages = new HashMap<>();
				damages.put(DamageType.BRULURE, new int[]{50, 100});
				perso.setWeapon(0, ww = new Weapon(ItemType.IRON_PICKAXE, WeaponType.FEU, Classe.AUCUN, null, damages, null, 3, 10, 15, "ARME DU SIËCLE", "salut"));
				perso.getInventory().addItem(ww.toItemStack());
				
			break;
			
			case "bdd" :
				
				Document document = null;
				
				try {
					
					document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					document.appendChild(document.createElement("personnage"));
					perso.serialize(document.getDocumentElement());
				
					Accessor access = Accessor.createNewAccessor();
					System.out.println(access.isCreated(perso.getUniqueId()));
					access.create(perso.getUniqueId(), document.getDocumentElement());
					System.out.println(access.isCreated(perso.getUniqueId()));
					
					perso.setLevel(85);
					perso.changeSkillFix(ClasseSkill.ENERGIE_TOTALE, 85);
					perso.changeEnergy(54);
					perso.serialize(document.getDocumentElement());
					access.setXML(perso.getUniqueId(), document.getDocumentElement());
					access.getXML(perso.getUniqueId());
				
				} catch(ParserConfigurationException ex) {
					ex.printStackTrace();
				}
				
				break;
			
			case "particles" :
				
				if(args.length != 8)
				{
					p.sendMessage("test particles particle extension increase counts layers particlesPerLayer betweenTime");
					return true;
				}
					
				SphereEffect e = new SphereEffect("LETEST", new ArrayList<>(Arrays.asList(Particle.valueOf(args[1]))), Double.valueOf(args[2]), Double.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), Integer.valueOf(args[7]));
				e.launch(p.getLocation().add(0, 1, 0));
				
				Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.getInstance(), new Runnable() {
					
					@Override
					public void run()
					{
						e.doTime();
					}
					
				}, 0, 1);
				
				break;
			
			
			
			case "serializePersonnage" :
				
				try {
				
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element pouvoir = doc.createElement("test");
					doc.appendChild(pouvoir);
					perso.serialize(pouvoir);
					
					final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			        final DOMSource source = new DOMSource(doc);
			        final StreamResult sortie = new StreamResult(new File(Constants.getServerFolderPath() + "/test.xml"));
			        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");         
			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			        transformer.transform(source, sortie);
				
				} catch(Exception ex) {
					ex.printStackTrace();
				}
		        
				break;
			
			case "DOT" :
				
				OverTimePouvoir dot = OverTimePouvoir.getPouvoirByName("POUVOIR DE TEST", perso);
				dot.launch();
				perso.sendMessage("lancÈ !");
				
				Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.getInstance(), new Runnable() {
					
					@Override
					public void run()
					{
						perso.doTime();
					}
					
				}, 0, 1);
				
				break;
			
			case "attack" :
				
				AttackPouvoir.getPouvoirByName("AttackTest1", perso).launch();
				
				break;
			
			case "launchBuff" :
				
				if(myLauncher == null)
				{
					p.sendMessage("OLALALA");
					return true;
				}
				
				myLauncher.launch();
				
				Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.getInstance(), new Runnable() {
					
					@Override
					public void run()
					{
						perso.doTime();
						myLauncher.doTime();
					}
					
				}, 0, 1);
				
				break;
			
			case "SkillsBuff" :
				
				try {
					
					for(PassivePersonnage pp : PersonnageManager.getInstance().getAllPersonnages().values())
						Bukkit.broadcastMessage(pp.getName());
					
					myLauncher = new SkillsBuffLauncher("Iron_Speed", (Personnage) PersonnageManager.getInstance().getPersonnage(p.getUniqueId()));
					Document doc1 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element pouvoir1 = doc1.createElement("test");
					doc1.appendChild(pouvoir1);
					myLauncher.serialize(pouvoir1);
					
					final Transformer transformer1 = TransformerFactory.newInstance().newTransformer();
			        final DOMSource source1 = new DOMSource(doc1);
			        final StreamResult sortie1 = new StreamResult(new File(Constants.getServerFolderPath() + "/test.xml"));
			        transformer1.setOutputProperty(OutputKeys.VERSION, "1.0");
			        transformer1.setOutputProperty(OutputKeys.ENCODING, "UTF-8");         
			        transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer1.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			        transformer1.transform(source1, sortie1);
				
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
				perso.sendMessage(skill + " a ÈtÈ changÈ ‡ : " + perso.getSkillFix(skill));
				
				break;
			
			case "changeEnergy" :
				
				perso.changeEnergy(Integer.valueOf(args[1]));
				perso.sendMessage("Ènergie : " + perso.getEnergy());
				
				break;
			
			case "weapon":
				
				Weapon w = new Weapon(ItemType.DIAMOND_SWORD, WeaponType.LAME, Classe.ASSASSIN, null, null, null, 5, 8, 0, "The GOOD",
						"Arme forgÈe avec un rateau. Oui je sais c'est dÈbile !");
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
						"Cette armure est cassÈe et adjkfbadkj adfhkabd  adbaskdbj dkahsbdask khashdbask");
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
	
	public boolean isInside(Location inside, Location point1, Location point2) 
	{
		double x1 = Math.min(point1.getX(), point2.getX());
		double y1 = Math.min(point1.getY(), point2.getY());
		double z1 = Math.min(point1.getZ(), point2.getZ());
		double x2 = Math.max(point1.getX(), point2.getX());
		double y2 = Math.max(point1.getY(), point2.getY());
		double z2 = Math.max(point1.getZ(), point2.getZ());
 
        return inside.getX() >= x1 && inside.getX() <= x2 && inside.getY() >= y1 && inside.getY() <= y2 && inside.getZ() >= z1 && inside.getZ() <= z2;
	}
}
