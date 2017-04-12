package com.wisteca.quartzlegion.world.objects;

import java.util.Collection;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.wisteca.quartzlegion.world.Area;
import com.wisteca.quartzlegion.world.ProtectedObject;
import com.wisteca.quartzlegion.world.ProtectedWorld;
import com.wisteca.quartzlegion.world.WorldManager;

/**
 * Représente un bloc qui une fois cassé, peut reprendre sa forme original.
 * @author Wisteca
 */

public class ProtectedBlock implements ProtectedObject, Block {
	
	private Block myBukkitBlock;
	private ProtectedBlockState myState;
	
	private Biome myInitialBiome;
	private Material myInitialType;
	
	public ProtectedBlock(Block block)
	{
		myBukkitBlock = block;
		myInitialBiome = myBukkitBlock.getBiome();
		myInitialType = myBukkitBlock.getType();
		
		// switcher le type de state
		myState = new ProtectedBlockState(block);
	}
	
	@Override
	public void replace()
	{
		myBukkitBlock.setType(myInitialType);
		myBukkitBlock.setBiome(myInitialBiome);
		myState.replace();
	}

	@Override
	public Area getArea()
	{
		return getWorld().getAreaAt(myBukkitBlock.getLocation());
	}

	@Override
	public ProtectedWorld getWorld()
	{
		return WorldManager.getInstance().getWorld(myBukkitBlock.getWorld().getName());
	}
	
	@Override
	public boolean breakNaturally()
	{
		boolean breaked = myBukkitBlock.breakNaturally();
		if(breaked)
			getWorld().addChangedObject(this);
		
		return breaked;
	}

	@Override
	public boolean breakNaturally(ItemStack tool)
	{
		boolean breaked = myBukkitBlock.breakNaturally(tool);
		if(breaked)
			getWorld().addChangedObject(this);
		
		return breaked;
	}

	@Override
	public void setBiome(Biome biome)
	{
		myInitialBiome = getBiome();
		myBukkitBlock.setBiome(biome);
	}

	@Override
	public void setType(Material type)
	{
		myInitialType = getType();
		getWorld().addChangedObject(this);
		myBukkitBlock.setType(type);
	}

	@Override
	public void setType(Material type, boolean applyPhysics)
	{
		myInitialType = getType();
		getWorld().addChangedObject(this);
		myBukkitBlock.setType(type, applyPhysics);
	}

	@Override
	public BlockState getState()
	{
		return myState;
	}
	
	
	/*
	 * Méthodes non-override --------------------------------------- 
	 */
	
	
	@Override
	public Chunk getChunk()
	{
		return myBukkitBlock.getChunk();
	}
	
	@Override
	public Biome getBiome()
	{
		return myBukkitBlock.getBiome();
	}
	
	@Override
	public List<MetadataValue> getMetadata(String data)
	{
		return myBukkitBlock.getMetadata(data);
	}

	@Override
	public boolean hasMetadata(String data)
	{
		return myBukkitBlock.hasMetadata(data);
	}

	@Override
	public void removeMetadata(String data, Plugin plugin)
	{
		myBukkitBlock.removeMetadata(data, plugin);
	}

	@Override
	public void setMetadata(String data, MetadataValue value)
	{
		myBukkitBlock.setMetadata(data, value);
	}

	@Override
	public int getBlockPower()
	{
		return myBukkitBlock.getBlockPower();
	}

	@Override
	public int getBlockPower(BlockFace face)
	{
		return myBukkitBlock.getBlockPower(face);
	}

	@Deprecated
	@Override
	public byte getData()
	{
		return myBukkitBlock.getData();
	}

	@Override
	public Collection<ItemStack> getDrops()
	{
		return myBukkitBlock.getDrops();
	}

	@Override
	public Collection<ItemStack> getDrops(ItemStack item)
	{
		return myBukkitBlock.getDrops(item);
	}

	@Override
	public BlockFace getFace(Block block)
	{
		return myBukkitBlock.getFace(block);
	}

	@Override
	public double getHumidity()
	{
		return myBukkitBlock.getHumidity();
	}

	@Override
	public byte getLightFromBlocks()
	{
		return myBukkitBlock.getLightFromBlocks();
	}

	@Override
	public byte getLightFromSky()
	{
		return myBukkitBlock.getLightFromSky();
	}

	@Override
	public byte getLightLevel()
	{
		return myBukkitBlock.getLightLevel();
	}

	@Override
	public Location getLocation()
	{
		return myBukkitBlock.getLocation();
	}

	@Override
	public Location getLocation(Location loc)
	{
		return myBukkitBlock.getLocation(loc);
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction()
	{
		return myBukkitBlock.getPistonMoveReaction();
	}

	@Override
	public Block getRelative(BlockFace face)
	{
		return myBukkitBlock.getRelative(face);
	}

	@Override
	public Block getRelative(BlockFace face, int distance)
	{
		return myBukkitBlock.getRelative(face, distance);
	}

	@Override
	public Block getRelative(int modX, int modY, int modZ)
	{
		return myBukkitBlock.getRelative(modX, modY, modZ);
	}

	@Override
	public double getTemperature()
	{
		return myBukkitBlock.getTemperature();
	}

	@Override
	public Material getType()
	{
		return myBukkitBlock.getType();
	}
	
	@Deprecated
	@Override
	public int getTypeId()
	{
		return myBukkitBlock.getTypeId();
	}

	@Override
	public int getX()
	{
		return myBukkitBlock.getX();
	}

	@Override
	public int getY()
	{
		return myBukkitBlock.getY();
	}

	@Override
	public int getZ()
	{
		return myBukkitBlock.getZ();
	}

	@Override
	public boolean isBlockFaceIndirectlyPowered(BlockFace face)
	{
		return myBukkitBlock.isBlockFaceIndirectlyPowered(face);
	}

	@Override
	public boolean isBlockFacePowered(BlockFace face)
	{
		return myBukkitBlock.isBlockFacePowered(face);
	}

	@Override
	public boolean isBlockIndirectlyPowered()
	{
		return myBukkitBlock.isBlockIndirectlyPowered();
	}

	@Override
	public boolean isBlockPowered()
	{
		return myBukkitBlock.isBlockPowered();
	}

	@Override
	public boolean isEmpty()
	{
		return myBukkitBlock.isEmpty();
	}

	@Override
	public boolean isLiquid()
	{
		return myBukkitBlock.isLiquid();
	}

	@Deprecated
	@Override
	public void setData(byte data)
	{
		myBukkitBlock.setData(data);
	}

	@Deprecated
	@Override
	public void setData(byte data, boolean applyPhysics)
	{
		myBukkitBlock.setData(data, applyPhysics);
	}

	@Deprecated
	@Override
	public boolean setTypeId(int id)
	{
		return myBukkitBlock.setTypeId(id);
	}

	@Deprecated
	@Override
	public boolean setTypeId(int id, boolean applyPhysics)
	{
		return myBukkitBlock.setTypeId(id, applyPhysics);
	}

	@Deprecated
	@Override
	public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics)
	{
		return myBukkitBlock.setTypeIdAndData(type, data, applyPhysics);
	}
}
