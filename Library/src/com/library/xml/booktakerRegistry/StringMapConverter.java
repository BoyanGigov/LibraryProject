package com.library.xml.booktakerRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;

public class StringMapConverter {
	
	@XmlElementWrapper(name = "bookingMapWrapper")
	public List<StringMap> entry1 = 
		      new ArrayList<StringMap>();
		   
		   
		   public List<StringMap> getMyMapType() {
			   return this.entry1;
		   }
}
