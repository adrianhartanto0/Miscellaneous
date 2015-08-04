public abstract class AbstractCommand {
	protected static final String INVALID_ARGS = "Error: Invalid args.";
	protected String[] flags;
	protected InterfaceClientModel model;
	protected View view;

	public AbstractCommand(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		this.model = model;
		this.flags = flags;
		if (!validateArguments()) {
			throw new BadCommandException(INVALID_ARGS);
		}
	}

	public AbstractCommand(InterfaceClientModel model, View view, String[] flags)
			throws BadCommandException {
		this(model, flags);
		this.view = view;
	}

	abstract public String execute();

	protected abstract boolean validateArguments();
}
