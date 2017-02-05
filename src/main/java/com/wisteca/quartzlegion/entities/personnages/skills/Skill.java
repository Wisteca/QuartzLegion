package com.wisteca.quartzlegion.entities.personnages.skills;

import java.util.ArrayList;
import java.util.List;

public interface Skill {
	
	public String getDescription();
	public String getCompleteName();
	
	public static List<Skill> values()
	{
		List<Skill> allSkills = new ArrayList<Skill>();
		
		for(HabilitySkill s : HabilitySkill.values())
			allSkills.add(s);
		
		for(ClasseSkill s : ClasseSkill.values())
			allSkills.add(s);
		
		return allSkills;
	}
	
	public static enum ClasseSkill implements Skill {
		
		//		           Force   Inte.   Pr�s.   Sant�   Endu.   Agilit�    Type               description                                                                        
		REGEN_VIE         (0 ,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente la vitesse � laquelle votre vie r�g�n�re.",	       					"R�g�n�ration vie"			),
		REGEN_ENERGIE     (0 ,     0 ,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,	 "Augmente la vitesse � laquelle votre �nergie r�g�n�re.",						"R�g�n�ration �nergie"		),
		VIE_TOTALE        (10,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente votre taux de vie maximal.",  										"Vie totale"				),
		ENERGIE_TOTALE    (0 ,     10,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,  "Augmente votre taux d'�nergie maximal.", 										"�nergie totale"			),
		EVADE             (0 ,     0 ,     20,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances d'�viter des projectiles.",  							"�vades"					),
		PARADE            (20,     0 ,     0 ,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances de d�vier des coups d'armes de m�l�es.", 				"Parades"					),
		RESIS_MAGIQUE     (0 ,     50,     0 ,     0 ,     0 ,     20,        SkillType.FORME ,  "Augmente vos chances d'�viter des pouvoirs magiques.",  						"R�sistance magique"		),
	
		CORDE			  (10,     10,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes � corde.",				"Corde"						),
		FEU				  (0 ,     20,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes � feu.", 				"Feu"						),
		LAME			  (40,     10,     0 ,     10,     0 ,     20,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des lames.",  						"Lames"						),
		ARME_LONGUE		  (50,     50,     0 ,     10,     0 ,     20,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes longues.",				"Armes longues"				),
		ARME_LOURDE		  (60,     50,     0 ,     0 ,     0 ,     20,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes lourdes.",				"Armes lourdes"				),
		DEMONIAQUE		  (20,     40,     0 ,     0 ,     10,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez en pratiquant la magie d�moniaque.",	"Magie d�moniaque"			),
		ELEMENTAIRE		  (0 ,     60,     0 ,     0 ,     20,     0 ,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez en pratiquant la magie �l�mentaire.",	"Magie �l�mentaire"			),
		SANG			  (0 ,     50,     0 ,     20,     10,     0 ,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez en pratiquant la magie du sang.", 		"Magie du sang"				),
	
		SORTS_HABILITE    (10,     10,     10,     10,     10,     10,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts d'habilit�s.", 						"Sorts d'habilit�"			),
		SORTS_FORME		  (0 ,     0 ,     0 ,     20,     20,     20,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts de formes.",  						"Sorts de forme"			),
		SORTS_COMBAT	  (20,     20,     20,     0 ,     0 ,     0 ,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts de combats.",  						"Sorts de combats"			),
		SORTS_SORTS		  (0 ,     40,     0 ,     0 ,     20,     0 ,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts.", 									"Sorts"						),
		SORTS_PROTECTION  (20,     50,     0 ,     20,     0 ,     20,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts de protection.",			  		"Sorts de protection"		),
	
		VITESSE_MARCHE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de course.", 								 			"Vitesse de course"			),
		VITESSE_NAGE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de nage.",									  			"Vitesse de nage"			),
		ARMURE			  (10,     0 ,     0 ,     10,     0 ,     10,        SkillType.DIVERS,  "Augmente votre protection et permet de vous �quiper de meilleures armures.", 	"Armure"					);
	
		private int myForceDep, myIntelligenceDep, myPrecisionDep, mySanteDep,
				myEnduranceDep, myAgiliteDep;
		private SkillType myType;
		private String myDescription, myCompleteName;

		ClasseSkill(int forceDep, int intelligenceDep, int precisionDep, int santeDep, int enduranceDep, int agiliteDep, SkillType type, String description, String completeName)
		{
			myForceDep = forceDep;
			myIntelligenceDep = intelligenceDep;
			myPrecisionDep = precisionDep;
			mySanteDep = santeDep;
			myEnduranceDep = enduranceDep;
			myAgiliteDep = agiliteDep;

			myType = type;
			myDescription = description;
			myCompleteName = completeName;
		}

		public int getDependencie(HabilitySkill skill)
		{
			switch(skill)
			{
			case AGILITE:
				return myAgiliteDep;
			
			case ENDURANCE:
				return myEnduranceDep;
			
			case FORCE:
				return myForceDep;
			
			case INTELLIGENCE:
				return myIntelligenceDep;
			
			case PRECISION:
				return myPrecisionDep;
			
			case SANTE:
				return mySanteDep;
			}
			
			return 0;
		}
		
		public SkillType getType()
		{
			return myType;
		}
		
		@Override
		public String getDescription()
		{
			return myDescription;
		}
		
		@Override
		public String getCompleteName()
		{
			return myCompleteName;
		}
	}

	public static enum HabilitySkill implements Skill {
		
		//              description
		FORCE       	("Augmente votre force, vous permet de taper plus fort avec des armes de m�l�es, lames,...",                "Force"			),
		INTELLIGENCE	("Augmente votre intelligence, permet d'infliger plus de d�g�ts avec vos pouvoirs magiques.",				"Intelligence"  ),
		PRECISION   	("Augmente votre pr�cision au tir, permet de mieux viser votre cible et de lui infliger plus de d�g�ts.",	"Pr�cision"		),
		SANTE			("Augmente toutes les comp�tences en rapport avec votre vie.",												"Sant�"			),
		ENDURANCE		("Augmente toutes les comp�tences en rapport avec votre �nergie.",											"Endurance"		),
		AGILITE			("Augmente votre facilit� � �viter les coups.",																"Agilit�"		);
		
		private String myDescription, myCompleteName;
		
		private HabilitySkill(String description, String completeName) 
		{
			myDescription = description;
			myCompleteName = completeName;
		}

		@Override
		public String getDescription()
		{
			return myDescription;
		}
		
		@Override
		public String getCompleteName()
		{
			return myCompleteName;
		}
	}

	public enum SkillType
	{
		FORME,
		COMBAT,
		SORTS,
		DIVERS;
	}
}
