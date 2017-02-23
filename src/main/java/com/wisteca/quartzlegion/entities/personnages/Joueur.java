package com.wisteca.quartzlegion.entities.personnages;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.wisteca.quartzlegion.entities.personnages.skills.Skill.ClasseSkill;

public class Joueur extends Personnage {

	private Player myPlayer;
	
	private int mySerializeTime = 0;
	
	public Joueur(UUID uuid, Race race, Classe classe, Player player)
	{
		super(uuid, race, classe, null, null, null, null, 0, 100);
		myPlayer = player;
		
	}

	@Override
	protected void healthChanged()
	{
		if(getHealth() != 0)
		{
			if(myPlayer.getGameMode().equals(GameMode.SPECTATOR))
				myPlayer.setGameMode(GameMode.SURVIVAL);
			else
				myPlayer.setHealth(getHealth() / getTemporarySkill(ClasseSkill.VIE_TOTALE) * 20);
		}
		else if(myPlayer.getGameMode().equals(GameMode.SPECTATOR))
			myPlayer.setGameMode(GameMode.SURVIVAL);
	}

	@Override
	protected void energyChanged()
	{
		
	}

	@Override
	public void damageInfo(int damages, Personnage target)
	{
		
	}
	
	@Override
	public void doTime()
	{
		super.doTime();
		
		mySerializeTime++;
		if(mySerializeTime > 6000)
		{
			mySerializeTime = 0;
			/*
			 * SERIALIZER
			 */
		}
	}
	
	/*
	 *  Bukkit methods
	 */
	
	@Override
	public boolean addScoreboardTag(String tag)
	{
		return myPlayer.addScoreboardTag(tag);
	}

	@Override
	public boolean eject()
	{
		return myPlayer.eject();
	}

	@Override
	public String getCustomName()
	{
		return myPlayer.getCustomName();
	}

	@Override
	public int getEntityId()
	{
		return myPlayer.getEntityId();
	}

	@Override
	public float getFallDistance()
	{
		return myPlayer.getFallDistance();
	}

	@Override
	public int getFireTicks()
	{
		return myPlayer.getFireTicks();
	}

	@Override
	public EntityDamageEvent getLastDamageCause()
	{
		return myPlayer.getLastDamageCause();
	}

	@Override
	public Location getLocation()
	{
		return myPlayer.getLocation();
	}

	@Override
	public Location getLocation(Location loc)
	{
		return myPlayer.getLocation(loc);
	}

