package com.wisteca.quartzlegion.entities.nms;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;

import com.wisteca.quartzlegion.entities.personnages.MoveableEntity;
import com.wisteca.quartzlegion.utils.Item;
import com.wisteca.quartzlegion.utils.Utils;

import net.minecraft.server.v1_11_R1.EntityZombie;
import net.minecraft.server.v1_11_R1.PathEntity;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;

/**
 * L'entité custom NMS.
 * @author Wisteca
 */

public class CustomZombie extends EntityZombie implements MoveableEntity {
	
	public CustomZombie(org.bukkit.World world)
	{
		super(((CraftWorld) world).getHandle());
		
		/*
		 * Suppression des pathfindergoals existants
		 */
		
		for(String name : Utils.getNamesOfFields(PathfinderGoalSelector.class, Set.class))
		{
			LinkedHashSet<?> goals = (LinkedHashSet<?>) Utils.getPrivateField(name, PathfinderGoalSelector.class, super.goalSelector);
			goals.clear();
			
			LinkedHashSet<?> targets = (LinkedHashSet<?>) Utils.getPrivateField(name, PathfinderGoalSelector.class, super.targetSelector);
			targets.clear();
		}
	}
	
	@Override
	public void moveTo(Location to)
	{
		PathEntity path = getNavigation().a(to.getBlockX(), to.getBlockY(), to.getBlockZ());
		for(int i = 0 ; ; i++)
		{
			try {
				
				System.out.println(path.a(i).a + " et " + path.a(i).b + " et " + path.a(i).c);
				
			} catch(ArrayIndexOutOfBoundsException ex){break;}
		}
		//PathEntity path = new PathEntity(new PathPoint[] {new PathPoint(to.getBlockX() + 5, to.getBlockY(), to.getBlockZ()) });
		getNavigation().a(path, 1f);
	}

	@Override
	public void moveTo(Location to, int speed)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeadPosition(int yaw, int pitch)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attack()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void damage()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setItemInHand(Item item)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Item getItemInHand()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
