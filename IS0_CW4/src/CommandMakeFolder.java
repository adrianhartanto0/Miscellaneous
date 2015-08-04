
public class CommandMakeFolder extends AbstractCommand {

	private static final int REQ_ARGS = 2;

	public CommandMakeFolder(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public String execute() {
		if (model.createFolder(flags[1])) {
			return "Success: Created folder " + flags[1];
		} else {
			return "Error: Folder " + flags[1] + " exists already.";
		}
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}
}
