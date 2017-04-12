package com.wisteca.quartzlegion.world.objects;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.wisteca.quartzlegion.world.Area;
import com.wisteca.quartzlegion.world.ProtectedObject;
import com.wisteca.quartzlegion.world.ProtectedWorld;
import com.wisteca.quartzlegion.world.WorldManager;

/**
 * Représente l'état d'un bloc protégé.
 * @author Wisteca
 */

public class ProtectedBlockState implements BlockState, ProtectedObject {

	private BlockState myBukkitState;
	private Block myBlock;
	
	private MaterialData myInitialData;
	private Material myInitialType;
	
	public ProtectedBlockState(Block block)
	{
		myBukkitState = block.getState();
		myBlock = block;
		
		myInitialData = myBukkitState.getData();
		myInitialType = myBukkitState.getType();
	}
	
	@Override
	public void replace()
	{
		myBukkitState.setData(myInitialData);
		myBukkitState.setType(myInitialType);
		update(true);
	}

	@Override
	public Area getArea()
	{
		return getWorld().getAreaAt(myBukkitState.getLocation());
	}

	@Override
	public ProtectedWorld getWorld()
	{
		return WorldManager.getInstance().getWorld(myBukkitState.getWorld().getName());
	}

	@Override
	public Block getBlock()
	{
		return myBlock;
	}

	@Override
	public void setData(MaterialData data)
	{
		myInitialData = myBukkitState.getData();
		getWorld().addChangedObject(this);
		myBukkitState.setData(data);
	}

	@Override
	public void setType(Material type)
	{
		myInitialType = myBukkitState.getType();
		getWorld().addChangedObject(this);
		myBukkitState.setType(type);
	}

	
	/***** Méthodes non-override ***************************/
	
	
	@Override
	public List<MetadataValue> getMetadata(String data)
	{
		return myBukkitState.getMetadata(data);
	}

	@Override
	public boolean hasMetadata(String data)
	{
		return myBukkitState.hasMetadata(data);
	}

	@Override
	public void removeMetadata(String data, Plugin plugin)
	{
		myBukkitState.removeMetadata(data, plugin);
	}

	@Override
	public void setMetadata(String data, MetadataValue value)
	{
		myBukkitState.setMetadata(data, value);
	}

	@Override
	public Chunk getChunk()
	{
		return myBukkitState.getChunk();
	}

	@Override
	public MaterialData getData()
	{
		return myBukkitState.getData();
	}

	@Override
	public byte getLightLevel()
	{
		return myBukkitState.getLightLevel();
	}

	@Override
	public Location getLocation()
	{
		return myBukkitState.getLocation();
	}

	@Override
	public Location getLocation(Location loc)
	{
		return myBukkitState.getLocation(loc);
	}

	@Deprecated
	@Override
	public byte getRawData()
	{
		return myBukkitState.getRawData();
	}

	@Override
	public Material getType()
	{
		return myBukkitState.getType();
	}

	@Deprecated
	@Override
	public int getTypeId()
	{
		return myBukkitState.getTypeId();
	}

	@Override
	public int getX()
	{
		return myBukkitState.getX();
	}

	@Override
	public int getY()
	{
		return myBukkitState.getY();
	}

	@Override
	public int getZ()
	{
		return myBukkitState.getZ();
	}

	@Override
	public boolean isPlaced()
	{
		return myBukkitState.isPlaced();
	}

	@Deprecated
	@Override
	public void setRawData(byte data)
	{
		myBukkitState.setRawData(data);
	}

	@Deprecated
	@Override
	public boolean setTypeId(int id)
	{
		return myBukkitState.setTypeId(id);
	}

	@Override
	public boolean update()
	{
		return myBukkitState.update();
	}

	@Override
	public boolean update(boolean force)
	{
		return myBukkitState.update(force);
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics)
	{
		return myBukkitState.update(force, applyPhysics);
	}
}
