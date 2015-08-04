
public class CommandMark extends AbstractCommand {

	private static final int REQ_ARGS = 3;
	private int messageId;
	private boolean read;
	private String successMessage;

	public CommandMark(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public String execute() {
		if (model.mark(messageId, read)) {
			return successMessage;
		} else {
			return "Error: check " + messageId + " exists.";
		}
	}

	@Override
	public boolean validateArguments() {
		if (flags.length != REQ_ARGS) {
			return false;
		}

		try {
			messageId = Integer.parseInt(flags[2]);
		} catch (NumberFormatException e) {
			return false;
		}

		if (flags[1].equalsIgnoreCase("-r")) {
			read = true;
			successMessage = "Success: marked " + messageId + " as read";
		} else if (flags[1].equals("-u")) {
			read = false;
			successMessage = "Success: marked " + messageId + " as unread";
		} else {
			return false;
		}

		return true;
	}
}
