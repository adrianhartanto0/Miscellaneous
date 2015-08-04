
public class CommandDelete extends AbstractCommand {

	private static final int REQ_ARGS = 2;
	private int messageId;

	public CommandDelete(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
	}

	@Override
	public String execute() {
		if (model.delete(messageId)) {
			return "Successfully deleted " + messageId;
		} else {
			return "Error: check message with ID '" + messageId + "' exists.";
		}
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
}
