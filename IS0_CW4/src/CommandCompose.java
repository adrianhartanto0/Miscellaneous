
public class CommandCompose extends AbstractCommand {
	private View view;

	InterfaceMessage message;
	private static final int REQ_ARGS = 1;

	public CommandCompose(InterfaceClientModel model, View view, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
		this.view = view;
	}

	@Override
	public String execute() {
		view.compose(this);
		if (model.sendMessage(message)) {
			return "Success: sent";
		} else {
			return "Failed: Could not send.";
		}
	}

	public void setMessage(Message message1) {
		this.message = message1;
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

}
