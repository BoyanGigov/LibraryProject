package com.library.xml.userRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.library.data.UserRegistry;

public class UserMapXML {

	private static final String USER_XML = "./userRegistry-jaxb.xml";

	public static void writeToXML(UserRegistry userRegistry) {
		try {
			JAXBContext context = JAXBContext.newInstance(UserRegistry.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(userRegistry, new File(USER_XML));
		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tWRITE TO XML FAILED\n");
			System.exit(0);
			
		}
	}

	public static UserRegistry readFromXML() {
		UserRegistry userDataFromXML = null;
		try {
			JAXBContext context = JAXBContext.newInstance(UserRegistry.class);
			;
			Unmarshaller um = context.createUnmarshaller();
			userDataFromXML = (UserRegistry) um.unmarshal(new FileReader(USER_XML));
			
		} catch (JAXBException e) {
			//e.printStackTrace();
			System.out.println("\n\tREAD FROM user XML FAILED\n");
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println("\n\tUSER XML FILE NOT FOUND, creating new UserRegistry\n");
			return new UserRegistry();
		}
		return userDataFromXML;
	}
}