	@Override
	public int getMaxFireTicks()
	{
		return myPlayer.getMaxFireTicks();
	}

	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z)
	{
		return myPlayer.getNearbyEntities(x, y, z);
	}

	@Deprecated
	@Override
	public Entity getPassenger()
	{
		return myPlayer.getPassenger();
	}

	@Override
	public int getPortalCooldown()
	{
		return myPlayer.getPortalCooldown();
	}

	@Override
	public Set<String> getScoreboardTags()
	{
		return myPlayer.getScoreboardTags();
	}

	@Override
	public Server getServer()
	{
		return myPlayer.getServer();
	}

	@Override
	public int getTicksLived()
	{
		return myPlayer.getTicksLived();
	}

	@Override
	public EntityType getType()
	{
		return myPlayer.getType();
	}

	@Override
	public Entity getVehicle()
	{
		return myPlayer.getVehicle();
	}

	@Override
	public Vector getVelocity()
	{
		return myPlayer.getVelocity();
	}

	@Override
	public World getWorld()
	{
		return myPlayer.getWorld();
	}

	@Override
	public boolean hasGravity()
	{
		return myPlayer.hasGravity();
	}

	@Override
	public boolean isCustomNameVisible()
	{
		return myPlayer.isCustomNameVisible();
	}

	@Override
	public boolean isEmpty()
	{
		return myPlayer.isEmpty();
	}

	@Override
	public boolean isGlowing()
	{
		return myPlayer.isGlowing();
	}

	@Override
	public boolean isInsideVehicle()
	{
		return myPlayer.isInsideVehicle();
	}

	@Override
	public boolean isInvulnerable()
	{
		return myPlayer.isInvulnerable();
	}

	@Deprecated
	@Override
	public boolean isOnGround()
	{
		return myPlayer.isOnGround();
	}

	@Override
	public boolean isSilent()
	{
		return myPlayer.isSilent();
	}

	@Override
	public boolean isValid()
	{
		return myPlayer.isValid();
	}

	@Override
	public boolean leaveVehicle()
	{
		return myPlayer.leaveVehicle();
	}

	@Override
	public void playEffect(EntityEffect effect)
	{
		myPlayer.playEffect(effect);
	}

	@Override
	public void remove()
	{
		myPlayer.remove();
	}

	@Override
	public boolean removeScoreboardTag(String tag)
	{
		return myPlayer.removeScoreboardTag(tag);
	}

	@Override
	public void setCustomName(String customName)
	{
		myPlayer.setCustomName(customName);
	}

	@Override
	public void setCustomNameVisible(boolean visible)
	{
		myPlayer.setCustomNameVisible(visible);
	}

	@Override
	public void setFallDistance(float distance)
	{
		myPlayer.setFallDistance(distance);
	}

	@Override
	public void setFireTicks(int fireTicks)
	{
		myPlayer.setFireTicks(fireTicks);
	}

	@Override
	public void setGlowing(boolean glowing)
	{
		myPlayer.setGlowing(glowing);
	}

	@Override
	public void setGravity(boolean gravity)
	{
		myPlayer.setGravity(gravity);
	}

	@Override
	public void setInvulnerable(boolean invulnerable)
	{
		myPlayer.setInvulnerable(invulnerable);
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent e)
	{
		myPlayer.setLastDamageCause(e);
	}

	@Deprecated
	@Override
	public boolean setPassenger(Entity passenger)
	{
		return myPlayer.setPassenger(passenger);
	}

	@Override
	public void setPortalCooldown(int ticks)
	{
		myPlayer.setPortalCooldown(ticks);
	}

	@Override
	public void setSilent(boolean silent)
	{
		myPlayer.setSilent(silent);
	}

	@Override
	public void setTicksLived(int ticks)
	{
		myPlayer.setTicksLived(ticks);
	}

	@Override
	public void setVelocity(Vector vector)
	{
		myPlayer.setVelocity(vector);
	}

	@Override
	public Spigot spigot()
	{
		return myPlayer.spigot();
	}

	@Override
	public boolean teleport(Location loc)
	{
		return myPlayer.teleport(loc);
	}

	@Override
	public boolean teleport(Entity entity)
	{
		return myPlayer.teleport(entity);
	}

	@Override
	public boolean teleport(Location loc, TeleportCause cause)
	{
		return myPlayer.teleport(loc, cause);
	}

	@Override
	public boolean teleport(Entity entity, TeleportCause cause)
	{
		return myPlayer.teleport(entity, cause);
	}

	@Override
	public List<MetadataValue> getMetadata(String data)
	{
		return myPlayer.getMetadata(data);
	}

	@Override
	public boolean hasMetadata(String data)
	{
		return myPlayer.hasMetadata(data);
	}

	@Override
	public void removeMetadata(String data, Plugin plugin)
	{
		myPlayer.removeMetadata(data, plugin);
	}

	@Override
	public void setMetadata(String data, MetadataValue value)
	{
		myPlayer.setMetadata(data, value);
	}

	@Override
	public String getName()
	{
		return myPlayer.getName();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin)
	{
		return myPlayer.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks)
	{
		return myPlayer.addAttachment(plugin, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value)
	{
		return myPlayer.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks)
	{
		return myPlayer.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{
		return myPlayer.getEffectivePermissions();
	}

	@Override
	public boolean hasPermission(String permission)
	{
		return myPlayer.hasPermission(permission);
	}

	@Override
	public boolean hasPermission(Permission permission)
	{
		return myPlayer.hasPermission(permission);
	}

	@Override
	public boolean isPermissionSet(String permission)
	{
		return myPlayer.isPermissionSet(permission);
	}

	@Override
	public boolean isPermissionSet(Permission permission)
	{
		return myPlayer.isPermissionSet(permission);
	}

	@Override
	public void recalculatePermissions()
	{
		myPlayer.recalculatePermissions();
	}

	@Override
	public void removeAttachment(PermissionAttachment attachement)
	{
		myPlayer.removeAttachment(attachement);
	}

	@Override
	public boolean isOp()
	{
		return myPlayer.isOp();
	}

	@Override
	public void setOp(boolean op)
	{
		myPlayer.setOp(op);
	}

	@Override
	public Location getEyeLocation()
	{
		return myPlayer.getEyeLocation();
	}

	@Override
	public void sendMessage(String message)
	{
		myPlayer.sendMessage(message);
	}

	@Override
	public void sendMessage(String[] messages)
	{
		myPlayer.sendMessage(messages);
	}

	@Override
	public boolean addPassenger(Entity passenger)
	{
		return myPlayer.addPassenger(passenger);
	}

	@Override
	public List<Entity> getPassengers()
	{
		return myPlayer.getPassengers();
	}

	@Override
	public boolean removePassenger(Entity passenger)
	{
		return myPlayer.removePassenger(passenger);
	}
}
