
public class CommandFolderName extends AbstractCommand {

	public final int REQ_ARGS = 1;

	public CommandFolderName(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);

	}

	@Override
	public String execute() {
		return model.getActiveFolderName();
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

}
