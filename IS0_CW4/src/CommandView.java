
public class CommandView extends AbstractCommand {

	private static final int REQ_ARGS = 2;
	int messageId;

	public CommandView(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);

	}

	@Override
	public String execute() {

		InterfaceMessage message = model.getMessage(messageId);
		if (message != null) {
			message.markRead(true);
			return model.getMessage(messageId).toString();
		} else {
			return "Error: Message does not exist";
		}
	}

	@Override
	public boolean validateArguments() {
		if (flags.length != REQ_ARGS) {
			return false;
		}

		try {
			messageId = Integer.parseInt(flags[1]);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
