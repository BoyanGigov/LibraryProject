package com.library.xml.userRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;

public class StringUserMapConverter {
	
	 @XmlElementWrapper(name = "userMapEntry")
	public List<StringUserMap> entry = 
		      new ArrayList<StringUserMap>();
		   
		   
		   public List<StringUserMap> getMyMapType() {
			   return this.entry;
		   }
			   
}
