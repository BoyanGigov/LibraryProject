package com.library.business;

import java.util.Iterator;
import java.util.LinkedList;

import com.library.data.Author;
import com.library.data.AuthorRegistry;
import com.library.data.Book;
import com.library.data.BookRegistry;
import com.library.data.UserRegistry;
import com.library.exceptions.BookException;

public class Search {

	private BookRegistry bookRegistry;
	private AuthorRegistry authorRegistry;
//	private UserRegistry userRegistry;
	private LinkedList<Book> bookList = new LinkedList<Book>();
	private LinkedList<Author> authorList = new LinkedList<Author>();
//	private LinkedList<String> userList = new LinkedList<String>();

	public Search(BookRegistry bookRegistry, AuthorRegistry authorRegistry, UserRegistry userRegistry) {
		setBookRegistry(bookRegistry);
		setAuthorRegistry(authorRegistry);
//		setUserRegistry(userRegistry);
	}

	protected Search(LinkedList<Object> list) {

	}

	private void setBookRegistry(BookRegistry registry) {
		bookRegistry = registry;
	}

	private void setAuthorRegistry(AuthorRegistry registry) {
		authorRegistry = registry;
	}

//	private void setUserRegistry(UserRegistry registry) {
//		userRegistry = registry;
//	}

	// search book by isbn
	public Book searchBookByIsbn(String isbn) throws BookException {
		Book tempBook = this.bookRegistry.getBook(isbn);
		return tempBook;
	}

	// search book by title
	public Search searchBookByTitle(String title) {
		for (Iterator<Book> iter = getBookIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			String bookTitleLowCase = tempBook.getTitle().toLowerCase();
			if (!bookList.contains(tempBook)) {
				if (bookTitleLowCase.contains(title.toLowerCase())) {
					this.bookList.add(tempBook);
				}
			} else {
				if (!bookTitleLowCase.contains(title.toLowerCase())) {
					iter.remove();
				}
			}
		}
		return this;
	}

	// search book by author
	public Search searchBookByAuthor(String author) {
		for (Iterator<Book> iter = getBookIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			String bookAuthLowCase = tempBook.getAuthorName().toLowerCase();
			if (!bookList.contains(tempBook)) {
				if (bookAuthLowCase.contains(author.toLowerCase())) {
					bookList.add(tempBook);
				}
			} else {
				if (!bookAuthLowCase.contains(author.toLowerCase())) {
					iter.remove();
				}
			}
		}
		return this;
	}

	// search book by description
	public Search searchBookByDescription(String description) {
		for (Iterator<Book> iter = getBookIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			String bookDescLowCase = tempBook.getDescription().toLowerCase();
			if (!bookList.contains(tempBook)) {
				if (bookDescLowCase.contains(description.toLowerCase())) {
					bookList.add(tempBook);
				}
			} else {
				if (!bookDescLowCase.contains(description.toLowerCase())) {
					iter.remove();
				}
			}
		}
		return this;
	}

	// search book by dateWritten
	public Search searchBookByDate(int year, int month, int day) {
		boolean isNarrowSearch = !bookList.isEmpty();
		for (Iterator<Book> iter = getBookIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			if (year != 0) {
				if (tempBook.getDate().getYear() != year) {
					if (isNarrowSearch) {
						iter.remove();
					} else {
						continue;
					}
				}
			}
			if (month != 0) {
				if (tempBook.getDate().getMonthOfYear() != month) {
					if (isNarrowSearch) {
						iter.remove();
					} else {
						continue;
					}
				}
			}
			if (day != 0) {
				if (tempBook.getDate().getDayOfMonth() != day) {
					if (isNarrowSearch) {
						iter.remove();
					} else {
						continue;
					}
				}
			}
			if (!isNarrowSearch) {
				bookList.add(tempBook);
			}
		}
		return this;
	}

	// search book by copiesTotal
	public Search searchBookByCopiesTotal(int copiesTotal) throws Exception {
		for (Iterator<Book> iter = getBookIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			if (!bookList.contains(tempBook)) {
				if (tempBook.getCopiesTotal() == copiesTotal) {
					bookList.add(tempBook);
				} else {
					if (tempBook.getCopiesTotal() != copiesTotal) {
						iter.remove();
					}
				}
			}
		}
		return this;
	}

	// search book by copiesAvailable
	public Search searchBookByCopiesAvailable(int copiesAvailable) throws Exception {
		for (Iterator<Book> iter = getBookIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			if (!bookList.contains(tempBook)) {
				if (!bookList.contains(tempBook)) {
					if (tempBook.getCopiesAvailable() == copiesAvailable) {
						bookList.add(tempBook);
					}
				} else {
					if (tempBook.getCopiesAvailable() != copiesAvailable) {
						iter.remove();
					}
				}
			}
		}
		return this;
	}

	// search book by price
	public Search searchBookByPrice(double price) {
		for (Iterator<Book> iter = getBookIter(); iter.hasNext();) {
			Book tempBook = iter.next();
			if (!bookList.contains(tempBook)) {
				if (!bookList.contains(tempBook)) {
					if (tempBook.getPrice() == price) {
						bookList.add(tempBook);
					}
				} else {
					if (tempBook.getPrice() != price) {
						iter.remove();
					}
				}
			}
		}
		return this;
	}

	// search AuthorRegistry for author
	public Search searchAuthor(String name) {
		for (Iterator<Author> iter = this.authorRegistry.getAuthorsIter(); iter.hasNext();) {
			Author tempAuthor = iter.next();
			String authorNameLowCase = tempAuthor.getName().toLowerCase();
			if (!authorList.contains(tempAuthor)) {
				if (authorNameLowCase.contains(name.toLowerCase())) {
					authorList.add(tempAuthor);
				}
			} else {
				if (!tempAuthor.getName().contains(name)) {
					iter.remove();
				}
			}
		}
		return this;
	}

//	// search UserRegistry for user
//	public LinkedList<String> searchUser(String username) {
//		for (Iterator<String> iter = userRegistry.getRegistry().keySet().iterator(); iter.hasNext();) {
//			String tempUsername = iter.next();
//			if (!userList.contains(tempUsername)) {
//				if (tempUsername.contains(username)) {
//					userList.add(username);
//				}
//			} else {
//				if (!tempUsername.contains(username)) {
//					this.userList.remove(username);
//				}
//			}
//		}
//		return userList;
//	}

	private Iterator<Book> getBookIter() {
		if (this.bookList.isEmpty()) {
			return this.bookRegistry.getBooksIter();
		} else {
			return this.bookList.iterator();
		}
	}

	public LinkedList<Book> getBookList() {
		return this.bookList;
	}
	
	public LinkedList<Author> getAuthorList() {
		return this.authorList;
	}

}
