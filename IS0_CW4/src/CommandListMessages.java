
import java.text.SimpleDateFormat;
import java.util.Collection;

public class CommandListMessages extends AbstractCommand {

	public final int REQ_ARGS = 1;

	public CommandListMessages(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public String execute() {
		StringBuilder stringBuilder = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");

		Collection<InterfaceMessage> messages = model.getMessages();
		stringBuilder.append("===Messages===\r\n");
		for (InterfaceMessage message : messages) {
			String read = message.isRead() ? "R" : "U";
			
//			if (msg.isRead()) {
//				read = "R";
//			} else {
//				read = "U";
//			}

			stringBuilder.append(message.getId() + ": " + read + ": " + message.getColor() + ": "
					+ dateFormat.format(message.getDate()) + ": "
					+ message.getSubject() + "\r\n");
		}

		if (messages.size() == 0) {
			stringBuilder.append("No messages.");
		}

		return stringBuilder.toString();
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

}
