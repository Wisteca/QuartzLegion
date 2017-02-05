package com.wisteca.quartzlegion.utils.effects;

import java.util.ArrayList;

import org.bukkit.Particle;

public abstract class AOEffect implements Effect {
	
	private float mySpeed;
	private ArrayList<Particle> myParticles = new ArrayList<>();
	
	public void setSpeed(float speed)
	{
		mySpeed = speed;
	}
	
	public float getSpeed()
	{
		return mySpeed;
	}
	
	public void setParticles(ArrayList<Particle> particles)
	{
		myParticles = particles;
	}
	
	public ArrayList<Particle> getParticles()
	{
		return myParticles;
	}
	
	public void addParticle(Particle particle)
	{
		myParticles.add(particle);
	}
	
	public void removeParticle(Particle particle)
	{
		myParticles.remove(particle);
	}
}
