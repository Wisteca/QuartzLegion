package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.overtime;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.OverTimePouvoir;

public class OverTimeTest extends OverTimePouvoir {

	public OverTimeTest(Personnage perso)
	{
		super(perso, 100, 10, 5, "POUVOIR DE TEST", null);
	}

	@Override
	protected void action()
	{
		getTarget().sendMessage("LOL");
	}
}
