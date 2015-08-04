
import java.util.Collection;

public class CommandListFolders extends AbstractCommand {

	public final int REQ_ARGS = 1;

	public CommandListFolders(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);

	}

	@Override
	public String execute() {
		StringBuilder stringBuilder = new StringBuilder();
		Collection<String> folders = model.getFolderNames();
		stringBuilder.append("===Folders===\r\n");
		for (String folder : folders) {
			stringBuilder.append(folder + "\r\n");
		}
		return stringBuilder.toString();
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

}
