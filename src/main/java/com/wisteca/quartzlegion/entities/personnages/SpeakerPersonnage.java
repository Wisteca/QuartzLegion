package com.wisteca.quartzlegion.entities.personnages;

import java.util.List;
import java.util.Set;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.w3c.dom.Element;

import com.wisteca.quartzlegion.utils.effects.EffectInterface.Part;

public class SpeakerPersonnage extends PassivePersonnage {

	public SpeakerPersonnage(Element element)
	{
		super(element);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean addPassenger(Entity arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addScoreboardTag(String arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eject()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getEntityId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFallDistance()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFireTicks()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EntityDamageEvent getLastDamageCause()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocation(Location arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFireTicks()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getPassenger()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> getPassengers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPortalCooldown()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> getScoreboardTags()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Server getServer()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTicksLived()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EntityType getType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getVehicle()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getVelocity()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getWorld()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasGravity()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCustomNameVisible()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDead()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGlowing()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInsideVehicle()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInvulnerable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnGround()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSilent()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean leaveVehicle()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void playEffect(EntityEffect arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removePassenger(Entity arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeScoreboardTag(String arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCustomNameVisible(boolean arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFallDistance(float arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFireTicks(int arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGlowing(boolean arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGravity(boolean arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInvulnerable(boolean arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setPassenger(Entity arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPortalCooldown(int arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSilent(boolean arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTicksLived(int arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVelocity(Vector arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Spigot spigot()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean teleport(Location arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean teleport(Entity arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean teleport(Location arg0, TeleportCause arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean teleport(Entity arg0, TeleportCause arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MetadataValue> getMetadata(String arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasMetadata(String arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeMetadata(String arg0, Plugin arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMetadata(String arg0, MetadataValue arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(String arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String[] arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPermission(String arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPermission(Permission arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPermissionSet(String arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPermissionSet(Permission arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void recalculatePermissions()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttachment(PermissionAttachment arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOp()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setOp(boolean arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCustomName(String arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location getEyeLocation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEvent(Event e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location getCurrentLocation(Part part)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
