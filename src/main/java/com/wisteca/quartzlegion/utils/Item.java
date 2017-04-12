package com.wisteca.quartzlegion.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Serializer;

/**
 * Classe utile pour gérer plus facilement les items, elle a comme attribut un ItemType au lieu d'un Material, pour gérer plus facilement les items custom à durabilité.
 * @author Wisteca
 */

public class Item implements Serializer {

	private ItemStack myItem;
	private ItemType myType;
	private boolean myIsPermanent;
	
	/**
	 * Créer un item avec les attributs de base et en spécifiant juste un ItemType
	 * @param type le type de l'item
	 * @see ItemType
	 */
	
	public Item(ItemType type)
	{
		myItem = new ItemStack(type.getMaterial(), 1, type.getDurability());
		myType = type;
		myIsPermanent = false;
	}
	
	/**
	 * Créer un item en spécifiant chaque attributs.
	 * @param type le type de l'item
	 * @param amount le nombre d'item
	 * @param permanent si l'item peut être jeté ou non de l'inventaire du joueur
	 * @param name le nom de l'item
	 * @param lore la description de l'item
	 * @param hasFlags si l'item a les flags tel que cacher la durabilité, les enchantements etc...
	 * @param isShiny si l'item a un effet brillant (est enchanté)
	 * @see ItemType
	 */
	
	public Item(ItemType type, int amount, boolean permanent, String name, List<String> lore, boolean hasFlags, boolean isShiny)
	{
		myItem = new ItemStack(type.getMaterial(), amount, type.getDurability());
		myType = type;
		myIsPermanent = permanent;
		setName(name);
		setLore(lore);
		setFlags(hasFlags);
		setShiny(isShiny);
	}
	
	/**
	 * Permet de construire un item en le déserializant d'une chaîne XML.
	 * @param element l'élément dans lequel l'item a été sérializé auparavant
	 */
	
	public Item(Element element)
	{
		deserialize(element);
	}
	
	/**
	 * @param amount le nombre d'items
	 */
	
	public void setAmount(int amount)
	{
		myItem.setAmount(amount);
	}
	
	/**
	 * @return le nombre d'items
	 */
	
	public int getAmount()
	{
		return myItem.getAmount();
	}
	
	/**
	 * @param type le nouveau type de l'item
	 */
	
	public void setType(ItemType type)
	{
		myType = type;
		myItem.setType(type.getMaterial());
		myItem.setDurability(type.getDurability());
	}
	
	/**
	 * @return le type de l'item
	 */
	
	public ItemType getType()
	{
		return myType;
	}
	
	/**
	 * Permet de facilement modifier le nom de l'item sans passer par l'ItemMeta...
	 * @param name le nouveau nom de l'item
	 */
	
	public void setName(String name)
	{
		ItemMeta meta = myItem.getItemMeta();
		meta.setDisplayName(name);
		myItem.setItemMeta(meta);
	}
	
	/**
	 * @return le nom de l'item
	 */
	
	public String getName()
	{
		return myItem.getItemMeta().getDisplayName();
	}
	
	/**
	 * Mettre une description à l'item
	 * @param lore un tableau de String contenant la description lignes par lignes
	 */
	
	public void setLore(String... lore)
	{
		List<String> loreList;
		
		if(lore != null)
			loreList = new ArrayList<>(Arrays.asList(lore));
		else
			loreList = new ArrayList<>();

		if(myIsPermanent && loreList.contains("§c§oNon-jetable") == false)
			loreList.add("§c§oNon-jetable");
		else if(myIsPermanent == false && loreList.contains("§c§oNon-jetable"))
			loreList.remove("§c§oNon-jetable");
		
		ItemMeta meta = myItem.getItemMeta();
		meta.setLore(loreList);
		myItem.setItemMeta(meta);
	}
	
	/**
	 * Mettre une description à l'item
	 * @param lore une collection de String à mettre en description
	 */
	
