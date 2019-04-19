package com.library.xml.bookRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;

public class StringBookConverter {
	@XmlElementWrapper(name = "bookMapWrapper")
	public List<StringBookMap> entry = 
		      new ArrayList<StringBookMap>();
		   
		   public List<StringBookMap> getMyMapType() {
			   return this.entry;
		   }
}
