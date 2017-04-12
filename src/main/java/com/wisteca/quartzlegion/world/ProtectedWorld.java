package com.wisteca.quartzlegion.world;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.data.Constants;
import com.wisteca.quartzlegion.data.Serializer;
import com.wisteca.quartzlegion.entities.PersonnageManager;
import com.wisteca.quartzlegion.entities.personnages.Joueur;
import com.wisteca.quartzlegion.world.objects.ProtectedBlock;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Explosion;

/**
 * Représente un monde dans lequel tout élément détruit ou changé sera replacé après un certain temps en fonction de la zone.
 * @author Wisteca
 */

public class ProtectedWorld implements World, Serializer {
	
	private World myBukkitWorld;
	private ConcurrentHashMap<ProtectedObject, Integer> myChangedObjects = new ConcurrentHashMap<>();
	private ArrayList<Area> myAreas = new ArrayList<>();
	
	public ProtectedWorld(World world)
	{
		myBukkitWorld = world;
	}

	public void addChangedObject(ProtectedObject object)
	{
		int replaceTime = object.getArea() == null ? Constants.DEFAULT_REPLACE_TIME : object.getArea().getReplaceTime();
		myChangedObjects.put(object, replaceTime);
	}
	
	public Area getAreaAt(Location loc)
	{
		for(Area area : myAreas)
		{
			if(area.isInArea(loc))
				return area;
		}
		
		return null;
	}
	
	public void doTime() // appeler chaque seconde
	{
		for(ProtectedObject object : myChangedObjects.keySet())
		{
			if(myChangedObjects.get(object) == 0)
			{
				for(Joueur j : PersonnageManager.getInstance().getOnlinePlayers())
				{
					int replaceRadius = object.getArea() == null ? Constants.DEFAULT_REPLACE_RADIUS : object.getArea().getReplaceRadius();
					if(j.getLocation().distance(object.getLocation()) < replaceRadius)
						break;
					
					object.replace();
					myChangedObjects.remove(object);
				}
			}
			else
				myChangedObjects.put(object, myChangedObjects.get(object) - 1);
		}
	}
	
	/**
	 * Sérialiser le monde et ses zones, ne sérialise pas les objets changés !
	 */
	
	@Override
	public void serialize(Element toWrite)
	{
		Element world = toWrite.getOwnerDocument().createElement("world");
		toWrite.appendChild(world);
		world.setAttribute("name", getName());
		
		for(Area area : myAreas)
		{
			Element areaElement = toWrite.getOwnerDocument().createElement("area");
			world.appendChild(areaElement);
			area.serialize(areaElement);
		}
	}

	@Override
	public void deserialize(Element element)
	{
		
	}

	@Override
	public boolean createExplosion(Location loc, float power)
	{
		return createExplosion(loc, power, true);
	}

