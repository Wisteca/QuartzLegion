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
import com.wisteca.quartzlegion.data.Accessor.AccessorMode;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.AttackPouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.OverTimePouvoir;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.attack.AttackTest1;
import com.wisteca.quartzlegion.entities.personnages.combats.pouvoirs.overtime.OverTimeTest;
import com.wisteca.quartzlegion.utils.effects.Effect;
import com.wisteca.quartzlegion.utils.effects.SphereEffect;

/**
 * Classe static servant à récupérer quelques informations de base comme des chemins d'accès à des fichiers.
 * @author Wisteca
 */

public class Constants {
	
	/**
	 * Chemin vers le fichier des joueurs (si la config indique de stocker les joueurs dans un fichier).
	 */
	
	public final static String JOUEURS_FILE_PATH = getServerFolderPath() + "/joueurs.xml";
	
	/**
	 * Document XML contenant les joueurs.
	 */
	
	public final static Document JOUEURS_DOCUMENT = getOrCreateDocument(JOUEURS_FILE_PATH, "joueurs");
	
	/**
	 * Le chemin vers le fichier de configuration du plugin.
	 */
	
	public final static String CONFIGURATION_FILE_PATH = getServerFolderPath() + "/config.xml";
	
	/**
	 * Le document XML de configuration du plugin.
	 */
	
	public final static Document CONFIGURATION_DOCUMENT = getOrCreateDocument(CONFIGURATION_FILE_PATH, "configuration");
	
	
	/**
	 * Le chemin d'accès au fichier des pouvoirs.
	 */
	
	public final static String POUVOIRS_FILE_PATH = getServerFolderPath() + "/pouvoirs.xml";
	
	/**
	 * Le document xml contenant tous les pouvoirs.
	 */
	
	public final static Document POUVOIRS_DOCUMENT = getOrCreateDocument(POUVOIRS_FILE_PATH, "pouvoirs");
	
	/***********************************************************************/
	
	/**
	 * Le chemin d'accès au dossier des skins.
	 */
	
	public final static String SKINS_FILE_PATH = getServerFolderPath() + "/skins";
	
	/**
	 * La valeur maximal qu'une arme peut avoir en chances de critiques.
	 */
	
	public final static int MAX_CRITICAL_LUCK = 10_000;
	
	/**
	 * Définit si le plugin va utiliser la base de données pour sauvegarder les infos des joueurs ou alors un fichier xml.
	 */
	
	public final static AccessorMode ACCESSOR_MODE = AccessorMode.valueOf(((Element) CONFIGURATION_DOCUMENT.getElementsByTagName("config").item(0)).getAttribute("AccessorMode"));
	
	/***********************************************************************/
	
	/**
	 * Liste des pouvoirs d'attaques, itéré lors de la déserialization de pouvoir, Tous les pouvoirs d'attaques doivent être enregistrés dans cette liste !
	 */
	
	public final static ArrayList<Class<? extends AttackPouvoir>> ATTACK_POUVOIR_LIST = new ArrayList<>(Arrays.asList(AttackTest1.class));
	
	/**
	 * Liste des DOT, tous les DOT doivent être enregistrés dans cette liste sinon ils ne pourront pas être déserializé !
	 */
	
	public final static ArrayList<Class<? extends OverTimePouvoir>> OVERTIME_LIST = new ArrayList<>(Arrays.asList(OverTimeTest.class));
	
	/**
	 * Liste des effets, ils doivent tous être enregistrés dans cette liste pour pouvoir être désérialisé !
	 */
	
	public final static ArrayList<Class<? extends Effect>> EFFECTS_LIST = new ArrayList<>(Arrays.asList(SphereEffect.class));
	
	/**
	 * @return le chemin d'accès vers le dossier du serveur
	 */
	
	public static String getServerFolderPath()
	{
		return MainClass.getInstance().getDataFolder().getAbsolutePath().replace("plugins\\" + MainClass.getInstance().getName(), "").replace("plugins/" + MainClass.getInstance().getName(), "");
	}
	
	static // dossier des skins, fichier config,...
	{
		// dossier des skins
		File skins = new File(SKINS_FILE_PATH);
		if(skins.exists() == false)
			skins.mkdir();
	}
	
	private static Document getOrCreateDocument(String path, String rootElementName)
	{
		try {
			
			File file = new File(path);
			
			if(file.exists() == false)
			{
				try {
					
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = doc.createElement(rootElementName);
					doc.appendChild(root);
					
					if(rootElementName.equals("configuration")) // valeurs par défauts dans le config.xml
					{
						Element bdd = doc.createElement("BDD");
						root.appendChild(bdd);
						
						bdd.setAttribute("user", "userName");
						bdd.setAttribute("password", "password");
						
						Element config = doc.createElement("config");
						root.appendChild(config);
						
						config.setAttribute("AccessorMode", "BDD");
					}
					
					final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			        final DOMSource source = new DOMSource(doc);
			        final StreamResult sortie = new StreamResult(file);
			        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");         
			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			        transformer.transform(source, sortie);
				
				} catch(ParserConfigurationException | TransformerException ex) {
					ex.printStackTrace();
				}	
			}
			
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		
		} catch(SAXException | IOException | ParserConfigurationException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}
