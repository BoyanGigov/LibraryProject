package com.library.xml.booktakerRegistry;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

public class StringMapAdapter
		extends XmlAdapter<StringMapConverter, Map<String, Map<String, PriorityQueue<DateTime>>>> {

	@Override
	public StringMapConverter marshal(Map<String, Map<String, PriorityQueue<DateTime>>> mapToConvert) throws Exception {
		StringMapConverter myMapType = new StringMapConverter();
		for (Entry<String, Map<String, PriorityQueue<DateTime>>> entry : mapToConvert.entrySet()) {
			StringMap myMapEntryType = new StringMap();
			myMapEntryType.key = entry.getKey();
			Map<String, StringPQueueMap> testMap = new TreeMap<String, StringPQueueMap>();
			for (Entry<String, PriorityQueue<DateTime>> secondEntry : entry.getValue().entrySet()) {
				StringPQueueMap secondMapEntryType = new StringPQueueMap();
				secondMapEntryType.key = secondEntry.getKey();

				int i = 0;
				String[] stringArr = new String[secondEntry.getValue().size()];
				for (DateTime test : secondEntry.getValue()) {
					stringArr[i++] = test.toString();
				}
				secondMapEntryType.value = stringArr;

				testMap.put(secondEntry.getKey(), secondMapEntryType);
			}
			myMapEntryType.value = testMap;
			myMapType.entry1.add(myMapEntryType);
		}
		return myMapType;
	}

	@Override
	public Map<String, Map<String, PriorityQueue<DateTime>>> unmarshal(StringMapConverter mapConverted)
			throws Exception {
		Map<String, Map<String, PriorityQueue<DateTime>>> unmarshaledMap = new TreeMap<String, Map<String, PriorityQueue<DateTime>>>();
		for (StringMap myEntryType : mapConverted.entry1) {
			Map<String, PriorityQueue<DateTime>> PQMap = new TreeMap<String, PriorityQueue<DateTime>>();
			for (Entry<String, StringPQueueMap> secondEntry : myEntryType.value.entrySet()) {
				StringPQueueMap secondMapEntryType = new StringPQueueMap();
				secondMapEntryType.key = secondEntry.getKey();
				PriorityQueue<DateTime> PQ = new PriorityQueue<DateTime>();
				for (String stringToDate : secondEntry.getValue().value) {
					DateTime tempDate = new DateTime(stringToDate);
					PQ.add(tempDate);
				}

				PQMap.put(secondEntry.getKey(), PQ);
			}
			unmarshaledMap.put(myEntryType.key, PQMap);
		}
		return unmarshaledMap;
	}
}
