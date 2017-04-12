package com.wisteca.quartzlegion.commands.megacopier;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MegaCopierCommand implements CommandExecutor {

	private HashMap<UUID, HashMap<Integer, Copier>> myCopiers = new HashMap<>();
	 
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender.isOp() == false || (sender instanceof Player) == false)
			return false;
		
		Player user = (Player) sender;
		
		if(args.length < 1)
			user.sendMessage("§cIl manque un argument ! /megacopier aide pour plus d'informations !");
		
		/**
		 * *** UTILISATION ***
		 * new / nouveau index --> créer une nouvelle instance de Copier à l'index donné
		 * set / mettre index first / premier | second / deuxième --> placer le point 1 ou 2 du Copier donné
		 * list / liste --> voir la liste des Copier et leur avancement
		 * start / commencer index --> démarrer le Copier se trouvant à l'index donné
		 * aide / help --> aide
		 */
		
		if(args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("nouveau"))
		{
			if(args.length != 2)
			{
				user.sendMessage("§cSyntaxe : /megacopier nouveau §lindex");
				return false;
			}
			
			HashMap<Integer, Copier> copiers;
			
			if(myCopiers.containsKey(user.getUniqueId()))
				copiers = myCopiers.get(user.getUniqueId());
			else
				copiers = new HashMap<>();
			
			int index = getInt(args[1], user);
			
			if(index == -1)
				return false;
			
			if(copiers.containsKey(index) && copiers.get(index).getRemainingTime() > 0)
			{
				user.sendMessage("§cUn copieur possède déjà cet index !");
				return true;
			}
							
			copiers.put(index, new Copier());
			myCopiers.put(user.getUniqueId(), copiers);
			user.sendMessage("§2Le Copieur a été ajouté à votre liste sous l'index " + index + ".");
			
		}
		else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("mettre"))
		{
			if(args.length != 3)
			{
				user.sendMessage("§cSyntaxe : /megacopier mettre index premier | second");
				return false;
			}
			
			int index = getInt(args[1], user);
			
			if(index == -1)
				return false;
			
			if(args[2].equalsIgnoreCase("premier") || args[2].equalsIgnoreCase("first"))
			{
				myCopiers.get(user.getUniqueId()).get(index).setFirstPoint(user.getLocation());
				return true;
			}
			else if(args[2].equalsIgnoreCase("second") || args[2].equalsIgnoreCase("deuxième"))
			{
				myCopiers.get(user.getUniqueId()).get(index).setSecondPoint(user.getLocation());
				return true;
			}
			else
			{
				user.sendMessage("§cSyntaxe : /megacopier mettre index premier | second");
				return false;
			}
		}
		else if(args[0].equalsIgnoreCase("commencer") || args[0].equalsIgnoreCase("start"))
		{
			if(args.length != 2)
			{
				user.sendMessage("§cSyntaxe : /megacopier commencer | start index");
				return false;
			}
			
			int index = getInt(args[1], user);
			
			if(index == -1)
				return false;
			
			if(myCopiers.get(user.getUniqueId()).get(index).start())
				user.sendMessage("§2Le copieur a commencé son travail ...");
			else
				user.sendMessage("§cCe copieur est déjà en train de travailler ou vous n'avez pas terminé de le configurer !");
		}
		else if(args[0].equalsIgnoreCase("liste") || args[0].equalsIgnoreCase("liste"))
		{
			user.sendMessage("");
			for(int i = 0 ; ; i++)
			{
				Copier copier = myCopiers.get(user.getUniqueId()).get(i);
				if(copier == null)
					break;
				
				user.sendMessage("§2Copieur Numéro " + i + " :");
				user.sendMessage("    §c1er position  : " + locationToString(copier.getFirstPoint()));
				user.sendMessage("    §c2eme position : " + locationToString(copier.getSecondPoint()));
				user.sendMessage("§4Temps restant : " + copier.getRemainingTime());
				user.sendMessage("");
			}
		}
		else if(args[0].equalsIgnoreCase("aide") || args[0].equalsIgnoreCase("help"))
		{
			user.sendMessage("§e---- Aide ----");
			user.sendMessage("§5/megacopier new | nouveau index --> Créer un copieur et lui attribuer l'index. Exemple : / megacopier new 1");
			user.sendMessage("§5/megacopier set index | mettre first | premier || second | deuxième --> Indiquer au copieur choisit en fonction de l'index qu'elle est la première position et la deuxième. Exemple : /megacopier set 1 premier");
			user.sendMessage("§5/megacopier list | liste --> Afficher le liste des copieurs et leur avancement.");
			user.sendMessage("§5/megacopier commencer | start index --> Une fois les deux positions indiquées, permet de lancer le travail. Exemple : /megacopier start 1");
			user.sendMessage("§e          ~");
		}
		
		
		return true;
	}
	
	private String locationToString(Location loc)
	{
		return "X : " + loc.getBlockX() + "  Y : " + loc.getBlockY() + "  Z : " + loc.getBlockZ();
	}
	
	private int getInt(String str, Player user)
	{
		int index = 0;
		
		try {
			
			index = Integer.valueOf(str);
			
			if(index < 0)
			{
				user.sendMessage("§cMerci d'entrer un chiffre correct !");
				return -1;
			}
			
		} catch(NumberFormatException ex) {
			user.sendMessage("§cMerci d'entrer un chiffre correct !");
			return -1;
		}
		
		return index;
	}
}


















