package com.library.xml.booktakerRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.library.data.BooktakerRegistry;

public class BooktakerMapXML {

	private static final String BOOKING_XML = "./booktakerRegistry-jaxb.xml";

	public static void writeToXML(BooktakerRegistry booktakerRegistry) {
		try {
			JAXBContext context = JAXBContext.newInstance(BooktakerRegistry.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//Write to File
			m.marshal(booktakerRegistry, new File(BOOKING_XML));
		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tWRITE TO booktakerRegistry XML FAILED\n");
			System.exit(0);
		}
	}

	public static BooktakerRegistry readFromXML() {
		BooktakerRegistry bookingDataFromXML = null;
		try {
			JAXBContext context = JAXBContext.newInstance(BooktakerRegistry.class);
			;
			Unmarshaller um = context.createUnmarshaller();
			bookingDataFromXML = (BooktakerRegistry) um.unmarshal(new FileReader(BOOKING_XML));
			
		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tREAD FROM booking XML FAILED\n");
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println("\n\tbooking XML FILE NOT FOUND, creating new BooktakerRegistry\n");
			return new BooktakerRegistry();
		}
		return bookingDataFromXML;
	}
}
