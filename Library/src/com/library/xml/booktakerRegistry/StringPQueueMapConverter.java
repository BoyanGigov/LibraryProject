package com.library.xml.booktakerRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;

public class StringPQueueMapConverter {
	
	@XmlElementWrapper(name = "StringPQueueMapWrapper")
	public List<StringPQueueMap> entry2 = 
		      new ArrayList<StringPQueueMap>();
	//public List<DateTime> entry = PriorityQueue<DatimeTime>;
		   
		   
		   public List<StringPQueueMap> getMyMapType() {
			   return this.entry2;
		   }
}
