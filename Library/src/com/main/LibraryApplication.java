package com.main;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.library.business.*;
import com.library.data.User.UserTypeEnum;
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

public class LibraryApplication {

	private static final BookingSystem bookingSystem = new BookingSystem(BookMapXML.readFromXML(),
			AuthorMapXML.readFromXML(), UserMapXML.readFromXML(), BooktakerMapXML.readFromXML());
	private static UserTypeEnum userType;
	private static String username;
	private static Scanner sc = new Scanner(System.in);
	private static boolean isDefaultAdmingCreated = !UserMapXML.readFromXML().checkIfUsernameAvailable("admin");

	public static void main(String[] args) {
		try {
			// String isbn, String title, String authorName, String description, int year,
			// int month, int day, int copiesTotal, double price
			bookingSystem.addBook("isbn1", "title1", "authorName1", "description1", 2015, 10, 24, 5, 3);
			bookingSystem.addBook("isbn2", "title2", "authorName2", "description2", 2014, 9, 23, 4, 2);
			bookingSystem.addBook("isbn3", "title3", "authorName1", "description3", 2012, 5, 6, 12, 46);
		} catch (Exception e) {
			System.out.println("\t\texception when adding test books at start:\n" + e.getMessage() + "\n");
		}

		createDefaultAdmin();
		boolean runApplication;
		do {
			runApplication = welcomeMenu();
			if (runApplication) {
				loggedInMenu();
			}
		} while (runApplication);
	}

	private static boolean welcomeMenu() {
		int option;

		do {
			System.out.println("\nWould you like to: \n0 - exit\n1 - log in\n2 - register\nselect option:");
			try {
				option = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\n\tINCORRECT INPUT 1\n");
				continue;
			} catch (Exception e) {
				System.out.println("\n\tError 1");
				return false;
			} finally {
				sc.nextLine();
			}
			if (option == 0) {
				return false;
			} else if (option == 1 || option == 2) {
				try {
					logRegMenu(option);
				} catch (LoginException e) {
					System.out.println("\n" + e.getMessage() + "\n");
				} catch (Exception e) {
					System.out.println("\n\tError 2");
					return false;
				}
			} else {
				System.out.println("\n\tINCORRECT INPUT 2\n");
				return false;
			}
		} while (userType == UserTypeEnum.DEFAULT);
		return true;
	}

	private static void logRegMenu(int option) throws LoginException {
		System.out.println("username:");
		String username = sc.nextLine();
		System.out.println("password:");
		String password = sc.nextLine();

		if (option == 1) {
			userType = bookingSystem.logUser(username, password);
			LibraryApplication.username = username;
			return;
		} else if (option == 2) {
			bookingSystem.createUser(UserTypeEnum.STANDARD, username, password);
			userType = UserTypeEnum.STANDARD;
			LibraryApplication.username = username;
			return;
		}
	}

	private static void loggedInMenu() {
		boolean refreshMenu = false;
		do {
			if (userType == UserTypeEnum.STANDARD) {
				refreshMenu = standardUserPage();
			} else if (userType == UserTypeEnum.ADMIN) {
				refreshMenu = adminUserPage();
			} else {
				System.out.println("\tincorrect userType!\n");
			}
		} while (refreshMenu);
	}

// user pages
	private static boolean standardUserPage() {
		int option = -1;
		System.out.println("\n\n\n\t\tThis is your profile page:\n\n\tBooks taken:");
		try {
			printSBArray(bookingSystem.viewUserProfile(username));
		} catch (BooktakerException e) {
			System.out.println("\n" + e.getMessage() + " so user profile is empty\n");
		} catch (UserException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		} catch (Exception e) {
			System.out.println("\n\tError 17");
			e.printStackTrace();
			return false;
		}

		System.out.println(
				"0 - Log out\n1 - Search author\n2 - Search book\n3 - Take book\n4 - Return book\n5 - Delete account\nselect option:");
		try {
			option = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 3\n");
		} catch (Exception e) {
			System.out.println("\n\tError 18");
			return false;
		} finally {
			sc.nextLine();
		}
		if (option == 0) {
			// Log out
			userType = UserTypeEnum.DEFAULT;
			return false;
		} else if (option == 1) {
			try {
				printSBArray(bookingSystem.getAuthorInfo(searchAuthor()));
			} catch (AuthorException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			}
		} else if (option == 2) {
			searchBook();
		} else if (option == 3) {
			requestTakeBook();
		} else if (option == 4) {
			requestReturnBook();
		} else if (option == 5) {
			if (deleteOwnAccount()) {
				userType = UserTypeEnum.DEFAULT;
				return false;
			}
		}
		return true;
	}

