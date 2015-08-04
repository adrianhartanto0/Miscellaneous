public class CommandQuit extends AbstractCommand {
	public static final String QUIT_MESSAGE = "Quitting...";
	private static final int REQ_ARGS = 1;

	public CommandQuit(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
	}

	@Override
	public String execute() {
		return QUIT_MESSAGE;
	}
	
	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

}
