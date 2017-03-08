package com.wisteca.quartzlegion.data;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.wisteca.quartzlegion.MainClass;

/**
 * Sauvegarde les joueurs dans la BDD.
 * @author Wisteca
 */

public class BDDAccessor implements Accessor, Listener {

	private static Connection myBDDConnection;
	
	static
	{
		try {
			
			// connexion à la BDD
			Element bddConfig = (Element) Constants.CONFIGURATION_DOCUMENT.getDocumentElement().getElementsByTagName("BDD").item(0);
			myBDDConnection = DriverManager.getConnection("jdbc:mysql://localhost/QuartzLegion", bddConfig.getAttribute("user"), bddConfig.getAttribute("password"));
			MainClass.getInstance().getLogger().info("Connexion à la base de données réussie.");
			
		} catch(SQLException ex) {
			MainClass.getInstance().getLogger().warning("Connexion à la base de données impossible, config.xml non configuré ?");
			ex.printStackTrace();
		}
	}
	
	public BDDAccessor()
	{
		Bukkit.getPluginManager().registerEvents(this, MainClass.getInstance());
	}
	
	@Override
	public boolean isCreated(UUID uuid)
	{
		try {
			
			Statement state = myBDDConnection.createStatement();
			ResultSet result = state.executeQuery("SELECT uuid FROM Joueurs WHERE uuid='" + uuid.toString() + "'");
			return result.first();
			
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public void create(UUID uuid, Element element)
	{
		if(isCreated(uuid))
			return;
		
		try {
			
			Statement state = myBDDConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			state.executeUpdate("INSERT INTO Joueurs VALUES ('" + uuid.toString() + "', '" + getStringOfElement(element) + "')");
			
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void setXML(UUID uuid, Element element)
	{
		try {
			
			Statement state = myBDDConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			state.executeUpdate("UPDATE Joueurs SET xml='" + getStringOfElement(element) + "' WHERE uuid='" + uuid.toString() + "'");
		
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public Element getXML(UUID uuid)
	{
		try {
			
			Statement state = myBDDConnection.createStatement();
			ResultSet result = state.executeQuery("SELECT xml FROM Joueurs WHERE uuid='" + uuid.toString() + "'");
			
			result.first();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(result.getString("xml"))));
			return doc.getDocumentElement();
			
		} catch(SQLException | SAXException | IOException | ParserConfigurationException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private String getStringOfElement(Element element)
	{
		try	{
			
			DOMSource domSource = new DOMSource(element);
		    StringWriter writer = new StringWriter();
		    StreamResult result = new StreamResult(writer);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.transform(domSource, result);
		    return writer.toString();
		
		} catch(TransformerFactoryConfigurationError | TransformerException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	@EventHandler
	public void onDisable(PluginDisableEvent e)
	{
		try {
			
			myBDDConnection.close();
		
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}
