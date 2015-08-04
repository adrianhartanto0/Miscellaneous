//import java.util.HashMap;
//import java.util.Map;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;

public class Controller {
	private InterfaceClientModel model;
	private CommandFactory commandFactory;
	private View view;
	private InterfaceConnector connector;

	public Controller() {
		view = new View(this);
		connector = StandardConnector.getInstance();
		commandFactory = CommandFactory.getInstance();
		model = new ClientModel(connector);
		commandFactory.setReferences(model, view);
		commandFactory.addMethod();
	}

	public void begin() {		
		view.getInput();
	}

	public String processInput(String theInput) {
		String response =null;
		AbstractCommand command;

		try {
			command = CommandFactory.buildCommand(theInput);
			response = command.execute();	
		} catch (BadCommandException bce) {
			//System.out.println("asdasd");
			response = bce.getMessage();
		}
		return response;
	}
}
