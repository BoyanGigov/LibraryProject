package com.library.xml.userRegistry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.library.data.User;

public class StringUserAdapter extends

		XmlAdapter<StringUserMapConverter, Map<String, User>> {

	@Override
	public StringUserMapConverter marshal(Map<String, User> mapToConvert) throws Exception {
		StringUserMapConverter myMapType = new StringUserMapConverter();
		for (Entry<String, User> entry : mapToConvert.entrySet()) {
			StringUserMap myMapEntryType = new StringUserMap();
			myMapEntryType.key = entry.getKey();
			myMapEntryType.value = entry.getValue();
			myMapType.entry.add(myMapEntryType);
		}
		return myMapType;
	}

	@Override
	public Map<String, User> unmarshal(StringUserMapConverter mapConverted) throws Exception {
		Map<String, User> hashMap = new TreeMap<String, User>();
		for (StringUserMap myEntryType : mapConverted.entry) {
			hashMap.put(myEntryType.key, myEntryType.value);
		}
		return hashMap;
	}

}
