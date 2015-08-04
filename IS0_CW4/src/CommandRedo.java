
public class CommandRedo extends AbstractCommand {
	private static final int REQ_ARGS = 1;
	
	public CommandRedo(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public String execute() {
		
		return model.redo();
//		if (model.redo()) {
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
