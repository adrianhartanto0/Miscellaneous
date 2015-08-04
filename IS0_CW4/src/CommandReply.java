
public class CommandReply extends AbstractCommand {
	
	private static final int REQ_ARGS = 1;
	private View view;
	InterfaceMessage message1;
	
	public CommandReply(InterfaceClientModel model,View view,String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
		this.view = view;
	}

	@Override
	public String execute() {
		if(this.model.getLastViewed() != null)
		{
			view.reply(this,this.model.getLastViewed());
			InterfaceMessage msg = setMessageForSending(this.model.getLastViewed(), message1);
			if (this.model.sendMessage(msg)) {
				return "Success: sent";
			} else {
				return "Failed: Could not send.";
			}
		}
		else
		{
			return "Error: " + "Must first view a message to reply";
		}
	}
	
	private InterfaceMessage setMessageForSending(InterfaceMessage message1, InterfaceMessage message2)
	{
		message2.setRecipient(message1.getFrom());
		message2.setFrom(message1.getRecipient());
		message2.setSubject("RE: " + message1.getSubject());
		return message2;
	}
	
	public void setMessage(Message message) {
		this.message1 = message;
	}

	@Override
	public boolean validateArguments() {
//		System.out.println(flags.length == REQ_ARGS);
		return flags.length == REQ_ARGS;
	}
	
//	msg.setRecipient(this.model.getLastViewed().getRecipient());
//	msg.setFrom(this.model.getLastViewed().getFrom());
//	msg.setSubject("RE: " + this.model.getLastViewed().getSubject());

}
