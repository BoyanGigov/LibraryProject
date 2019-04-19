package com.library.xml.userRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringStringAdapter extends

		XmlAdapter<StringStringMapConverter, Map<String, String>> {

	// For passwordRegistry

	@Override
	public StringStringMapConverter marshal(Map<String, String> mapToConvert) throws Exception {
		StringStringMapConverter myMapType = new StringStringMapConverter();
		for (Entry<String, String> entry : mapToConvert.entrySet()) {
			StringStringMap myMapEntryType = new StringStringMap();
			myMapEntryType.key = entry.getKey();
			myMapEntryType.value = entry.getValue();
			myMapType.entry.add(myMapEntryType);
		}
		return myMapType;
	}

	@Override
	public Map<String, String> unmarshal(StringStringMapConverter mapConverted) throws Exception {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (StringStringMap myEntryType : mapConverted.entry) {
			hashMap.put(myEntryType.key, myEntryType.value);
		}
		return hashMap;
	}

}
