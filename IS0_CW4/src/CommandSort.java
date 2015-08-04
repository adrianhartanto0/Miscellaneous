
public class CommandSort extends AbstractCommand {

	boolean byDate;
	private static final int REQ_ARGS = 2;

	public CommandSort(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
	}

	@Override
	public String execute() {
		if (byDate) {
			model.sortByDate(false);
		} else {
			model.sortBySubject(true);
		}
		return "Success: Messages sorted.";
	}

	@Override
	public boolean validateArguments() {
		if (flags.length != REQ_ARGS) {
			return false;
		}

		if (flags[1].equals("-d")) {
			byDate = true;
			return true;
		} else if (flags[1].equals("-s")) {
			byDate = false;
			return true;
		}

		return false;
	}

}
