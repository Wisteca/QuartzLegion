package com.wisteca.quartzlegion.data;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

public interface Serializer {
	
	public void serialize(Element toWrite) throws ParserConfigurationException;
	
	public void deserialize(Element element);
	
}
