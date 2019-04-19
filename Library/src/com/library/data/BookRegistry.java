package com.library.data;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.library.exceptions.BookException;
import com.library.xml.bookRegistry.BookMapXML;
import com.library.xml.bookRegistry.StringBookAdapter;

@XmlRootElement(name = "bookReg")
public class BookRegistry implements IRegistry{

	@XmlJavaTypeAdapter(StringBookAdapter.class)
	private Map<String, Book> bookRegistry = new TreeMap<String, Book>();

// default constructor for XML
	public BookRegistry() {

	}
	
	@Override
	public Iterator<String> getKeySetIter() {
		return bookRegistry.keySet().iterator();
	}

	// addBook method
	public Book add(String isbn, String title, String authorName, String description, int year, int month, int day,
			int copiesTotal, double price) throws BookException {
		if (!this.bookRegistry.containsKey(isbn)) {
			try {
				DateTime date = new DateTime(year, month, day, 0, 0);
				Book book = new Book(isbn, title, authorName, description, date, copiesTotal, price);
				bookRegistry.put(isbn, book);
				BookMapXML.writeToXML(this);
				return book;
			} catch (org.joda.time.IllegalFieldValueException e) {
				throw new BookException("\tAdding the book failed: " + e.getMessage());
			} catch (Exception e) {
				throw new BookException("\tError BR1");
			}
		} else {
			throw new BookException("\tBook already exists");
		}
	}

	// removeBook method
	public void remove(String isbn) throws BookException {
		if (this.bookRegistry.remove(isbn) != null) {
			System.out.println("Book removed.");
		} else {
			throw new BookException("\tBook not found");
		}
		this.bookRegistry.remove(isbn);
		BookMapXML.writeToXML(this);
	}

	// addCopies method
	public void addCopies(String isbn, int copies) throws BookException {
		Book tempBook = getBook(isbn);
		tempBook.changeCopiesTotal(copies);
		BookMapXML.writeToXML(this);
	}

	// removeCopies method, requires negative number for changeCopiesTotal
	public void removeCopies(String isbn, int copies) throws BookException {
		Book tempBook = getBook(isbn);
		if (tempBook.getCopiesTotal() > copies) {
			tempBook.changeCopiesTotal(-copies);
		}
		BookMapXML.writeToXML(this);
	}

	// check how many copies are available
	public int checkCopiesAvailable(String isbn) throws BookException {
		return getBook(isbn).getCopiesAvailable();
	}

	// returnCopies method, requires negative number for changeCopiesAvailable
	public void returnCopies(String isbn, int copies) throws BookException {
		Book tempBook = getBook(isbn);
		tempBook.changeCopiesAvailable(copies);
		BookMapXML.writeToXML(this);
	}

	// takeCopies method, requires negative number for changeCopiesAvailable
	public void takeCopies(String isbn, int copies) throws BookException {
		Book tempBook = getBook(isbn);
		if (tempBook.getCopiesAvailable() > copies) {
			tempBook.changeCopiesAvailable(-copies);
		}
		BookMapXML.writeToXML(this);
	}

	public void convertDateStringToDate() {
		for (Iterator<Book> iter = this.getBooksIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			tempBook.setDate(DateStringConverter.dateStringToDate(tempBook.getDateString()));
		}
	}

	public Book getBook(String isbn) throws BookException {
		try {
			return this.bookRegistry.get(isbn);
		} catch (NullPointerException e) {
			throw new BookException("\tISBN DOES NO EXIST");
		}
	}

	public Iterator<Book> getBooksIter() {
		return this.bookRegistry.values().iterator();
	}
}
