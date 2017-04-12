package com.wisteca.quartzlegion.utils.effects;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Fais appara�tre une sph�re autour de la position donn�e.
 * @author Wisteca
 */

public class SphereEffect extends AOEffect {
	
	// Valeurs par d�fauts
	private double myExtension, myCurrentExtension, myIncrease;
	private int myCounts, myCurrentCounts, myLayers, myParticlesPerLayer, myCurrentTime = 0, myBetweenTime;
	private Location myLocation;
	private boolean myIsRunning = false;
	
	/**
	 * Construire l'effet en pr�cisant chaque attributs.
	 * @param name le nom de l'effet
	 * @param particles les particules qui appara�tront al�atoirement
	 * @param extension le rayon de la sph�re de base
	 * @param increase l'expansion de la sph�re
	 * @param counts le nombre d'expansion
	 * @param layers le nombre de couches de particules qui forment la sph�re
	 * @param particlesPerLayer le nombre de particules par couche
	 * @param betweenTime le temps en ticks entre chaque counts (expansion)
	 */
	
	public SphereEffect(String name, ArrayList<Particle> particles, double extension, double increase, int counts, int layers, int particlesPerLayer, int betweenTime)
	{
		super(name, particles);
		myExtension = extension;
		myIncrease = increase;
		myCounts = counts;
		myLayers = layers;
		myParticlesPerLayer = particlesPerLayer;
		myBetweenTime = betweenTime;
	}
	
	/**
	 * Construire l'effet en le d�s�rialisant.
	 * @param element l'�l�ment dans lequel l'effet � �t� s�rialis� auparavant
	 */
	
	public SphereEffect(Element element)
	{
		super(element);
	}
	
	@Override
	public void serialize(Element toWrite)
	{
		super.serialize(toWrite);

		Element effect = null;
		NodeList list = toWrite.getElementsByTagName("AOEffect");
		for(int i = 0 ; i < list.getLength() ; i++)
			if(list.item(i).getNodeType() == Node.ELEMENT_NODE && ((Element) list.item(i)).getAttribute("name").equals(getName()))
				effect = (Element) list.item(i);
		
		effect.setAttribute("extension", Double.toString(myExtension));
		effect.setAttribute("increase", Double.toString(myIncrease));
		effect.setAttribute("counts", Integer.toString(myCounts));
		effect.setAttribute("layers", Integer.toString(myLayers));
		effect.setAttribute("particlesPerLayer", Integer.toString(myParticlesPerLayer));
		effect.setAttribute("betweenTime", Integer.toString(myBetweenTime));
	}

	@Override
	public void deserialize(Element element)
	{
		super.deserialize(element);
		myExtension = Double.valueOf(element.getAttribute("extension"));
		myIncrease = Double.valueOf(element.getAttribute("increase"));
		myCounts = Integer.valueOf(element.getAttribute("counts"));
		myLayers = Integer.valueOf(element.getAttribute("layers"));
		myParticlesPerLayer = Integer.valueOf(element.getAttribute("particlesPerLayer"));
		myBetweenTime = Integer.valueOf(element.getAttribute("betweenTime"));
	}
	
	/**
	 * @param extension le rayon (en bloc) de la sph�re
	 */
	
	public void setExtension(double extension)
	{
		myExtension = extension;
	}
	
	/**
	 * @return le rayon de la sph�re
	 */
	
	public double getExtension()
	{
		return myExtension;
	}
	
	/**
	 * @param counts le nombre de fois ou la sph�re se r�affiche
	 */
	
	public void setCounts(int counts)
	{
		myCounts = counts; 
	}
	
	/**
	 * @return le nombre de fois ou la sph�re se r�affiche
	 */
	
	public int getCounts()
	{
		return myCounts;
	}
	
	/**
	 * @param layers le nombre de couche que la sph�re affiche
	 */
	
	public void setLayers(int layers)
	{
		myLayers = layers;
	}
	
	/**
	 * @return le nombre de couche que la sph�re affiche
	 */
	
	public int getLayers()
	{
		return myLayers;
	}
	
	/**
	 * @param particles le nombre de particules qui s'afficheront � chaque couches
	 */
	
	public void setParticlesPerLayer(int particles)
	{
		myParticlesPerLayer = particles;
	}
	
	/**
	 * @return le nombre de particules qui s'afficheront � chaque couches
	 */
	
	public int getParticlesPerLayer()
	{
		return myParticlesPerLayer;
	}
	
	/**
	 * @param increase l'augmentation (en bloc) que la sph�re gagnera � chaque count
	 */
	
	public void setIncrease(double increase)
	{
		myIncrease = increase;
	}
	
	/**
	 * @return l'augmentation (en bloc) que la sph�re gagnera � chaque count
	 */
	
	public double getIncrease()
	{
		return myIncrease;
	}
	
	/**
	 * @param ticks le temps entre chaque counts
	 */
	
	public void setBetweenTime(int ticks)
	{
		myBetweenTime = ticks;
	}
	
	/**
	 * @return le temps entre chaque counts
	 */
	
	public int getBetweenTime()
	{
		return myBetweenTime;
	}
	
	/**
	 * La sph�re se r�pand � la position pass�e en param�tre de la m�thode launch(), sert � changer cette position si par exemple, la sph�re doit suivre un joueur.
	 * @param newLoc la nouvelle position o� la sph�re continue son expansion
	 */
	
	@Override
	public void updateLocation(Location newLoc)
	{
		myLocation = newLoc;
	}
	
	/**
	 * Fais appara�tre une sph�re � la position choisit.
	 */
	
	@Override
	public void launch(Location center)
	{
		if(myIsRunning == false)
		{
			myIsRunning = true;
			myCurrentExtension = myExtension;
			myCurrentCounts = myCounts;
		}
		
		myLocation = center;
		Random rand = new Random();
		//     longueur entre chaque couche            degr�s entre chaque particules
		double layerPlus = myCurrentExtension * 2 / myLayers, particlesPlus = 360D / myParticlesPerLayer, rayon, x, z;
		for(double layer = -myCurrentExtension ; layer < myCurrentExtension ; layer += layerPlus) // en sachant que le point y = 0 est le centre de la sph�re, y - extension est le dessous
		{   																		// de la sph�re, on fait des couches de particules de ce point jusqu'� y + extension qui est
																					// le dessus de la sph�re. 
			rayon = Math.sqrt(Math.pow(myCurrentExtension, 2) - Math.pow(layer, 2)); // chaque couche est un cercle, on cherche le rayon du cercle avec Pythagore       
			for(double degre = 0 ; degre < 360 ; degre += particlesPlus) // on parcourt le tour du cercle
			{
				x = center.getX() + rayon * Math.cos(Math.toRadians(degre)); // on cherche la position x par rapport � la position de centre
				z = center.getZ() + rayon * Math.sin(Math.toRadians(degre)); // on cherche la position y par rapport � la position de centre
				// on affiche une particule � la position trouv�e
				center.getWorld().spawnParticle(getParticles().get(rand.nextInt(getParticles().size())), new Location(center.getWorld(), x, center.getY() + layer, z), 1);
			}
		}
	}
	
	@Override
	public void doTime()
	{
		if(myIsRunning == false)
			return;
		
		if(myCurrentTime == 0)
		{
			if(myCurrentCounts == 0)
			{
				myCurrentCounts = myCounts;
				myIsRunning = false;
				return;
			}
			
			myCurrentExtension += myIncrease;
			myCurrentCounts--;
			myCurrentTime = myBetweenTime;
			launch(myLocation);
		}
		
		myCurrentTime--;
	}
}
