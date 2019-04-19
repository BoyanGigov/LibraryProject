package com.library.xml.authorRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;

public class StringAuthorConverter {
	@XmlElementWrapper(name = "authorMapWrapper")
	public List<StringAuthorMap> entry = 
		      new ArrayList<StringAuthorMap>();
		   
		   public List<StringAuthorMap> getMyMapType() {
			   return this.entry;
		   }
}
