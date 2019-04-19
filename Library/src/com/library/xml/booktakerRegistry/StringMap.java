package com.library.xml.booktakerRegistry;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;



public class StringMap {
	
	@XmlAttribute
	public String key;

	@XmlElement(name = "StringMapObject")
	public Map<String, StringPQueueMap> value;
}
