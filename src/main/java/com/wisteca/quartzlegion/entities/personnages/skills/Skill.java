package com.wisteca.quartzlegion.entities.personnages.skills;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface de base des comp�tences, permet de regrouper les comp�tences d'habilit�s et de classe dans un seul type primitif.
 * Le syst�me de comp�tence est tir� d'Anarchy Online :p
 * @author Wisteca
 */

public interface Skill {
	
	/**
	 * @return une description de la comp�tence
	 */
	
	public String getDescription();
	
	/**
	 * @return le nom �crit proprement avec une majuscule de la comp�tence
	 */
	
	public String getCompleteName();
	
	/**
	 * @return Une liste de toutes les comp�tences d'habilit�s et de classe, permet de toutes les it�rer d'une seule boucle.
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
	 * @param str le nom de la comp�tence � r�cup�rer
	 * @return la comp�tence portant le nom pass� en param�tre
	 * @throws IllegalArgumentException si la comp�tence n'existe pas
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
	 * Les comp�tences de classe augmentent ou d�finissent les capacit�s d'un personnage (voir les descriptions des comp�tences), elles d�pendent des comp�tences d'habilit�es (voir tableau),
	 * par exemple si un joueur augmente sa comp�tence force de 100, toutes les comp�tences de classe qui d�pendent de force vont �tre augment�es, la vie totale du joueur sera augment�e de 10,
	 * sa parade de 20 etc... Les comp�tences de classe sont plus ou moins facile � augmenter (co�t en points de comp�tences) d'apr�s la classe du joueur, cela permet aux joueurs de telle ou
	 * telle classe de se sp�cialiser dans des domaines sp�cifiques.
	 * @author Wisteca
	 */
	
	public static enum ClasseSkill implements Skill {
		
		//		           Force   Inte.   Pr�s.   Sant�   Endu.   Agilit�    Type               description                                                                        
		REGEN_VIE         (0 ,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente la vitesse � laquelle votre vie r�g�n�re.",	       					"R�g�n�ration vie"			),
		REGEN_ENERGIE     (0 ,     0 ,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,	 "Augmente la vitesse � laquelle votre �nergie r�g�n�re.",						"R�g�n�ration �nergie"		),
		VIE_TOTALE        (10,     0 ,     0 ,     50,     0 ,     0 ,        SkillType.FORME ,  "Augmente votre taux de vie maximal.",  										"Vie totale"				),
		ENERGIE_TOTALE    (0 ,     10,     0 ,     0 ,     50,     0 ,        SkillType.FORME ,  "Augmente votre taux d'�nergie maximal.", 										"�nergie totale"			),
		EVADE             (0 ,     0 ,     20,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances d'�viter des projectiles.",  							"�vades"					),
		PARADE            (20,     0 ,     0 ,     0 ,     0 ,     50,        SkillType.FORME ,  "Augmente vos chances de d�vier des coups d'armes de m�l�es.", 				"Parades"					),
		RESIS_MAGIQUE     (0 ,     50,     0 ,     0 ,     0 ,     20,        SkillType.FORME ,  "Augmente vos chances d'�viter des pouvoirs magiques.",  						"R�sistance magique"		),
	
		CORDE			  (0 ,     20,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes � corde.",				"Corde"						),
		FEU				  (30,     0 ,     50,     0 ,     0 ,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes � feu.", 				"Feu"						),
		LAME			  (40,     20,     0 ,     10,     0 ,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des lames.",  						"Lames"						),
		ARME_LONGUE		  (40,     0 ,     20,     10,     0 ,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes longues.",				"Armes longues"				),
		ARME_LOURDE		  (60,     0 ,     0 ,     20,     0 ,     0 ,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez avec des armes lourdes.",				"Armes lourdes"				),
		DEMONIAQUE		  (20,     40,     0 ,     0 ,     10,     10,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez en pratiquant la magie d�moniaque.",	"Magie d�moniaque"			),
		ELEMENTAIRE		  (0 ,     60,     0 ,     0 ,     20,     0 ,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez en pratiquant la magie �l�mentaire.",	"Magie �l�mentaire"			),
		SANG			  (0 ,     50,     0 ,     20,     10,     0 ,        SkillType.COMBAT,  "Augmente les d�g�ts que vous infligez en pratiquant la magie du sang.", 		"Magie du sang"				),
	
		SORTS_HABILITE    (10,     10,     10,     10,     10,     10,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts d'habilit�s.", 						"Sorts d'habilit�"			),
		SORTS_FORME		  (0 ,     0 ,     0 ,     20,     20,     20,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts de formes.",  						"Sorts de forme"			),
		SORTS_COMBAT	  (20,     20,     20,     0 ,     0 ,     0 ,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts de combats.",  						"Sorts de combats"			),
		SORTS_SORTS		  (0 ,     40,     0 ,     0 ,     20,     0 ,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts.", 									"Sorts"						),
		SORTS_PROTECTION  (30,     0 ,     0 ,     20,     0 ,     10,        SkillType.SORTS ,  "Permet de vous �quiper de meilleurs sorts de protection.",			  		"Sorts de protection"		),
	
		VITESSE_MARCHE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de course.", 								 			"Vitesse de course"			),
		VITESSE_NAGE	  (10,     0 ,     0 ,     0 ,     10,     10,        SkillType.DIVERS,  "Augmente votre vitesse de nage.",									  			"Vitesse de nage"			),
		ARMURE			  (10,     0 ,     0 ,     10,     0 ,     10,        SkillType.DIVERS,  "Augmente votre protection et permet de vous �quiper de meilleures armures.", 	"Armure"					);
	
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
		 * R�cup�rer le pourcentage de d�pendance d'une comp�tence de classe par rapport � une comp�tence d'habilit�,
		 * @param skill la comp�tence d'habilit�
		 * @return le pourcentage de d�pendance
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
		 * @return le type de la comp�tence, l'endroit o� elle sera class�e dans un tableau par exemple
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
	 * Les comp�tences d'habilit�es sont les comp�tences de base d'un personnage, alors que les comp�tences de classe sont augmentables plus ou moins facilement en fonction de la classe du
	 * personnage, les comp�tences d'habilit�es sont plus ou moins facilement augmentable d'apr�s la race du personnage. Cela signifie qui si par exemple un joueur aimerait taper tr�s fort et
	 * s'orienter vers le DPS, il aura int�r�t � prendre par exemple la race Orc avec la classe Barbare, ce qui lui permettra d'augmenter tr�s facilement sa comp�tence de force --> augmentation
	 * automatique des comp�tences de classe Arme lourde et Arme longue + augmentation facile des ces comp�tences gr�ce � la classe Barbare.
	 * @author Wisteca
	 */
	
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
	
	/**
	 * Enum�ration des types de comp�tences de classe.
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
