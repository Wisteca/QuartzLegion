package com.wisteca.quartzlegion.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Serializer;

public class Item implements Serializer {
	
	/*
	 * Classe utile pour gérer les items.
	 */

	private ItemStack myItem;
	private ItemType myType;
	private boolean myIsPermanent;
	
	public Item(ItemType type)
	{
		myItem = new ItemStack(type.getMaterial(), 1, type.getDurability());
		myType = type;
		myIsPermanent = false;
	}
	
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
	
	public Item(Element element)
	{
		deserialize(element);
	}
	
	public void setAmount(int amount)
	{
		myItem.setAmount(amount);
	}
	
	public int getAmount()
	{
		return myItem.getAmount();
	}
	
	public void setType(ItemType type)
	{
		myType = type;
		myItem.setType(type.getMaterial());
		myItem.setDurability(type.getDurability());
	}
	
	public ItemType getType()
	{
		return myType;
	}
	
	public void setName(String name)
	{
		ItemMeta meta = myItem.getItemMeta();
		meta.setDisplayName(name);
		myItem.setItemMeta(meta);
	}
	
	public String getName()
	{
		return myItem.getItemMeta().getDisplayName();
	}
	
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
	
	public List<String> getLore()
	{
		return myItem.getItemMeta().getLore();
	}
	
	public void setShiny(boolean shiny)
	{
		if(shiny)
			myItem.addEnchantment(Enchantment.DURABILITY, 1);
		else
			myItem.removeEnchantment(Enchantment.DURABILITY);
	}
	
	public boolean isShiny()
	{
		return myItem.containsEnchantment(Enchantment.DURABILITY);
	}
	
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
	
	public void setPermanent(boolean permanent)
	{
		myIsPermanent = permanent;
		setLore();
	}
	
	public boolean isPermanent()
	{
		return myIsPermanent;
	}
	
	public ItemStack toItemStack()
	{
		return new ItemStack(myItem);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
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
		Element item = (Element) element.getElementsByTagName("item").item(0);
		
		ItemType type = ItemType.valueOf(item.getAttribute("type"));
		myItem = new ItemStack(type.getMaterial(), Integer.valueOf(item.getAttribute("amount")), type.getDurability());
		
		setPermanent(Boolean.valueOf(item.getAttribute("permanent")));
		setShiny(Boolean.valueOf(item.getAttribute("shiny")));
		setName(item.getAttribute("name"));
		setFlags(Boolean.valueOf(item.getAttribute("flags")));
		
		if(item.hasAttribute("lore"))
			setLore(item.getAttribute("lore").split("/n"));
	}
}
