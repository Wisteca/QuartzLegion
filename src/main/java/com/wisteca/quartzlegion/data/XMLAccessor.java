package com.wisteca.quartzlegion.data;

import java.io.File;
import java.util.UUID;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe qui a le même comportement que la BDDAccessor mais qui sauvegarde les joueurs dans un fichier, pour faire des tests locals.
 * @author Wisteca
 */

public class XMLAccessor implements Accessor {
	
	@Override
	public boolean isCreated(UUID uuid)
	{
		return getXML(uuid) != null;
	}
	
	@Override
	public void create(UUID uuid, Element element)
	{
		Element copy = (Element) element.cloneNode(true);
		Constants.JOUEURS_DOCUMENT.adoptNode(copy);
		Constants.JOUEURS_DOCUMENT.getDocumentElement().appendChild(copy);
		saveJoueurs();
	}
	
	@Override
	public void setXML(UUID uuid, Element element)
	{
		Element copy = (Element) element.cloneNode(true);
		Constants.JOUEURS_DOCUMENT.adoptNode(copy);
		Constants.JOUEURS_DOCUMENT.getDocumentElement().replaceChild(copy, getXML(uuid));
		saveJoueurs(); // sauvegarde du fichier à chaque changements, pour les tests
	}
	
	@Override
	public Element getXML(UUID uuid)
	{
		NodeList list = Constants.JOUEURS_DOCUMENT.getDocumentElement().getChildNodes();
		
		for(int i = 0; i < list.getLength(); i++)
		{
			Node node = list.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			Element element = (Element) node;
			if(element.getAttribute("uuid").equals(uuid.toString()))
				return element;
		}
		
		return null;
	}
	
	private void saveJoueurs()
	{
		try {
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        DOMSource source = new DOMSource(Constants.JOUEURS_DOCUMENT);
	        StreamResult sortie = new StreamResult(new File(Constants.JOUEURS_FILE_PATH));
	        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");         
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        transformer.transform(source, sortie);
			
		} catch(TransformerFactoryConfigurationError | TransformerException ex) {
			ex.printStackTrace();
		}
	}
}
