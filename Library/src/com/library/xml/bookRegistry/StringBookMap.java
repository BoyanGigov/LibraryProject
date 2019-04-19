package com.library.xml.bookRegistry;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import com.library.data.Book;

public class StringBookMap {

	@XmlAttribute
	public String key;

	@XmlElement(name = "book")
	public Book value;

}
