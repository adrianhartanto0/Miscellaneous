
public class CommandChangeRule extends AbstractCommand {
	
	private final int REQ_ARGS = 4;

	public CommandChangeRule(InterfaceClientModel model, final String[] flags)
			throws BadCommandException {
		super(model, flags);
	}
	

	@Override
	public String execute() {
//		System.out.println(flags[3]);
		return model.addRule(flags[1],flags[2],flags[3]);
	}
	
	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

}
