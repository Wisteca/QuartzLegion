package com.wisteca.quartzlegion.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.wisteca.quartzlegion.MainClass;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AttackPouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.OverTimePouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack.AttackTest1;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack.AttackTest2;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.overtime.OverTimeTest;

/**
 * Classe static servant à récupérer quelques informations de base comme des chemins d'accès à des fichiers.
 * @author Wisteca
 */

public class Constants {
	
	/**
	 * Le chemin d'accès au fichier des pouvoirs.
	 */
	
	public final static String POUVOIRS_FILE_PATH = getServerFolderPath() + "pouvoirs.xml";
	
	/**
	 * Le document xml contenant tous les pouvoirs.
	 */
	
	public final static Document POUVOIRS_DOCUMENT = getDocument();
	
	/**
	 * Le chemin d'accès au dossier des skins.
	 */
	
	public final static String SKINS_FILE_PATH = getServerFolderPath() + "/skins";
	
	/**
	 * La valeur maximal qu'une arme peut avoir en chances de critiques.
	 */
	
	public final static int MAX_CRITICAL_LUCK = 10_000;
	
	/**
	 * Liste des pouvoirs d'attaques, itéré lors de la déserialization de pouvoir, Tous les pouvoirs d'attaques doivent être enregistrés dans cette liste !
	 */
	
	public final static ArrayList<Class<? extends AttackPouvoir>> ATTACK_POUVOIR_LIST = new ArrayList<>(Arrays.asList(AttackTest1.class, AttackTest2.class));
	
	/**
	 * Liste des DOT, tous les DOT doivent être enregistrés dans cette liste sinon ils ne pourront pas être déserializé !
	 */
	
	public final static ArrayList<Class<? extends OverTimePouvoir>> OVERTIME_LIST = new ArrayList<>(Arrays.asList(OverTimeTest.class));
	
	/**
	 * Méthode appelée au démarrage du plugin pour effectuer divers opérations comme la mise en place des fichiers si ils sont inexistants.
	 */
	
	public static void init()
	{
		File skins = new File(SKINS_FILE_PATH);
		if(skins.exists() == false)
			skins.mkdir();
	}
	
	/**
	 * @return le chemin d'accès vers le dossier du serveur
	 */
	
	public static String getServerFolderPath()
	{
		return MainClass.getInstance().getDataFolder().getAbsolutePath().replace("plugins\\" + MainClass.getInstance().getName(), "");
	}
	
	private static Document getDocument()
	{
		try {
			
			File pouvoirFile = new File(POUVOIRS_FILE_PATH);
			if(pouvoirFile.exists() == false)
			{
				try {
					
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = doc.createElement("pouvoirs");
					doc.appendChild(root);
					
					final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			        final DOMSource source = new DOMSource(doc);
			        final StreamResult sortie = new StreamResult(pouvoirFile);
			        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");         
			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			        transformer.transform(source, sortie);
				
				} catch(ParserConfigurationException | TransformerException ex) {
					ex.printStackTrace();
				}	
			}
			
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pouvoirFile);
		
		} catch(SAXException | IOException | ParserConfigurationException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}
