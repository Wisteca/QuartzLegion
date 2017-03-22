package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.overtime;

import com.wisteca.quartzlegion.entities.personnages.Personnage;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.OverTimePouvoir;
import com.wisteca.quartzlegion.utils.effects.Effect;
import com.wisteca.quartzlegion.utils.effects.EffectInterface.Part;

public class OverTimeTest extends OverTimePouvoir {

	public OverTimeTest(Personnage perso)
	{
		super(perso, 100, 10, 5, "POUVOIR DE TEST", Effect.getEffectByName("Gris scintillant"), Part.HEAD, 10);
	}

	@Override
	protected void action()
	{
		getTarget().sendMessage("LOL " + getRemainingTime());
	}
}
