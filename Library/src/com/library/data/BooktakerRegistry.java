package com.library.data;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.library.exceptions.BookException;
import com.library.exceptions.BooktakerException;
import com.library.xml.booktakerRegistry.BooktakerMapXML;
import com.library.xml.booktakerRegistry.StringMapAdapter;

@XmlRootElement(name = "BooktakerRegistry")
public class BooktakerRegistry implements IRegistry {
	
	@XmlJavaTypeAdapter(StringMapAdapter.class)
	// Map<username, TreeMap<isbn, PriorityQueue<deadlines>>
	private Map<String, TreeMap<String, PriorityQueue<DateTime>>> takerMap = new TreeMap<String, TreeMap<String, PriorityQueue<DateTime>>>();
	
// default contstructor for XML
	public BooktakerRegistry() {
		
	}
	
	@Override
	public Iterator<String> getKeySetIter() {
		return this.takerMap.keySet().iterator();
	}
	
	public void add(String username, String isbn, DateTime deadline, int copies) {
		if (!this.takerMap.containsKey(username)) {
			PriorityQueue<DateTime> tempPQueue = new PriorityQueue<DateTime>();
			tempPQueue.add(deadline);
			TreeMap<String, PriorityQueue<DateTime>> tempMap = new TreeMap<String, PriorityQueue<DateTime>>();
			tempMap.put(isbn, tempPQueue);
			this.takerMap.put(username, tempMap);
			copies--;
		}
		if (copies > 0) {
			if (!this.takerMap.get(username).containsKey(isbn)) {
				PriorityQueue<DateTime> tempPQueue = new PriorityQueue<DateTime>();
				tempPQueue.add(deadline);
				this.takerMap.get(username).put(isbn, tempPQueue);
				copies--;
			}
			for (int i = 0; i < copies; i++) {
				this.takerMap.get(username).get(isbn).add(deadline);
			}
		}
		BooktakerMapXML.writeToXML(this);
	}
	
	public void remove(String username, String isbn, int copies) throws BooktakerException {
		if (this.takerMap.containsKey(username)) {
			if (this.takerMap.get(username).containsKey(isbn)) {
				if (this.takerMap.get(username).get(isbn).size() < copies) {
					System.out.println("/n/tOnly " + this.takerMap.get(username).get(isbn).size()
							+ " book(s) with this ISBN have been taken, you can't return more than that!/n");
				} else {
					try {
						for (int i = 0; i < copies; i++) {
							this.takerMap.get(username).get(isbn).remove();
						}
					} catch (NoSuchElementException e) {
						throw new BooktakerException("\n\tUnexpected Exception: PQ is empty, removing\n");
					}
				}
			} else {
				throw new BooktakerException("\tBOOK WITH THIS ISBN WASN'T TAKEN");
			}
		}
		if (this.takerMap.get(username).get(isbn).size() == 0) {
			this.takerMap.get(username).remove(isbn);
		}
		if (this.takerMap.get(username).isEmpty()) {
			this.takerMap.remove(username);
		}
		BooktakerMapXML.writeToXML(this);
	}
	
	
	public StringBuilder[] getBooksTaken(String username, BookRegistry bookRegistry) throws BookException {
		DateTime tempDate = new DateTime();
		StringBuilder[] booksTaken = new StringBuilder[this.takerMap.get(username).keySet().size()];
		int i = 0;
		for (Iterator<String> iter = this.takerMap.get(username).keySet().iterator(); iter
				.hasNext(); i++) {
			String isbn = iter.next();
			Book book = bookRegistry.getBook(isbn);
			int copiesOverdue = 0;
			booksTaken[i] = new StringBuilder();

			for (Iterator<DateTime> iter2 = this.takerMap.get(username).get(isbn)
					.iterator(); iter2.hasNext();) {
				if (tempDate.isAfter(iter2.next())) {
					copiesOverdue++;
				} else {
					break;
				}
			}
			booksTaken[i].append(book.getIsbn() + ": " + book.getTitle() + " by " + book.getAuthorName()
					+ "\t copies: " + this.takerMap.get(username).get(isbn).size()
					+ " , copies past deadline: " + copiesOverdue + " , first deadline: "
					+ this.takerMap.get(username).get(isbn).peek()
							.toString(DateTimeFormat.mediumDate()));
		}
		return booksTaken;
	}
	
	public boolean containsKey(String key) {
		return this.takerMap.containsKey(key);
	}
	
	public TreeMap<String, PriorityQueue<DateTime>> get(String username) {
		return this.takerMap.get(username);
	}
	
	public boolean isEmpty() {
		return this.takerMap.isEmpty();
	}
	
}