	public void setLore(List<String> lore)
	{
		if(lore == null)
			lore = new ArrayList<>();
		
		if(myIsPermanent && lore.contains("§c§oNon-jetable") == false)
			lore.add("§c§oNon-jetable");
		else if(myIsPermanent == false && lore.contains("§c§oNon-jetable"))
			lore.remove("§c§oNon-jetable");
		
		ItemMeta meta = myItem.getItemMeta();
		meta.setLore(new ArrayList<>(lore));
		myItem.setItemMeta(meta);
	}
	
	/**
	 * Récupérer la description
	 * @return la description
	 */
	
	public List<String> getLore()
	{
		return myItem.getItemMeta().getLore();
	}
	
	/**
	 * @param shiny true si l'item est brillant (enchanté)
	 */
	
	public void setShiny(boolean shiny)
	{
		try {
			
			if(shiny)
				myItem.addEnchantment(Enchantment.DURABILITY, 1);
			else
				myItem.removeEnchantment(Enchantment.DURABILITY);
			
		} catch(IllegalArgumentException ex) {}
	}
	
	/**
	 * @return true si l'item est brillant (enchanté)
	 */
	
	public boolean isShiny()
	{
		return myItem.containsEnchantment(Enchantment.DURABILITY);
	}
	
	/**
	 * Mettre ou non tous les flags telle que cacher la durabilité, les enchantements, etc...
	 * @param flags true pour mettre les flags
	 */
	
	public void setFlags(boolean flags)
	{
		ItemMeta meta = myItem.getItemMeta();
		
		if(flags)
		{
			for(ItemFlag flag : ItemFlag.values())
				meta.addItemFlags(flag);
		}
		else
		{
			for(ItemFlag flag : ItemFlag.values())
				meta.removeItemFlags(flag);
		}
		
		myItem.setItemMeta(meta);
	}
	
	/**
	 * @return true si l'item a les flags
	 */
	
	public boolean hasFlags()
	{
		boolean hasFlags = true;
		
		for(ItemFlag flag : ItemFlag.values())
		{
			if(myItem.getItemMeta().hasItemFlag(flag) == false)
			{
				hasFlags = false;
				break;
			}
		}
		
		return hasFlags;
	}
	
	/**
	 * @param permanent true pour empêcher un joueur de lancer l'item
	 */
	
	public void setPermanent(boolean permanent)
	{
		myIsPermanent = permanent;
		setLore();
	}
	
	/**
	 * @return true si l'item est permanent, donc que les joueurs ne peuvent pas le lancer
	 */
	
	public boolean isPermanent()
	{
		return myIsPermanent;
	}
	
	/**
	 * @return une copie de l'item sous forme d'ItemStack
	 */
	
	public ItemStack toItemStack()
	{
		return new ItemStack(myItem);
	}
	
	@Override
	public void serialize(Element toWrite)
	{
		Utils.removeElementIfExist(toWrite, "item");
		Element item = toWrite.getOwnerDocument().createElement("item");
		toWrite.appendChild(item);
		
		item.setAttribute("type", myType.toString());
		item.setAttribute("permanent", Boolean.toString(myIsPermanent));
		item.setAttribute("shiny", Boolean.toString(isShiny()));
		item.setAttribute("amount", Integer.toString(getAmount()));
		item.setAttribute("name", getName());
		item.setAttribute("flags", Boolean.toString(hasFlags()));
		
		if(getLore() != null)
		{
			StringBuilder lore = new StringBuilder();
			for(String str : getLore())
				lore.append(str + "/n");
			
			item.setAttribute("lore", lore.toString());
		}
	}

	@Override
	public void deserialize(Element element)
	{
		System.out.println("deserialisation item");
		Element item = (Element) element.getElementsByTagName("item").item(0);
		myType = ItemType.valueOf(item.getAttribute("type"));
		myItem = new ItemStack(myType.getMaterial(), Integer.valueOf(item.getAttribute("amount")), myType.getDurability());
		
		setPermanent(Boolean.valueOf(item.getAttribute("permanent")));
		setShiny(Boolean.valueOf(item.getAttribute("shiny")));
		setName(item.getAttribute("name"));
		setFlags(Boolean.valueOf(item.getAttribute("flags")));
		
		if(item.hasAttribute("lore"))
			setLore(item.getAttribute("lore").split("/n"));
	}
}
