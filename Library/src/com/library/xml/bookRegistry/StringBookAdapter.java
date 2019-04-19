package com.library.xml.bookRegistry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.library.data.Book;

public class StringBookAdapter extends

		XmlAdapter<StringBookConverter, Map<String, Book>> {

	@Override
	public StringBookConverter marshal(Map<String, Book> mapToConvert) throws Exception {
		StringBookConverter myMapType = new StringBookConverter();
		for (Entry<String, Book> entry : mapToConvert.entrySet()) {
			StringBookMap myMapEntryType = new StringBookMap();
			myMapEntryType.key = entry.getKey();
			myMapEntryType.value = entry.getValue();
			myMapType.entry.add(myMapEntryType);
		}
		return myMapType;
	}

	@Override
	public Map<String, Book> unmarshal(StringBookConverter mapConverted) throws Exception {
		Map<String, Book> hashMap = new TreeMap<String, Book>();
		for (StringBookMap myEntryType : mapConverted.entry) {
			hashMap.put(myEntryType.key, myEntryType.value);
		}
		return hashMap;
	}

}
