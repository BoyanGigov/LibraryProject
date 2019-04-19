package com.library.xml.authorRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.library.data.AuthorRegistry;

public class AuthorMapXML {

	private static final String AUTHOR_XML = "./authorRegistry-jaxb.xml";

	public static void writeToXML(AuthorRegistry authorRegistry) {
		try {
			JAXBContext context = JAXBContext.newInstance(AuthorRegistry.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//Write to File
			m.marshal(authorRegistry, new File(AUTHOR_XML));
		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tWRITE TO author XML FAILED\n");
			System.exit(0);
		}
	}

	public static AuthorRegistry readFromXML() {
		AuthorRegistry authorDataFromXML = null;
		try {
			JAXBContext context = JAXBContext.newInstance(AuthorRegistry.class);
			Unmarshaller um = context.createUnmarshaller();
			authorDataFromXML = (AuthorRegistry) um.unmarshal(new FileReader(AUTHOR_XML));
			authorDataFromXML.convertDateStringToDate();

		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tREAD FROM author XML FAILED\n");
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println("\n\tAUTHOR XML FILE NOT FOUND, creating new AuthorRegistry\n");
			return new AuthorRegistry();
		}
		return authorDataFromXML;
	}
}
