package com.library.business;

import java.util.Iterator;
import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.library.data.Author;
import com.library.data.AuthorRegistry;
import com.library.data.Book;
import com.library.data.BookRegistry;
import com.library.data.BooktakerRegistry;
import com.library.data.User.UserTypeEnum;
import com.library.data.UserRegistry;
import com.library.exceptions.AuthorException;
import com.library.exceptions.BookException;
import com.library.exceptions.BookingException;
import com.library.exceptions.LoginException;
import com.library.exceptions.BooktakerException;
import com.library.exceptions.UserException;
import com.library.xml.authorRegistry.AuthorMapXML;
import com.library.xml.bookRegistry.BookMapXML;
import com.library.xml.booktakerRegistry.BooktakerMapXML;
import com.library.xml.userRegistry.UserMapXML;

public class BookingSystem {

	private static BookRegistry bookRegistry;
	private static AuthorRegistry authorRegistry;
	private static UserRegistry userRegistry;
	private static BooktakerRegistry booktakerRegistry;
	private LinkedList<TakeOrReturnRequest> requestQ = new LinkedList<TakeOrReturnRequest>();

// default contstructor for XML	
	public BookingSystem() {

	}

	public BookingSystem(BookRegistry bookRegistry, AuthorRegistry authorRegistry, UserRegistry userRegistry,
			BooktakerRegistry booktakerRegistry) {
		BookMapXML.writeToXML(bookRegistry);
		AuthorMapXML.writeToXML(authorRegistry);
		UserMapXML.writeToXML(userRegistry);
		BooktakerMapXML.writeToXML(booktakerRegistry);
		setBookRegistry(bookRegistry);
		setAuthorRegistry(authorRegistry);
		setUserRegistry(userRegistry);
		setTakerMap(booktakerRegistry);
	}

	private void setBookRegistry(BookRegistry registry) {
		bookRegistry = registry;
	}

	private void setAuthorRegistry(AuthorRegistry registry) {
		authorRegistry = registry;
	}

	private void setUserRegistry(UserRegistry registry) {
		userRegistry = registry;
	}

	private void setTakerMap(BooktakerRegistry registry) {
		booktakerRegistry = registry;
	}

	// book-related methods
	public void addBook(String isbn, String title, String authorName, String description, int year, int month, int day,
			int copiesTotal, double price) throws BookException {
		Book book = bookRegistry.add(isbn, title, authorName, description, year, month, day, copiesTotal, price);
		authorRegistry.addBookWritten(authorName, book);
	}

	public void removeBook(String isbn) throws BookException {
		Book tempBook = bookRegistry.getBook(isbn);
		authorRegistry.removeBookWritten(tempBook.getAuthorName(), tempBook);
		bookRegistry.remove(isbn);
	}

	public void addBookCopies(String isbn, short copies) throws BookException {
		bookRegistry.addCopies(isbn, copies);
	}

	public void removeBookCopies(String isbn, short copies) throws BookException {
		bookRegistry.removeCopies(isbn, copies);
	}

	private void takeBooks(String username, String isbn, int copies, DateTime deadline) throws BookException {
		if (bookRegistry.checkCopiesAvailable(isbn) < copies) {
			throw new BookException("\tNot enough copies available");
		}
		booktakerRegistry.add(username, isbn, deadline, copies);
		bookRegistry.takeCopies(isbn, copies);
	}

	private void returnBooks(String username, String isbn, int copies) throws BookException, BooktakerException {
		booktakerRegistry.remove(username, isbn, copies);
		bookRegistry.returnCopies(isbn, copies);
	}

	public Search newSearch() {
		return new Search(bookRegistry, authorRegistry, userRegistry);
	}

	public StringBuilder[] getUserTaken(String username) throws BooktakerException, UserException, BookException {
		if (booktakerRegistry.isEmpty()) {
			throw new BooktakerException("\tNO USERS HAVE TAKEN ANY BOOKS SO FAR");
		}
		if (!booktakerRegistry.containsKey(username)) {
			throw new BooktakerException("\tUSER HASN'T TAKEN ANY BOOKS");
		} else if (booktakerRegistry.get(username).isEmpty()) {
			throw new UserException("\tUSER DOES NOT EXIST");
		}
		return booktakerRegistry.getBooksTaken(username, BookingSystem.bookRegistry);
	}

	public StringBuilder searchByISBN(String isbn) throws BookException {
		Search tempSearch = newSearch();
		tempSearch.searchBookByIsbn(isbn);
		StringBuilder sb = new StringBuilder();
		Book tempBook = tempSearch.searchBookByIsbn(isbn);
		return sb.append(tempBook.getIsbn() + ": " + tempBook.getTitle() + " by " + tempBook.getAuthorName());
	}

	public StringBuilder[] getBooksFromList(LinkedList<Book> linkedList) {
		StringBuilder[] bookList = new StringBuilder[linkedList.size()];
		int i = 0;
		for (Iterator<Book> iter = linkedList.iterator(); iter.hasNext(); i++) {
			bookList[i] = new StringBuilder((i + 1) + " - " + linkedList.get(i).getIsbn() + ": "
					+ linkedList.get(i).getTitle() + " by " + linkedList.get(i).getAuthorName() + " written on "
					+ linkedList.get(i).getDate().toString(DateTimeFormat.mediumDate()));
			iter.next();
		}
		return bookList;
	}

