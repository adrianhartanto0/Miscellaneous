
public class CommandUndo extends AbstractCommand {
 
private static final int REQ_ARGS = 1;
	
	public CommandUndo(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public String execute() {
		
		return model.undo();
		
/*				
		if(model.undo()){
			
		}
		
		
*/		
//		if (model.checkForNewMessage) {
//			return "Successfully updated";
//		} else {
//			return "Error: Could not connect to server.";
//		}
	}
	
	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

	
}
