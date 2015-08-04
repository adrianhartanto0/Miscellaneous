public class CommandFlag extends AbstractCommand  {
	
	private static final int REQ_ARGS = 3;
	
	public CommandFlag(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}
	
	@Override
	public String execute() {
		if (model.flagMessage(Integer.parseInt(flags[2]),flags[1])) {
			return "Success: flagged " + flags[2] + " to " + flags[1];
		} else {
			return "Error: " + "mesage " + flags[2] 
					+ " not flagged to " + flags[1];
		}
	}

	@Override
	public boolean validateArguments() {
		
		if (flags.length != REQ_ARGS) {	
			return false;
		}
		try
		{
			Integer.parseInt(flags[2]);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}

}
