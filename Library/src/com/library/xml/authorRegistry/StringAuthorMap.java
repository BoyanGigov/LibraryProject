package com.library.xml.authorRegistry;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import com.library.data.Author;

public class StringAuthorMap {

	@XmlAttribute
	public String key;

	@XmlElement(name = "authorObject")
	public Author value;

}
