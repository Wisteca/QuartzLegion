package com.wisteca.quartzlegion.utils.effects;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.bukkit.Particle;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wisteca.quartzlegion.utils.Utils;

/**
 * Anarchy Online Effects, superclass repr�sentant quelques effets tir�s d'Anarchy Online.
 * @author Wisteca
 */

public abstract class AOEffect implements Effect {
	
	private String myName;
	private ArrayList<Particle> myParticles = new ArrayList<>();
	
	/**
	 * Construire un effet en pr�cisant chaque attributs.
	 * @param name le nom de l'effet
	 * @param particles la liste des particules qui appara�tront al�atoirement
	 */
	
	public AOEffect(String name, ArrayList<Particle> particles)
	{
		myName = name;
		myParticles = new ArrayList<>(particles);
	}
	
	/**
	 * Construire un effet en le d�s�rialisant.
	 * @param element l'�l�ment dans lequel l'effet � �t� s�rialis� auparavant
	 */
	
	public AOEffect(Element element)
	{
		deserialize(element);
	}
	
	@Override
	public void serialize(Element toWrite) throws ParserConfigurationException
	{
		Utils.removeElementsWhoHasAttribute(toWrite, "name", myName);
		Element effect = toWrite.getOwnerDocument().createElement("AOEffect");
		toWrite.appendChild(effect);
		
		effect.setAttribute("name", myName);
		
		Element particles = toWrite.getOwnerDocument().createElement("particles");
		effect.appendChild(particles);
		for(Particle pa : myParticles)
			particles.appendChild(toWrite.getOwnerDocument().createElement(pa.toString()));
	}
	
	@Override
	public void deserialize(Element element)
	{
		myName = element.getAttribute("name");
		
		myParticles = new ArrayList<>();
		NodeList list = element.getElementsByTagName("particles").item(0).getChildNodes();
		for(int i = 0 ; i < list.getLength() ; i++)
		{
			if(list.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			myParticles.add(Particle.valueOf(list.item(i).getNodeName()));
		}
	}
	
	@Override
	public String getName()
	{
		return myName;
	}
	
	/**
	 * Les particules qui appara�tront al�atoirement.
	 * @param particles une liste de particules
	 */
	
	public void setParticles(ArrayList<Particle> particles)
	{
		myParticles = new ArrayList<>(particles);
	}
	
	/**
	 * @return les particules de l'effet
	 */
	
	public ArrayList<Particle> getParticles()
	{
		return myParticles;
	}
	
	/**
	 * @param particle la particule � ajouter � la liste
	 */
	
	public void addParticle(Particle particle)
	{
		myParticles.add(particle);
	}
	
	/**
	 * @param particle la particule � supprimer de la liste
	 */
	
	public void removeParticle(Particle particle)
	{
		myParticles.remove(particle);
	}
}
