package com.library.xml.authorRegistry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.library.data.Author;

public class StringAuthorAdapter extends

		XmlAdapter<StringAuthorConverter, Map<String, Author>> {

	@Override
	public StringAuthorConverter marshal(Map<String, Author> mapToConvert) throws Exception {
		StringAuthorConverter myMapType = new StringAuthorConverter();
		for (Entry<String, Author> entry : mapToConvert.entrySet()) {
			StringAuthorMap myMapEntryType = new StringAuthorMap();
			myMapEntryType.key = entry.getKey();
			myMapEntryType.value = entry.getValue();
			myMapType.entry.add(myMapEntryType);
		}
		return myMapType;
	}

	@Override
	public Map<String, Author> unmarshal(StringAuthorConverter mapConverted) throws Exception {
		Map<String, Author> hashMap = new TreeMap<String, Author>();
		for (StringAuthorMap myEntryType : mapConverted.entry) {
			hashMap.put(myEntryType.key, myEntryType.value);
		}
		return hashMap;
	}

}