	// request-related methods
	public void requestTakeBooks(String username, String isbn, int copies, DateTime deadline) throws BookException {
		if (bookRegistry.getBook(isbn).getCopiesAvailable() >= copies) {
			TakeOrReturnRequest request = new TakeOrReturnRequest(username, isbn, copies, true, deadline);
			requestQ.add(request);
		} else {
			System.out.println("\n\tOnly " + bookRegistry.getBook(isbn).getCopiesAvailable()
					+ " book(s) with this ISBN are available, you can't take more than that!\n");
		}
	}

	public void requestReturnBooks(String username, String isbn, int copies) throws BookException, BooktakerException {
		if (!booktakerRegistry.containsKey(username)) {
			throw new BooktakerException("\tUser hasn't taken any books");
		}
		if (!booktakerRegistry.get(username).containsKey(isbn)) {
			throw new BookException("\tISBN not found");
		}
		if (booktakerRegistry.get(username).get(isbn).size() >= copies) {
			for (Iterator<DateTime> iter = booktakerRegistry.get(username).get(isbn).iterator(); iter.hasNext()
					&& copies > 0; copies--) {
				requestQ.add(new TakeOrReturnRequest(username, isbn, 1, false, iter.next()));
			}
		} else {
			System.out.println("\n\tOnly " + booktakerRegistry.get(username).get(isbn).size()
					+ " book(s) with this ISBN have been taken, you can't return more than that!\n");
		}
	}

	public boolean printAllRequests() {
		if (requestQ.isEmpty()) {
			return false;
		} else {
			int index = 1;
			for (TakeOrReturnRequest request : requestQ) {
				request.printRequest(index++);
			}
			return true;
		}
	}

	public void manageRequest(int index, boolean accept) throws BookException, BooktakerException {
		index--;
		if (accept) {
			TakeOrReturnRequest request = requestQ.get(index);
			if (request.isTakeRequest()) {
				this.takeBooks(request.getUsername(), request.getIsbn(), request.getCopies(), request.getDeadline());
			} else {
				this.returnBooks(request.getUsername(), request.getIsbn(), request.getCopies());
			}
		}
		requestQ.remove(index);
	}

	// author-related methods
	public StringBuilder[] getAuthorsFromList(LinkedList<Author> linkedList) {
		StringBuilder[] authorList = new StringBuilder[linkedList.size()];
		int i = 0;
		for (Iterator<Author> iter = linkedList.iterator(); iter.hasNext(); i++) {
			authorList[i] = new StringBuilder(linkedList.get(i).getName());
			iter.next();
		}
		return authorList;
	}

	public StringBuilder[] getAuthorInfo(String name) throws AuthorException {
		Author tempAuthor = authorRegistry.getAuthor(name);
		StringBuilder[] authorInfo = new StringBuilder[tempAuthor.getBooksWritten().size() + 1];
		int i = 0;
		if (tempAuthor.getDescription().equals("auto-generated")) {
			authorInfo[0] = new StringBuilder(tempAuthor.getName() + "\n\nBooks written:");
		} else {
			authorInfo[0] = new StringBuilder(
					tempAuthor.getName() + " born " + tempAuthor.getBirthDate().toString(DateTimeFormat.mediumDate())
							+ "\n\ndescription: " + tempAuthor.getDescription() + "\n\nBooks written:");
		}
		for (Iterator<Book> iter = tempAuthor.getBooksWritten().iterator(); iter.hasNext(); i++) {
			Book tempBook = iter.next();
			authorInfo[i + 1] = new StringBuilder(tempBook.getTitle() + ": "+tempBook.getDate().toString(DateTimeFormat.mediumDate()));
		}

		return authorInfo;
	}

	public void setAuthorDescription(String name, String description) throws AuthorException {
		authorRegistry.setDescription(name, description);
	}

	public void setAuthorBirthDate(String name, int year, int month, int day) throws AuthorException {
		authorRegistry.setBirthDate(name, year, month, day);
	}

	// user-related methods
	public void createUser(UserTypeEnum userType, String username, String password) throws LoginException {
		userRegistry.add(userType, username, password);
	}

	public UserTypeEnum logUser(String username, String password) throws LoginException {
		return userRegistry.logIn(username, password);
	}

	public StringBuilder[] viewUserProfile(String username) throws BooktakerException, UserException, BookException {
		return getUserTaken(username);
	}

	public void deleteUser(String username, String password) throws UserException {
		if (booktakerRegistry.containsKey(username)) {
			throw new UserException("\t\" " + username + " \" must first return all books");
		}
		userRegistry.remove(username, password);
	}

	public void deleteUserByAdmin(String username) throws UserException {
		userRegistry.removeByAdmin(username);
	}

	public void printList(int option) throws BookingException {
		int index = 1;
		System.out.println("\n\tLIST START\n");
		for (Iterator<String> iter = getRegistryIter(option); iter.hasNext();) {
			System.out.println("\t" + index++ + ": " + iter.next());
		}
		System.out.println("\n\tLIST END\n");
	}

	private Iterator<String> getRegistryIter(int option) throws BookingException {
		if (option == 1) {
			return booktakerRegistry.getKeySetIter();
		} else if (option == 2) {
			return authorRegistry.getKeySetIter();
		} else if (option == 3) {
			return bookRegistry.getKeySetIter();
		} else if (option == 4) {
			return userRegistry.getKeySetIter();
		} else {
			throw new BookingException("\tIncorrect option");
		}
	}
}
