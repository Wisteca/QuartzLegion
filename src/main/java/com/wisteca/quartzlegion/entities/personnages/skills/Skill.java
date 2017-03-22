package com.wisteca.quartzlegion.entities.personnages.skills;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface de base des compétences, permet de regrouper les compétences d'habilités et de classe dans un seul type primitif.
 * Le systême de compétence est tiré d'Anarchy Online :p
 * @author Wisteca
 */

public interface Skill {
	
	/**
	 * @return une description de la compétence
	 */
	
	public String getDescription();
	
	/**
	 * @return le nom écrit proprement avec une majuscule de la compétence
	 */
	
	public String getCompleteName();
	
	/**
	 * @return Une liste de toutes les compétences d'habilités et de classe, permet de toutes les itérer d'une seule boucle.
	 */
	
	public static List<Skill> values()
	{
		List<Skill> allSkills = new ArrayList<Skill>();
		
		for(HabilitySkill s : HabilitySkill.values())
			allSkills.add(s);
		
		for(ClasseSkill s : ClasseSkill.values())
			allSkills.add(s);
		
		return allSkills;
	}
	
	/**
	 * @param str le nom de la compétence à récupérer
	 * @return la compétence portant le nom passé en paramètre
	 * @throws IllegalArgumentException si la compétence n'existe pas
	 */
	
	public static Skill valueOf(String str)
	{
		try {
			
			return ClasseSkill.valueOf(str);
			
		} catch(IllegalArgumentException ex) {
			
			return HabilitySkill.valueOf(str);
			
		}
	}
	
	/**
	 * Les compétences de classe augmentent ou définissent les capacités d'un personnage (voir les descriptions des compétences), elles dépendent des compétences d'habilitées (voir tableau),
	 * par exemple si un joueur augmente sa compétence force de 100, toutes les compétences de classe qui dépendent de force vont être augmentées, la vie totale du joueur sera augmentée de 10,
	 * sa parade de 20 etc... Les compétences de classe sont plus ou moins facile à augmenter (coût en points de compétences) d'après la classe du joueur, cela permet aux joueurs de telle ou
	 * telle classe de se spécialiser dans des domaines spécifiques.
	 * @author Wisteca
	 */
	
	public static enum ClasseSkill implements Skill {
		
		//		           Force   Inte.   Prés.   Santé   Endu.   Agilité    Type               description                                                                        
		REGEN_VIE         (0 ,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente la vitesse à laquelle votre vie régénère.",	       					"Régénération vie"			),
		REGEN_ENERGIE     (0 ,     0 ,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,	 "Augmente la vitesse à laquelle votre énergie régénère.",						"Régénération énergie"		),
		VIE_TOTALE        (10,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente votre taux de vie maximal.",  										"Vie totale"				),
		ENERGIE_TOTALE    (0 ,     10,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,  "Augmente votre taux d'énergie maximal.", 										"Énergie totale"			),
		EVADE             (0 ,     0 ,     20,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances d'éviter des projectiles.",  							"Évades"					),
		PARADE            (20,     0 ,     0 ,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances de dévier des coups d'armes de mélées.", 				"Parades"					),
		RESIS_MAGIQUE     (0 ,     50,     0 ,     0 ,     0 ,     20,        SkillType.FORME ,  "Augmente vos chances d'éviter des pouvoirs magiques.",  						"Résistance magique"		),
	
		CORDE			  (0 ,     20,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes à corde.",				"Corde"						),
		FEU				  (30,     0 ,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes à feu.", 				"Feu"						),
		LAME			  (40,     20,     0 ,     10,     0 ,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des lames.",  						"Lames"						),
		ARME_LONGUE		  (40,     0 ,     20,     10,     0 ,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes longues.",				"Armes longues"				),
		ARME_LOURDE		  (60,     0 ,     0 ,     20,     0 ,     0 ,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez avec des armes lourdes.",				"Armes lourdes"				),
		DEMONIAQUE		  (20,     40,     0 ,     0 ,     10,     10,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez en pratiquant la magie démoniaque.",	"Magie démoniaque"			),
		ELEMENTAIRE		  (0 ,     60,     0 ,     0 ,     20,     0 ,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez en pratiquant la magie élémentaire.",	"Magie élémentaire"			),
		SANG			  (0 ,     50,     0 ,     20,     10,     0 ,        SkillType.COMBAT,  "Augmente les dégâts que vous infligez en pratiquant la magie du sang.", 		"Magie du sang"				),
	
		SORTS_HABILITE    (10,     10,     10,     10,     10,     10,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts d'habilités.", 						"Sorts d'habilité"			),
		SORTS_FORME		  (0 ,     0 ,     0 ,     20,     20,     20,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts de formes.",  						"Sorts de forme"			),
		SORTS_COMBAT	  (20,     20,     20,     0 ,     0 ,     0 ,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts de combats.",  						"Sorts de combats"			),
		SORTS_SORTS		  (0 ,     40,     0 ,     0 ,     20,     0 ,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts.", 									"Sorts"						),
		SORTS_PROTECTION  (30,     0 ,     0 ,     20,     0 ,     10,        SkillType.SORTS ,  "Permet de vous équiper de meilleurs sorts de protection.",			  		"Sorts de protection"		),
	
		VITESSE_MARCHE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de course.", 								 			"Vitesse de course"			),
		VITESSE_NAGE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de nage.",									  			"Vitesse de nage"			),
		ARMURE			  (10,     0 ,     0 ,     10,     0 ,     10,        SkillType.DIVERS,  "Augmente votre protection et permet de vous équiper de meilleures armures.", 	"Armure"					);
	
		private int myForceDep, myIntelligenceDep, myPrecisionDep, mySanteDep, myEnduranceDep, myAgiliteDep;
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
		
		/**
		 * Récupérer le pourcentage de dépendance d'une compétence de classe par rapport à une compétence d'habilité,
		 * @param skill la compétence d'habilité
		 * @return le pourcentage de dépendance
		 */
		
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
		
		/**
		 * @return le type de la compétence, l'endroit où elle sera classée dans un tableau par exemple
		 */
		
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
	
	/**
	 * Les compétences d'habilitées sont les compétences de base d'un personnage, alors que les compétences de classe sont augmentables plus ou moins facilement en fonction de la classe du
	 * personnage, les compétences d'habilitées sont plus ou moins facilement augmentable d'après la race du personnage. Cela signifie qui si par exemple un joueur aimerait taper très fort et
	 * s'orienter vers le DPS, il aura intérêt à prendre par exemple la race Orc avec la classe Barbare, ce qui lui permettra d'augmenter très facilement sa compétence de force --> augmentation
	 * automatique des compétences de classe Arme lourde et Arme longue + augmentation facile des ces compétences grâce à la classe Barbare.
	 * @author Wisteca
	 */
	
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
	
	/**
	 * Enumération des types de compétences de classe.
	 * @author Wisteca
	 */
	
	public static enum SkillType
	{
		FORME,
		COMBAT,
		SORTS,
		DIVERS;
	}
}
