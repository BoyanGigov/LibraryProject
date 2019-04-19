package com.library.xml.bookRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.library.data.BookRegistry;

public class BookMapXML {

	private static final String BOOK_XML = "./bookRegistry-jaxb.xml";

	public static void writeToXML(BookRegistry bookRegistry) {
		try {
			JAXBContext context = JAXBContext.newInstance(BookRegistry.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//Write to File
			m.marshal(bookRegistry, new File(BOOK_XML));
		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tWRITE TO book XML FAILED\n");
			System.exit(0);
		}
	}

	public static BookRegistry readFromXML() {
		BookRegistry bookDataFromXML = null;
		try {
			JAXBContext context = JAXBContext.newInstance(BookRegistry.class);
			Unmarshaller um = context.createUnmarshaller();
			bookDataFromXML = (BookRegistry) um.unmarshal(new FileReader(BOOK_XML));
			bookDataFromXML.convertDateStringToDate();

		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tREAD FROM book XML FAILED\n");
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println("\n\tBOOK XML FILE NOT FOUND, creating new BookRegistry\n");
			return new BookRegistry();
		}
		return bookDataFromXML;
	}
}
