import java.io.IOException;
import java.util.*;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;
import emailConnector.WikiConnector;

public class MailClient {

	public static void main(String[] args) {
		InterfaceConnector connector = StandardConnector.getInstance();
		
		// You may use the "wiki connector" for testing or messing with if you wish.
		// Uncomment the line below, and comment out the previous connector.
//		InterfaceConnector connector = WikiConnector.getInstance();

		Random rand = new Random();
		
	
		
		
		
		
		InterfaceClientModel model = new ClientModel(connector);		
		View textBasedView = new View(model);
		textBasedView.start();
	}
}