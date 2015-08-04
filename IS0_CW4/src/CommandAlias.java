public class CommandAlias extends AbstractCommand {
	
	private final int REQ_ARGS = 3;
	CommandFactory factory;

	public CommandAlias(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
	}

	
	@Override
	public String execute() {
		
		if(CommandFactory.addAlias(flags[1],flags[2]) ){
			return "Added alias " + flags[2] + " to " + flags[1];
		}else {
			return "Error: alias " + flags[2] + " not added"; 
		}
	}

	@Override
	public boolean validateArguments() {
		factory = CommandFactory.getInstance();
		return (!factory.checkIfAvailable(flags[2]) && flags.length == REQ_ARGS);
	}

}