	@Override
	public boolean createExplosion(Location loc, float power, boolean setFire)
	{
		return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power)
	{
		return createExplosion(x, y, z, power, true);
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire)
	{
		return createExplosion(x, y, z, power, setFire, true);
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks)
	{
		Explosion explosion = null;
		
		try {
			
			explosion = new Explosion(((CraftWorld) myBukkitWorld).getHandle(), null, x, y, z, power, false, false);
			explosion.a();
			
			for(BlockPosition pos : explosion.getBlocks())
			{
				Block block = getBlockAt(pos.getX(), pos.getY(), pos.getZ());
				block.setType(Material.AIR);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return !explosion.wasCanceled;
	}

	@Override
	public boolean generateTree(Location arg0, TreeType arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean generateTree(Location arg0, TreeType arg1, BlockChangeDelegate arg2)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Block getBlockAt(Location loc)
	{
		return new ProtectedBlock(myBukkitWorld.getBlockAt(loc));
	}

	@Override
	public Block getBlockAt(int x, int y, int z)
	{
		return new ProtectedBlock(myBukkitWorld.getBlockAt(x, y, z));
	}
	
	@Override
	public List<Entity> getEntities()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(@SuppressWarnings("unchecked") Class<T>... arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Entity> getEntitiesByClasses(Class<?>... arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LivingEntity> getLivingEntities()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chunk[] getLoadedChunks()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Entity> getNearbyEntities(Location arg0, double arg1, double arg2, double arg3)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Player> getPlayers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BlockPopulator> getPopulators()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setBiome(int arg0, int arg1, Biome arg2)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends Entity> T spawn(Location arg0, Class<T> arg1) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> T spawn(Location arg0, Class<T> arg1, Consumer<T> arg2) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location arg0, MaterialData arg1) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location arg0, Material arg1, byte arg2) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FallingBlock spawnFallingBlock(Location arg0, int arg1, byte arg2) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LightningStrike strikeLightning(Location arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LightningStrike strikeLightningEffect(Location arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getHighestBlockAt(Location loc)
	{
		return null;
	}

	@Override
	public Block getHighestBlockAt(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*
	 * Méhodes non-override ---------------------------------------
	 */
	
	@Override
	public Chunk getChunkAt(Location loc)
	{
		return myBukkitWorld.getChunkAt(loc);
	}

	@Override
	public Chunk getChunkAt(Block block)
	{
		return myBukkitWorld.getChunkAt(block);
	}

	@Override
	public Chunk getChunkAt(int x, int z)
	{
		return myBukkitWorld.getChunkAt(x, z);
	}
	
	@Override
	public Biome getBiome(int x, int z)
	{
		return myBukkitWorld.getBiome(x, z);
	}
	
	@Override
	public Set<String> getListeningPluginChannels()
	{
		return myBukkitWorld.getListeningPluginChannels();
	}

	@Override
	public void sendPluginMessage(Plugin source, String channel, byte[] message)
	{
		myBukkitWorld.sendPluginMessage(source, channel, message);
	}

	@Override
	public List<MetadataValue> getMetadata(String data)
	{
		return myBukkitWorld.getMetadata(data);
	}

	@Override
	public boolean hasMetadata(String data)
	{
		return myBukkitWorld.hasMetadata(data);
	}

	@Override
	public void removeMetadata(String data, Plugin plugin)
	{
		myBukkitWorld.removeMetadata(data, plugin);
	}

	@Override
	public void setMetadata(String data, MetadataValue value)
	{
		myBukkitWorld.setMetadata(data, value);
	}

	@Override
	public boolean canGenerateStructures()
	{
		return myBukkitWorld.canGenerateStructures();
	}

	@Override
	public Item dropItem(Location loc, ItemStack item)
	{
		return myBukkitWorld.dropItem(loc, item);
	}

	@Override
	public Item dropItemNaturally(Location loc, ItemStack item)
	{
		return myBukkitWorld.dropItemNaturally(loc, item);
	}

	@Override
	public boolean getAllowAnimals()
	{
		return myBukkitWorld.getAllowAnimals();
	}

	@Override
	public boolean getAllowMonsters()
	{
		return myBukkitWorld.getAllowMonsters();
	}

	@Override
	public int getAmbientSpawnLimit()
	{
		return myBukkitWorld.getAmbientSpawnLimit();
	}

	@Override
	public int getAnimalSpawnLimit()
	{
		return myBukkitWorld.getAnimalSpawnLimit();
	}

	@Deprecated
	@Override
	public int getBlockTypeIdAt(Location loc)
	{
		return myBukkitWorld.getBlockTypeIdAt(loc);
	}

	@Deprecated
	@Override
	public int getBlockTypeIdAt(int x, int y, int z)
	{
		return myBukkitWorld.getBlockTypeIdAt(x, y, z);
	}

	@Override
	public Difficulty getDifficulty()
	{
		return myBukkitWorld.getDifficulty();
	}

	@Override
	public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeTempsRain)
	{
		return myBukkitWorld.getEmptyChunkSnapshot(x, z, includeBiome, includeTempsRain);
	}

	@Override
	public Environment getEnvironment()
	{
		return myBukkitWorld.getEnvironment();
	}

	@Override
	public long getFullTime()
	{
		return myBukkitWorld.getFullTime();
	}

	@Override
	public String getGameRuleValue(String value)
	{
		return myBukkitWorld.getGameRuleValue(value);
	}

	@Override
	public String[] getGameRules()
	{
		return myBukkitWorld.getGameRules();
	}

	@Override
	public ChunkGenerator getGenerator()
	{
		return myBukkitWorld.getGenerator();
	}

	@Override
	public int getHighestBlockYAt(Location loc)
	{
		return myBukkitWorld.getHighestBlockYAt(loc);
	}

	@Override
	public int getHighestBlockYAt(int x, int y)
	{
		return myBukkitWorld.getHighestBlockYAt(x, y);
	}

	@Override
	public double getHumidity(int x, int z)
	{
		return myBukkitWorld.getHumidity(x, z);
	}

	@Override
	public boolean getKeepSpawnInMemory()
	{
		return myBukkitWorld.getKeepSpawnInMemory();
	}

	@Override
	public int getMaxHeight()
	{
		return myBukkitWorld.getMaxHeight();
	}

	@Override
	public int getMonsterSpawnLimit()
	{
		return myBukkitWorld.getMonsterSpawnLimit();
	}

	@Override
	public String getName()
	{
		return myBukkitWorld.getName();
	}

	@Override
	public boolean getPVP()
	{
		return myBukkitWorld.getPVP();
	}

	@Override
	public int getSeaLevel()
	{
		return myBukkitWorld.getSeaLevel();
	}

	@Override
	public long getSeed()
	{
		return myBukkitWorld.getSeed();
	}

	@Override
	public Location getSpawnLocation()
	{
		return myBukkitWorld.getSpawnLocation();
	}

	@Override
	public double getTemperature(int x, int z)
	{
		return myBukkitWorld.getTemperature(x, z);
	}

	@Override
	public int getThunderDuration()
	{
		return myBukkitWorld.getThunderDuration();
	}

	@Override
	public long getTicksPerAnimalSpawns()
	{
		return myBukkitWorld.getTicksPerAnimalSpawns();
	}

	@Override
	public long getTicksPerMonsterSpawns()
	{
		return myBukkitWorld.getTicksPerMonsterSpawns();
	}

	@Override
	public long getTime()
	{
		return myBukkitWorld.getTime();
	}

	@Override
	public UUID getUID()
	{
		return myBukkitWorld.getUID();
	}

	@Override
	public int getWaterAnimalSpawnLimit()
	{
		return myBukkitWorld.getWaterAnimalSpawnLimit();
	}

	@Override
	public int getWeatherDuration()
	{
		return myBukkitWorld.getWeatherDuration();
	}

	@Override
	public WorldBorder getWorldBorder()
	{
		return myBukkitWorld.getWorldBorder();
	}

	@Override
	public File getWorldFolder()
	{
		return myBukkitWorld.getWorldFolder();
	}

	@Override
	public WorldType getWorldType()
	{
		return myBukkitWorld.getWorldType();
	}

	@Override
	public boolean hasStorm()
	{
		return myBukkitWorld.hasStorm();
	}

	@Override
	public boolean isAutoSave()
	{
		return myBukkitWorld.isAutoSave();
	}

	@Override
	public boolean isChunkInUse(int x, int z)
	{
		return myBukkitWorld.isChunkInUse(x, z);
	}

	@Override
	public boolean isChunkLoaded(Chunk chunk)
	{
		return myBukkitWorld.isChunkLoaded(chunk);
	}

	@Override
	public boolean isChunkLoaded(int x, int z)
	{
		return myBukkitWorld.isChunkLoaded(x, z);
	}

	@Override
	public boolean isGameRule(String rule)
	{
		return myBukkitWorld.isGameRule(rule);
	}

	@Override
	public boolean isThundering()
	{
		return myBukkitWorld.isThundering();
	}

	@Override
	public void loadChunk(Chunk chunk)
	{
		myBukkitWorld.loadChunk(chunk);
	}

	@Override
	public void loadChunk(int x, int z)
	{
		myBukkitWorld.loadChunk(x, z);
	}

	@Override
	public boolean loadChunk(int x, int z, boolean generate)
	{
		return myBukkitWorld.loadChunk(x, z, generate);
	}

	@Override
	public void playEffect(Location loc, Effect effect, int data)
	{
		myBukkitWorld.playEffect(loc, effect, data);
	}

	@Override
	public <T> void playEffect(Location loc, Effect effect, T data)
	{
		myBukkitWorld.playEffect(loc, effect, data);
	}

	@Override
	public void playEffect(Location loc, Effect effect, int data, int radius)
	{
		myBukkitWorld.playEffect(loc, effect, data, radius);
	}

	@Override
	public <T> void playEffect(Location loc, Effect effect, T data, int radius)
	{
		myBukkitWorld.playEffect(loc, effect, data, radius);
	}

	@Override
	public void playSound(Location loc, Sound sound, float volume, float pitch)
	{
		myBukkitWorld.playSound(loc, sound, volume, pitch);
	}

	@Override
	public void playSound(Location loc, String sound, float volume, float pitch)
	{
		myBukkitWorld.playSound(loc, sound, volume, pitch);
	}

	@Override
	public void playSound(Location loc, Sound sound, SoundCategory category, float volume, float pitch)
	{
		myBukkitWorld.playSound(loc, sound, category, volume, pitch);
	}

	@Override
	public void playSound(Location loc, String sound, SoundCategory category, float volume, float pitch)
	{
		myBukkitWorld.playSound(loc, sound, category, volume, pitch);
	}

	@Deprecated
	@Override
	public boolean refreshChunk(int x, int z)
	{
		return myBukkitWorld.refreshChunk(x, z);
	}

	@Override
	public boolean regenerateChunk(int x, int z)
	{
		return myBukkitWorld.regenerateChunk(x, z);
	}

	@Override
	public void save()
	{
		myBukkitWorld.save();
	}

	@Override
	public void setAmbientSpawnLimit(int limit)
	{
		myBukkitWorld.setAmbientSpawnLimit(limit);
	}

	@Override
	public void setAnimalSpawnLimit(int limit)
	{
		myBukkitWorld.setAmbientSpawnLimit(limit);
	}

	@Override
	public void setAutoSave(boolean auto)
	{
		myBukkitWorld.setAutoSave(auto);
	}

	@Override
	public void setDifficulty(Difficulty difficulty)
	{
		myBukkitWorld.setDifficulty(difficulty);
	}

	@Override
	public void setFullTime(long time)
	{
		myBukkitWorld.setFullTime(time);
	}

	@Override
	public boolean setGameRuleValue(String rule, String value)
	{
		return myBukkitWorld.setGameRuleValue(rule, value);
	}

	@Override
	public void setKeepSpawnInMemory(boolean keep)
	{
		myBukkitWorld.setKeepSpawnInMemory(keep);
	}

	@Override
	public void setMonsterSpawnLimit(int limit)
	{
		myBukkitWorld.setMonsterSpawnLimit(limit);
	}

	@Override
	public void setPVP(boolean pvp)
	{
		myBukkitWorld.setPVP(pvp);
	}

	@Override
	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals)
	{
		myBukkitWorld.setSpawnFlags(allowMonsters, allowAnimals);
	}

	@Override
	public boolean setSpawnLocation(int x, int y, int z)
	{
		return myBukkitWorld.setSpawnLocation(x, y, z);
	}

	@Override
	public void setStorm(boolean storm)
	{
		myBukkitWorld.setStorm(storm);
	}

	@Override
	public void setThunderDuration(int duration)
	{
		myBukkitWorld.setThunderDuration(duration);
	}

	@Override
	public void setThundering(boolean thunder)
	{
		myBukkitWorld.setThundering(thunder);
	}

	@Override
	public void setTicksPerAnimalSpawns(int ticks)
	{
		myBukkitWorld.setTicksPerAnimalSpawns(ticks);
	}

	@Override
	public void setTicksPerMonsterSpawns(int ticks)
	{
		myBukkitWorld.setTicksPerMonsterSpawns(ticks);
	}

	@Override
	public void setTime(long time)
	{
		myBukkitWorld.setTime(time);
	}

	@Override
	public void setWaterAnimalSpawnLimit(int limit)
	{
		myBukkitWorld.setWaterAnimalSpawnLimit(limit);
	}

	@Override
	public void setWeatherDuration(int duration)
	{
		myBukkitWorld.setWeatherDuration(duration);
	}

	@Override
	public Arrow spawnArrow(Location loc, Vector direction, float speed, float spread)
	{
		return myBukkitWorld.spawnArrow(loc, direction, speed, spread);
	}

	@Override
	public <T extends Arrow> T spawnArrow(Location loc, Vector direction, float speed, float spread, Class<T> clazz)
	{
		return myBukkitWorld.spawnArrow(loc, direction, speed, spread, clazz);
	}

	@Override
	public Entity spawnEntity(Location loc, EntityType entity)
	{
		return myBukkitWorld.spawnEntity(loc, entity);
	}

	@Override
	public void spawnParticle(Particle particle, Location loc, int count)
	{
		myBukkitWorld.spawnParticle(particle, loc, count);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location loc, int count, T data)
	{
		myBukkitWorld.spawnParticle(particle, loc, count, data);
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count)
	{
		myBukkitWorld.spawnParticle(particle, x, y, z, count);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data)
	{
		myBukkitWorld.spawnParticle(particle, x, y, z, count, data);
	}

	@Override
	public void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ)
	{
		myBukkitWorld.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ, T data)
	{
		myBukkitWorld.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, data);
	}

	@Override
	public void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ, double extra)
	{
		myBukkitWorld.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, extra);
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ)
	{
		myBukkitWorld.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);
	}

	@Override
	public <T> void spawnParticle(Particle particle, Location loc, int count, double offsetX, double offsetY, double offsetZ, double extra, T data)
	{
		myBukkitWorld.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, extra, data);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data)
	{
		myBukkitWorld.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);
	}

	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra)
	{
		myBukkitWorld.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data)
	{
		myBukkitWorld.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
	}

	@Override
	public Spigot spigot()
	{
		return myBukkitWorld.spigot();
	}

	@Override
	public boolean unloadChunk(Chunk chunk)
	{
		return myBukkitWorld.unloadChunk(chunk);
	}

	@Override
	public boolean unloadChunk(int x, int z)
	{
		return myBukkitWorld.unloadChunk(x, z);
	}

	@Override
	public boolean unloadChunk(int x, int z, boolean save)
	{
		return myBukkitWorld.unloadChunk(x, z, save);
	}

	@Deprecated
	@Override
	public boolean unloadChunk(int x, int z, boolean save, boolean safe)
	{
		return myBukkitWorld.unloadChunk(x, z, save, safe);
	}

	@Override
	public boolean unloadChunkRequest(int x, int z)
	{
		return myBukkitWorld.unloadChunkRequest(x, z);
	}

	@Override
	public boolean unloadChunkRequest(int x, int z, boolean safe)
	{
		return myBukkitWorld.unloadChunkRequest(x, z, safe);
	}
}
