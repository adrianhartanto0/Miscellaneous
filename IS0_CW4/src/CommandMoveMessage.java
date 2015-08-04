
public class CommandMoveMessage extends AbstractCommand {

	private static final int REQ_ARGS = 3;
	private int messageId;

	public CommandMoveMessage(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public boolean validateArguments() {
		if (flags.length != REQ_ARGS) {
			return false;
		}

		try {
			messageId = Integer.parseInt(flags[1]);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}


	@Override
	public String execute() {
		if (model.move(messageId, flags[2])) {
			return "Success: Moved " + messageId + " to " + flags[2];
		} else {
			return "Error: Check message " + messageId + " exists and that "
					+ flags[2] + " is a folder, and that neither the " + "\n" + "currect folder or target folder are smart folder" ;
		}
	}
}
