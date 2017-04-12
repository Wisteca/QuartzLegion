package com.wisteca.quartzlegion.world.objects;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * Représente un panneau protégé.
 * @author Wisteca
 */

public class ProtectedSign extends ProtectedBlockState implements Sign {

	public ProtectedSign(Block block)
	{
		super(block);
	}

	@Override
	public String getLine(int line) throws IndexOutOfBoundsException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getLines()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLine(int arg0, String arg1) throws IndexOutOfBoundsException
	{
		// TODO Auto-generated method stub
		
	}
	
}
