package com.library.xml.userRegistry;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.library.data.User;


public class StringUserMap {
	
	@XmlAttribute
	   public String key; 
	 
	@XmlElement
	   public User value;
	
}
