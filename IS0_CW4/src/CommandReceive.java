public class CommandReceive extends AbstractCommand {
	private static final int REQ_ARGS = 1;
	
	public CommandReceive(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public String execute() {
		if (model.checkForNewMessages()) {
			return "Successfully updated";
		} else {
			return "Error: Could not connect to server.";
		}
	}
	
	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

}
