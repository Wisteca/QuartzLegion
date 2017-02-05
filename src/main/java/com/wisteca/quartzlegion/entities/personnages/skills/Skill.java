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
		
		//		           Force   Inte.   Prés.   Santé   Endu.   Agilité    Type               description                                                                        
		REGEN_VIE         (0 ,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente la vitesse à laquelle votre vie régénère.",	       					"Régénération vie"			),
		REGEN_ENERGIE     (0 ,     0 ,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,	 "Augmente la vitesse à laquelle votre énergie régénère.",						"Régénération énergie"		),
		VIE_TOTALE        (10,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente votre taux de vie maximal.",  										"Vie totale"				),
		ENERGIE_TOTALE    (0 ,     10,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,  "Augmente votre taux d'énergie maximal.", 										"Énergie totale"			),
		EVADE             (0 ,     0 ,     20,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances d'éviter des projectiles.",  							"Évades"					),
		PARADE            (20,     0 ,     0 ,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances de dévier des coups d'armes de mélées.", 				"Parades"					),
		RESIS_MAGIQUE     (0 ,     50,     0 ,     0 ,     0 ,     20,        SkillType.FORME ,  "Augmente vos chances d'éviter des pouvoirs magiques.",  						"Résistance magique"		),
	
		CORDE			  (10,     10,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes à corde.",				"Corde"						),
		FEU				  (0 ,     20,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes à feu.", 				"Feu"						),
		LAME			  (40,     10,     0 ,     10,     0 ,     20,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des lames.",  						"Lames"						),
		ARME_LONGUE		  (50,     50,     0 ,     10,     0 ,     20,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes longues.",				"Armes longues"				),
		ARME_LOURDE		  (60,     50,     0 ,     0 ,     0 ,     20,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes lourdes.",				"Armes lourdes"				),
		DEMONIAQUE		  (20,     40,     0 ,     0 ,     10,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez en pratiquant la magie démoniaque.",	"Magie démoniaque"			),
		ELEMENTAIRE		  (0 ,     60,     0 ,     0 ,     20,     0 ,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez en pratiquant la magie élémentaire.",	"Magie élémentaire"			),
		SANG			  (0 ,     50,     0 ,     20,     10,     0 ,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez en pratiquant la magie du sang.", 		"Magie du sang"				),
	
		SORTS_HABILITE    (10,     10,     10,     10,     10,     10,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts d'habilités.", 						"Sorts d'habilité"			),
		SORTS_FORME		  (0 ,     0 ,     0 ,     20,     20,     20,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts de formes.",  						"Sorts de forme"			),
		SORTS_COMBAT	  (20,     20,     20,     0 ,     0 ,     0 ,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts de combats.",  						"Sorts de combats"			),
		SORTS_SORTS		  (0 ,     40,     0 ,     0 ,     20,     0 ,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts.", 									"Sorts"						),
		SORTS_PROTECTION  (20,     50,     0 ,     20,     0 ,     20,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts de protection.",			  		"Sorts de protection"		),
	
		VITESSE_MARCHE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de course.", 								 			"Vitesse de course"			),
		VITESSE_NAGE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de nage.",									  			"Vitesse de nage"			),
		ARMURE			  (10,     0 ,     0 ,     10,     0 ,     10,        SkillType.DIVERS,  "Augmente votre protection et permet de vous équiper de meilleures armures.", 	"Armure"					);
	
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
		FORCE       	("Augmente votre force, vous permet de taper plus fort avec des armes de mélées, lames,...",                "Force"			),
		INTELLIGENCE	("Augmente votre intelligence, permet d'infliger plus de dégâts avec vos pouvoirs magiques.",				"Intelligence"  ),
		PRECISION   	("Augmente votre précision au tir, permet de mieux viser votre cible et de lui infliger plus de dégâts.",	"Précision"		),
		SANTE			("Augmente toutes les compétences en rapport avec votre vie.",												"Santé"			),
		ENDURANCE		("Augmente toutes les compétences en rapport avec votre énergie.",											"Endurance"		),
		AGILITE			("Augmente votre facilité à éviter les coups.",																"Agilité"		);
		
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
