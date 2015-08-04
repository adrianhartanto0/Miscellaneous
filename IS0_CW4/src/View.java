import java.util.Date;
import java.util.Scanner;

public class View {
	private Controller controller;
	private static final String PROMPT = "?> ";
	private Scanner input;

	public View(Controller controller) {
		this.controller = controller;
		input = new Scanner(System.in);
	}

	public void getInput() {
		String response = "";
		String userInput = "";
		System.out.println("===== AE1ISO Mail Client ====");

		// special cases needed for quit and compose.
		while (!response.equals(CommandQuit.QUIT_MESSAGE)) {
			userInput = getUserInput();
			response = controller.processInput(userInput);
			System.out.println(response);
		}
	}

	public void compose(CommandCompose command) {
		Message message = new Message();
		Date date = new Date();

		System.out.println("To: ");
		message.setRecipient(input.nextLine());
		System.out.println("From: ");
		message.setFrom(input.nextLine());
		System.out.println("Subject: ");
		message.setSubject(input.nextLine());
		System.out.println("Body: ");
		message.setBody(input.nextLine());
		message.setDate(date);
		command.setMessage(message);
	}
	
	public void reply(CommandReply reply, InterfaceMessage message)
	{
		Message message1 = new Message();
		Date date = new Date();
		System.out.println("To:" + message.getFrom());
		System.out.println("From:"+ message.getRecipient());
		System.out.println("RE: " + message.getSubject());
		System.out.println("Body: ");
		message1.setBody(input.nextLine());
		message1.setDate(date);
		reply.setMessage(message1);
	}

	private String getUserInput() {
		System.out.print(PROMPT);
		return input.nextLine();
	}
}
