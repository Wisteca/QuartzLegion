package com.wisteca.quartzlegion.data;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.wisteca.quartzlegion.MainClass;

public class Constants {
	
	public final static String SKILLSBUFF_FILE_PATH = getServerFilePath() + "Buffs.xml";
	
	public final static String SKINS_FILE_PATH = getServerFilePath() + "skins";
	
	public static void init()
	{
		File buffsXml = new File(SKILLSBUFF_FILE_PATH);
		if(buffsXml.exists() == false)
		{
			try {
				
				final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		        final DOMSource source = new DOMSource(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		        final StreamResult sortie = new StreamResult(buffsXml);
		        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
		        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");         
		        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		        transformer.transform(source, sortie);
			
			} catch(ParserConfigurationException | TransformerException ex) {
				ex.printStackTrace();
			}
		}
		
		File skins = new File(SKINS_FILE_PATH);
		if(skins.exists() == false)
			skins.mkdir();
	}
	
	private static String getServerFilePath()
	{
		return MainClass.getInstance().getDataFolder().getAbsolutePath().replace("plugins\\" + MainClass.getInstance().getName(), "");
	}
}