	private static boolean adminUserPage() {
		int option = -1;
		System.out.println(
				"\n0 - Log out\n1 - Manage requests\n2 - Search author\n3 - Fill author data\n4 - Search book\n5 - Add book\n6 - Remove book"
						+ "\n7 - Add/Remove book copies\n8 - View a user's profile\n9 - Get list\n10 - Delete User\n11 - Create new admin user\nselect option:");
		try {
			option = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 12\n");
		} catch (Exception e) {
			System.out.println("\n\tError 19");
			return false;
		} finally {
			try {
				sc.nextLine();
			} catch (Exception e) {
				System.out.println("\n\tError 23");
			}
		}
		if (option == 0) {
			// Log out
			userType = UserTypeEnum.DEFAULT;
			return false;
		} else if (option == 1) {
			manageRequests();
		} else if (option == 2) {
			try {
				printSBArray(bookingSystem.getAuthorInfo(searchAuthor()));
			} catch (AuthorException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			}
		} else if (option == 3) {
			try {
				changeAuthorData();
			} catch (AuthorException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			}
		} else if (option == 4) {
			searchBook();
		} else if (option == 5) {
			addBook();
		} else if (option == 6) {
			removeBook();
		} else if (option == 7) {
			addOrRemoveBookCopies();
		} else if (option == 8) {
			viewUserProfile();
		} else if (option == 9) {
			try {
				System.out.println("\n1 - Book-takers\n2 - Authors\n3 - Books\n4 - Users\nselect option:");
				int printOption = sc.nextInt();
				bookingSystem.printList(printOption);
			} catch (InputMismatchException e) {
				System.out.println("\tIncorrect Input 21");
			} catch (BookingException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			} catch (Exception e) {
				System.out.println("\n\tError 31");
			} finally {
				sc.nextLine();
			}
		} else if (option == 10) {
			deleteUser();
		} else if (option == 11) {
			System.out.println("username:");
			String username = sc.nextLine();
			System.out.println("password:");
			String password = sc.nextLine();
			try {
				bookingSystem.createUser(UserTypeEnum.ADMIN, username, password);
			} catch (LoginException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			} catch (Exception e) {
				System.out.println("\n\tError 20");
				return false;
			}
		}
		return true;
	}

// standard user actions
	private static void requestTakeBook() {
		if (searchBook()) {
			int copies = 0;
			String isbn;
			System.out.println("\nConfirm isbn:");
			isbn = sc.nextLine();
			System.out.println("\nHow many copies would you like to take?\ncopies:");
			try {
				copies = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\n\tINCORRECT INPUT 4\n");
				return;
			} catch (Exception e) {
				System.out.println("\n\tError 3");
				return;
			} finally {
				sc.nextLine();
			}
			DateTime deadline = DateTime.now().plusMonths(1);
			System.out.println("\nDefault deadline: " + deadline.toString(DateTimeFormat.shortDate()));
			System.out.println(
					"Default deadline is 1 month from current date. Do you wish to change that?\n1 - yes\n2 - no");
			try {
				if (sc.nextInt() == 1) {
					System.out.println("State desired deadline for the book(s)\nYEAR/MONTH/DAY:");
					try {
						sc.useDelimiter("\\p{javaWhitespace}+|\\.+|\\/+|\\,");
						deadline = new DateTime(sc.nextInt(), sc.nextInt(), sc.nextInt(), 0, 0);
					} catch (InputMismatchException e) {
						System.out.println("\n\tINCORRECT INPUT 5\n");
						return;
					} catch (Exception e) {
						System.out.println("\n\tError 4");
						return;
					} finally {
						sc.nextLine();
						sc.useDelimiter("\\p{javaWhitespace}+");
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("\n\tINCORRECT INPUT 6\n");
				return;
			} catch (Exception e) {
				System.out.println("\n\tError 5");
				return;
			} finally {
				sc.nextLine();
			}
			if (copies <= 0) {
				System.out.println("\nYou can't take " + copies + " copies!");
				return;
			} else {
				try {
					bookingSystem.requestTakeBooks(username, isbn, copies, deadline);
				} catch (BookException e) {
					System.out.println("\n" + e.getMessage() + "\n");
				} catch (Exception e) {
					System.out.println("\n\tError 6");
					return;
				}
			}
		}
	}

	private static void requestReturnBook() {
		int copies = 0;
		String isbn;
		try {
			printSBArray(bookingSystem.getUserTaken(username));
		} catch (BooktakerException e) {
			System.out.println("\n" + e.getMessage() + "\n");
			return;
		} catch (UserException e) {
			System.out.println("\n" + e.getMessage() + "\n");
			return;
		} catch (Exception e) {
			System.out.println("\n\tError 25");
			return;
		}
		System.out.println("Select which book you'd like to return.\nisbn:");
		isbn = sc.nextLine();
		System.out.println("\nSelect how many copies you'd like to return.\ncopies:");
		try {
			copies = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 7\n");
			return;
		} catch (Exception e) {
			System.out.println("\n\tError 7");
			return;
		} finally {
			sc.nextLine();
		}
		if (copies < 1) {
			System.out.println("\nYou can't return less than 1 copy!");
			return;
		} else {
			try {
				bookingSystem.requestReturnBooks(username, isbn, copies);
			} catch (BookException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			} catch (Exception e) {
				System.out.println("Error 26");
			}
		}
	}

	private static boolean deleteOwnAccount() {
		System.out.println("password:");
		String password = sc.nextLine();
		try {
			bookingSystem.deleteUser(LibraryApplication.username, password);
			return true;
		} catch (UserException e) {
			System.out.println("\n" + e.getMessage() + "\n");
			return false;
		} catch (Exception e) {
			System.out.println("\n\tError 8");
			return false;
		}
	}

// all-user actions
	private static void viewUserProfile() {
		System.out.println("username:");
		String user = sc.nextLine();
		try {
			printSBArray(bookingSystem.viewUserProfile(user));
		} catch (BooktakerException e) {
			System.out.println("\n" + e.getMessage() + " so user profile is empty\n");
		} catch (UserException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		} catch (Exception e) {
			System.out.println("\n\tError 9");
			return;
		}
	}

	private static boolean searchBook() {

		String isbn;
		System.out.println("\nSearch by:\n0 - go back\n1 - ISBN\n2 - title\n3 - author\nselect option:");
		int searchOption = 0;
		try {
			searchOption = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 8\n");
			return false;
		} catch (Exception e) {
			System.out.println("\n\tError 10");
		} finally {
			sc.nextLine();
		}
		if (searchOption == 0) {
			return false;
		} else if (searchOption == 1) {
			System.out.println("isbn:");
			isbn = sc.nextLine();
			try {
				System.out.println("\n" + bookingSystem.searchByISBN(isbn));
			} catch (BookException e) {
				System.out.println("\n" + e.getMessage() + "\n");
				return false;
			} catch (Exception e) {
				System.out.println("\n\tError 11");
				return false;
			}
		} else if (searchOption == 2) {
			System.out.println("title:");
			String title = sc.nextLine();
			Search bookListSearch = bookingSystem.newSearch().searchBookByTitle(title);
			if (bookListSearch.getBookList().isEmpty()) {
				System.out.println("\n\tNOTHING FOUND\n");
				return false;
			}
			try {
				narrowSearchBook(bookListSearch);
			} catch (Exception e) {
				System.out.println("LE1: " + e.getMessage() + "\n");
			}

		} else if (searchOption == 3) {
			System.out.println("author:");
			String authorName = sc.nextLine();
			Search bookListSearch = bookingSystem.newSearch().searchBookByAuthor(authorName);
			if (bookListSearch.getBookList().isEmpty()) {
				System.out.println("\n\tNOTHING FOUND\n");
				return false;
			}
			try {
				narrowSearchBook(bookListSearch);
			} catch (Exception e) {
				System.out.println("LE2: " + e.getMessage() + "\n");
			}
		}
		return true;
	}

	private static Search narrowSearchBook(Search bookListSearch) {
		if (bookListSearch.getBookList().isEmpty()) {
			System.out.println("\n\tNO RESULTS FOUND\n");
			return bookListSearch;
		} else {
			printSBArray(bookingSystem.getBooksFromList(bookListSearch.getBookList()));
		}
		System.out.println("Would you like to narrow the search?\n1 - yes\n2 - no\nselect option:");
		int option = 0;
		try {
			option = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 9\n");
			return bookListSearch;
		} catch (Exception e) {
			System.out.println("\n\tError 12");
		} finally {
			sc.nextLine();
		}
		if (option == 1) {
			System.out.println(
					"\nWould you like to narrow search by:\n1 - title\n2 - author\n3 - description\n4 - date written\n5 - price\n\nselect option:");
			int optionNarrowSearch = 0;
			try {
				optionNarrowSearch = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\n\tINCORRECT INPUT 10\n");
				return bookListSearch;
			} catch (Exception e) {
				System.out.println("\n\tError 13");
			} finally {
				sc.nextLine();
			}
			if (optionNarrowSearch == 1) {
				System.out.println("title:");
				bookListSearch.searchBookByTitle(sc.nextLine());
			} else if (optionNarrowSearch == 2) {
				System.out.println("author:");
				bookListSearch.searchBookByAuthor(sc.nextLine());
			} else if (optionNarrowSearch == 3) {
				System.out.println("description:");
				bookListSearch.searchBookByDescription(sc.nextLine());
			} else if (optionNarrowSearch == 4) {
				System.out.println(
						"\nUse 0 to skip a parameter (for example 1994 0 0 searches all books from 1994)\nYEAR/MONTH/DAY:");
				try {
					sc.useDelimiter("\\p{javaWhitespace}+|\\.+|\\/+|\\,");
					bookListSearch.searchBookByDate(sc.nextInt(), sc.nextInt(), sc.nextInt());
				} catch (InputMismatchException e) {
					System.out.println("\n\tINCORRECT INPUT 11\n");
					return bookListSearch;
				} catch (Exception e) {
					System.out.println("\n\tError 14");
				} finally {
					sc.nextLine();
					sc.useDelimiter("\\p{javaWhitespace}+");
				}
			} else if (optionNarrowSearch == 5) {
				try {
					System.out.println("price:");
					bookListSearch.searchBookByPrice(sc.nextDouble());
				} catch (InputMismatchException e) {
					System.out.println("\n\tINCORRECT INPUT for price\n");
				} catch (Exception e) {
					System.out.println("\n\tError 15");
				} finally {
					sc.nextLine();
				}
			}
		} else if (option == 2) {
			System.out.println("\nThis is the final result of your search:");
			printSBArray(bookingSystem.getBooksFromList(bookListSearch.getBookList()));
			return bookListSearch;
		}
		return narrowSearchBook(bookListSearch);
	}

	private static String searchAuthor() throws AuthorException {
		System.out.println("\nSearch author by name: ");
		String name = sc.nextLine();
		Search authorListSearch = bookingSystem.newSearch().searchAuthor(name);
		if (authorListSearch.getAuthorList().isEmpty()) {
			throw new AuthorException("\n\tNOTHING FOUND\n");
		}
		printSBArray(bookingSystem.getAuthorsFromList(authorListSearch.getAuthorList()));
		System.out.println("\nSelect author: ");
		name = sc.nextLine();
		return name;
	}

// admin user actions
	private static void manageRequests() {
		int index, option;
		boolean acceptReq;
		if (bookingSystem.printAllRequests()) {
			try {
				System.out.println("\nSelect index:");
				index = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\n\tINCORRECT INPUT 13");
				return;
			}
			try {
				System.out.println("\n1 - Accept request\n2 - Deny request");
				option = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\n\tINCORRECT INPUT 14");
				return;
			}
			if (option == 1) {
				acceptReq = true;
			} else if (option == 2) {
				acceptReq = false;
			} else {
				System.out.println("\n\tIncorrect input 20");
				return;
			}
			try {
				bookingSystem.manageRequest(index, acceptReq);
			} catch (BookException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			} catch (Exception e) {
				System.out.println("\n\tError 24");
			}
		} else {
			System.out.println("\n\tThere are currently no pending requests.");
		}
	}

	private static void changeAuthorData() throws AuthorException {
		String name = searchAuthor();
		System.out.println("\ndescription:");
		String description = sc.nextLine();
		bookingSystem.setAuthorDescription(name, description);
		System.out.println("\nYEAR/MONTH/DAY:");
		try {
			sc.useDelimiter("\\p{javaWhitespace}+|\\.+|\\/+|\\,");
			bookingSystem.setAuthorBirthDate(name, sc.nextInt(), sc.nextInt(), sc.nextInt());
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 18");
		} catch (Exception e) {
			System.out.println("\n\tError 29");
		} finally {
			sc.nextLine();
			sc.useDelimiter("\\p{javaWhitespace}+");
		}
	}

	private static void addBook() {
		int dateArray[] = { 0, 0, 0 };
		System.out.println("isbn:");
		String isbn = sc.nextLine();
		System.out.println("title:");
		String title = sc.nextLine();
		System.out.println("author name:");
		String authorName = sc.nextLine();
		System.out.println("description:");
		String description = sc.nextLine();
		System.out.println("YEAR/MONTH/DAY:");

		try {
			sc.useDelimiter("\\p{javaWhitespace}+|\\.+|\\/+|\\,");
			int i = 0;
			while (sc.hasNext()) {
				if (sc.hasNextInt()) {
					dateArray[i] = sc.nextInt();
					i++;
				} else {
					sc.next();
				}
				if (i == dateArray.length) {
					break;
				}
			}
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 19");
		} catch (Exception e) {
			System.out.println("\n\tError 30");
		} finally {
			sc.nextLine();
			sc.useDelimiter("\\p{javaWhitespace}+");
		}

		try {
			System.out.println("copies:");
			int copiesTotal = sc.nextInt();
			sc.nextLine();
			System.out.println("price:");
			double price = sc.nextDouble();
			sc.nextLine();
			int year = dateArray[0];
			int month = dateArray[1];
			int day = dateArray[2];
			bookingSystem.addBook(isbn, title, authorName, description, year, month, day, copiesTotal, price);
			System.out.println("\nBook added successfully.");
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT when adding book\n");
			return;
		} catch (BookException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		} catch (Exception e) {
			System.out.println("\n\tError 16");
			return;
		}
	}

	private static void removeBook() {
		if (searchBook()) {
			System.out.println("\nConfirm isbn:");
			String isbn = sc.nextLine();
			try {
				bookingSystem.removeBook(isbn);
			} catch (BookException e) {
				System.out.println("\n" + e.getMessage() + "\n");
			}
		}
	}

	private static void deleteUser() {
		try {
			System.out.println("username:");
			String user = sc.nextLine();
			if (!LibraryApplication.username.equals(user)) {
				bookingSystem.deleteUserByAdmin(user);
			} else {
				System.out.println("\n\tYOU CAN'T DELETE YOUR OWN ADMIN ACCOUNT\n");
			}
		} catch (UserException e) {
			System.out.println("\n" + e.getMessage() + "\n");
		} catch (Exception e) {
			System.out.println("\n\tError 22");
			return;
		}
	}

	private static void addOrRemoveBookCopies() {
		String isbn;
		short copies = 0, option;
		if (searchBook()) {
			System.out.println("\nselect isbn: ");
			isbn = sc.nextLine();
			try {
				System.out.println("\ncopies: ");
				copies = sc.nextShort();
			} catch (InputMismatchException e) {
				System.out.println("\n\tINCORRECT INPUT 15\n");
				return;
			} catch (Exception e) {
				System.out.println("\n\tError 27");
				return;
			} finally {
				sc.nextLine();
			}
		} else {
			return;
		}
		try {
			System.out.println("\n1 - Add copies\n2 - Remove copies");
			option = sc.nextShort();
		} catch (InputMismatchException e) {
			System.out.println("\n\tINCORRECT INPUT 16\n");
			return;
		} catch (Exception e) {
			System.out.println("\n\tError 28");
			return;
		} finally {
			sc.nextLine();
		}
		if (option == 1) {
			try {
				bookingSystem.addBookCopies(isbn, copies);
			} catch (BookException e) {
				System.out.println("\n" + e.getMessage() + "\n");
				return;
			}
		} else if (option == 2) {
			try {
				bookingSystem.removeBookCopies(isbn, copies);
			} catch (BookException e) {
				System.out.println("\n" + e.getMessage() + "\n");
				return;
			}
		} else {
			System.out.println("\n\tINCORRECT INPUT 17");
			return;
		}
	}

// other
	private static void createDefaultAdmin() {
		if (getAdminCreated() == false) {
			try {
				bookingSystem.createUser(UserTypeEnum.ADMIN, "admin", "pass");
				System.out.println("Created default admin.\nusername: admin\npassword: pass \n");
				setAdminCreated();
			} catch (LoginException e) {
				System.out.println("\tError creating default admin. " + e.getMessage() + "\n");
			} catch (Exception e) {
				System.out.println("\n\tError 21");
				return;
			}
		}
	}

	private static boolean getAdminCreated() {
		return isDefaultAdmingCreated;
	}

	private static void setAdminCreated() {
		isDefaultAdmingCreated = true;
	}

	private static void printSBArray(StringBuilder[] sb) {
		for (int i = 0; i < sb.length; i++) {
			System.out.println("\n\t" + sb[i]);
		}
		System.out.println();
	}

}
