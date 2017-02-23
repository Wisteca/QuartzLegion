package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.overtime;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.OverTimePouvoir;

public class OverTimeTest extends OverTimePouvoir {

	public OverTimeTest(Personnage perso)
	{
		super(perso, 100, 10, 10, "le pouvoir de test !", null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void action()
	{
		// TODO Auto-generated method stub
		
	}
}
