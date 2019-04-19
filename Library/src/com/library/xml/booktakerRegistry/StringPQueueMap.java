package com.library.xml.booktakerRegistry;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class StringPQueueMap {
	
	@XmlAttribute
	public String key;
	
	@XmlElement(name = "StringPQueueMapObject")
	public String[] value;
	
}
