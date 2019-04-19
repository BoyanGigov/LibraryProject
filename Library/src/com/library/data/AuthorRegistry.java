package com.library.data;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.library.exceptions.AuthorException;
import com.library.xml.authorRegistry.AuthorMapXML;
import com.library.xml.authorRegistry.StringAuthorAdapter;

@XmlRootElement(name = "AuthorReg")
public class AuthorRegistry implements IRegistry{

	@XmlJavaTypeAdapter(StringAuthorAdapter.class)
	private Map<String, Author> authorRegistry = new TreeMap<String, Author>();

// default constructor for XML
	public AuthorRegistry() {

	}
	
	@Override
	public Iterator<String> getKeySetIter() {
		return authorRegistry.keySet().iterator();
	}
	
	public void convertDateStringToDate() {
		for (Iterator<Author> iter = this.getAuthorsIter(); iter.hasNext();) {
			Author tempAuthor = iter.next();
			try {
				DateTime tempDate = DateStringConverter.dateStringToDate(tempAuthor.getDateString());
				tempAuthor.setBirthDate(tempDate);
			} catch (org.joda.time.IllegalFieldValueException e) {
				System.out.println("\t\tSetting birthDate failed: " + e.getMessage());
			}

		}
	}

	public void setDescription(String name, String description) throws AuthorException {
		if (this.authorRegistry.containsKey(name)) {
			try {
				this.authorRegistry.get(name).setDescription(description);
			} catch (Exception e) {
				throw new AuthorException("\t\tSetting description failed: " + e.getMessage());
			}
		} else {
			throw new AuthorException("\t\tAuthor not found");
		}
	}

	public void setBirthDate(String name, int year, int month, int day) throws AuthorException {
		if (this.authorRegistry.containsKey(name)) {
			try {
				DateTime birthDate = new DateTime(year, month, day, 0, 0);
				this.authorRegistry.get(name).setBirthDate(birthDate);
			} catch (org.joda.time.IllegalFieldValueException e) {
				throw new AuthorException("\t\tSetting birthDate failed: " + e.getMessage());
			}
		} else {
			throw new AuthorException("\t\tAuthor not found");
		}
	}

	public void addBookWritten(String name, Book tempBook) {
		if (this.authorRegistry.get(name) == null) {
			this.authorRegistry.put(name, new Author(name, "auto-generated author\nThis author's data hasn't been filled yet", new DateTime(1, 1, 1, 0, 0)));
		}
		this.authorRegistry.get(name).addBooksWritten(tempBook);
		AuthorMapXML.writeToXML(this);
	}

	public void removeBookWritten(String name, Book tempBook) {
		this.authorRegistry.get(name).removeBooksWritten(tempBook);
		if (this.authorRegistry.get(name).getBooksWritten().isEmpty()) {
			this.authorRegistry.remove(name);
		}
	}
	
	public Author getAuthor(String name) throws AuthorException {
		if (authorRegistry.containsKey(name)) {
			return authorRegistry.get(name);
		} else {
			throw new AuthorException("\tAuthor not found");
		}
	}
	
	public Iterator<Author> getAuthorsIter() {
		return authorRegistry.values().iterator();
	}
}
