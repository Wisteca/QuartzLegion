package com.wisteca.quartzlegion.entities.nms.registry;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.wisteca.quartzlegion.entities.nms.CustomZombie;
import com.wisteca.quartzlegion.entities.personnages.MoveableEntity;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.MinecraftKey;

public enum QuartzEntityType {
	
	QUARTZ_ZOMBIE("Zombie", 54, CustomZombie.class);

	private QuartzEntityType(String name, int id, Class<? extends Entity> clazz)
    {
		EntityTypes.b.a(id, new MinecraftKey(name), clazz);
    }
	
	public MoveableEntity spawn(Location loc)
	{
		CustomZombie z = new CustomZombie(loc.getWorld());
		z.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftWorld) loc.getWorld()).addEntity(z, SpawnReason.CUSTOM);
		
		return z;
	}
}
