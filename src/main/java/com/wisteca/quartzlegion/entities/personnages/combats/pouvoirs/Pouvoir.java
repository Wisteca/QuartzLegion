package com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs;

import com.wisteca.quartzlegion.data.Serializer;

public interface Pouvoir extends Serializer {
	
	public boolean launch();
	
	public void doTime();
	
}
