package com.library.xml.userRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;

public class StringStringMapConverter {

	// For passwordRegistry
	@XmlElementWrapper(name = "passMapWrapper")
	public List<StringStringMap> entry = 
		      new ArrayList<StringStringMap>();
		   
		   
		   public List<StringStringMap> getMyMapType() {
			   return this.entry;
		   }
}
