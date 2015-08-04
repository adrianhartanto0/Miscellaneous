
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;




public class View {

	private InterfaceClientModel model;
	private static final String PROMPT = "?> ";
	private static final int THREEARGUMENTS = 3;
	private static final String FOLDER = "===Folders===";
	private static final String MESSAGES = "===Messages===";
	private Scanner input;
	InterfaceCommand command;

	
	public View(InterfaceClientModel model) {
		this.model = model;
		input = new Scanner(System.in);
	}

	public void start() {
		
		command = new Command(this.model);
		command.addMethod();
		
		
		String userCommand = "";
		this.model.createFolder("inbox");
		this.model.createFolder("sent");
		this.model.changeActiveFolder("inbox");
		System.out.println("===== AE1ISO Mail Client ====");
		
		while(!userCommand.equals("quit"))
		{
			userCommand = getUserInput();
			command.parse(userCommand);
			
			if(!command.checkArguments())
			{
				System.out.println("Error: Invalid arguments");
				continue;
			}
			if(!command.checkCommands(command.getCommand()))
			{
				System.out.println("Error: not a valid commands");
				continue;
			}
			process(command.process(command.getCommand(),command.getInput()));
		}
			
		System.out.println("Quitting...");
	}
	
	private String getUserInput() {
		System.out.print(PROMPT);
		return input.nextLine();
	}	
	
	public void process(boolean success)
	{
		switch(command.getCommand())
		{
			case "cf":
				change(success);
				break;
			case "listfolders":
				printFolders();
				break;
			case "rename":
				rename(success);
				break;
			case "mkf":
				createFolder(success);
				break;
			case "move":
				move(success);
				break;
			case "list":
				list();
				break;
			case "delete":
				delete(success);
				break;
			case "receive":
				 getNewMessage(success);
				 break;
			case "view":
				view(command.getInput());
				break;
			case "mark":
				mark(success);
				break;
			case "sort":
				sort(success);
				break;
			case "compose":
				compose(command.getInput());
				break;
			case "reply":
				reply(command.getInput());
				break;
			default:
				break;
		}
	}
	
	private void sort(boolean success)
	{
		if(success)
		{
			System.out.println("Success: Messages sorted.");
		}
		else
		{
			System.out.println("Error: Invalid arguments");
		}
	}

	private void mark(boolean success)
	{
		if(success)
		{
			System.out.println("Success: message is marked");
		}
		else
		{
			System.out.println("Error: message is not marked.");
		}
	}
	
	private void compose(String[] input1)
	{
		if(input1.length == 1)
		{
			Message message = new Message();
			System.out.print("To: ");
			message.setRecipient(input.nextLine());
			System.out.print("From: ");
			message.setFrom(input.nextLine());
			System.out.print("Subject: ");
			message.setSubject(input.nextLine());
			asdfs(message);
		}
		else
		{
			System.out.println("Error: Invalid arguments");
		}
	}
	
	private void sent(InterfaceMessage message)
	{
		if(this.model.sendMessage(message))
		{
			System.out.println("Success: sent");
		}
		else
		{
			System.out.println("Error: message not sent");
		}
	}
	
	private void asdfs(InterfaceMessage msg)
	{
		System.out.print("Body: ");
		msg.setBody(input.nextLine());
		msg.markRead(true);
		msg.setDate(new Date());
		sent(msg);
	}
	
	private void reply(String[] input)
	{
		try
		{
			if( input.length == 2 && Integer.parseInt(input[1]) >= 0 && this.model.getMessage(Integer.parseInt(input[1])) != null)
			{
				InterfaceMessage msg = new Message();
				msg.setRecipient(this.model.getMessage(Integer.parseInt(input[1])).getRecipient());
				msg.setFrom(this.model.getMessage(Integer.parseInt(input[1])).getFrom());
				msg.setSubject(this.model.getMessage(Integer.parseInt(input[1])).getSubject());
				asdfs(msg);
			}
			else
			{
				System.out.println("Error: message can't be replied");
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("Error: Invalid arguments");
		}		
	}
	
	private void delete(boolean success)
	{
		if(success && !command.getInput()[1].equals("-r") )
		{
			System.out.println("Successfully deleted " + command.getInput()[1]);
		}
		else if(command.getInput()[1].equals("-r") && success)
		{
			System.out.println("Success: Folder " + command.getInput()[2] + " deleted");
		}
		else if(command.getInput()[1].equals("-r") && !success)
		{
			System.out.println("Error: Folder not deleted.");
		}
		else
		{
			System.out.println("Error: Message not deleted.");
		}

	}
	
	private void change(boolean success)
	{
		if(success)
		{
			System.out.println("Success: Changed folder to " + command.getInput()[1]);
		}
		else
		{
			System.out.println("Error: Failed change folder "+ command.getInput()[1]);
		}
	}
	
	private void move(boolean success)
	{
		if(success)
		{
			System.out.println("Success: Moved " + command.getInput()[1] + " to " + command.getInput()[2]);
			
		}
		else
		{
			System.out.println("Error: message can't be moved");
		}
	}
	
	private void printFolders()
	{
		System.out.println(FOLDER);
		for(String name: this.model.getFolderNames())
		{
			System.out.println(name);
		}
	}
	
	
	private void list()
	{
		System.out.println(MESSAGES);
		if(this.model.getMessages() != null)
		{
			for(InterfaceMessage mem : this.model.getMessages())
			{
				String read = mem.isRead() ? " R " : " U ";
				String date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(mem.getDate());
				System.out.println(mem.getId() + ":" + read + ":" + date + ":" + " " + mem.getSubject());
			}
		}
		else
		{
			System.out.println("Error: no current active folder");
		}
	}
	
	private void view(String[] input)
	{
		try
		{
			if(Integer.parseInt(input[1]) >= 0 && input.length == 2 && this.model.getMessage(Integer.parseInt(input[1])) != null )
			{
				InterfaceMessage message = this.model.getMessage(Integer.parseInt(input[1]));
				message.markRead(true);
				String date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(message.getDate());
				String message1 = "To: " + message.getRecipient() + "\n" + "From: " + message.getFrom() + "\n" + "Date: " + date + "\n" + "Subject: " + message.getSubject() + "\n"; 
				System.out.println(message1 + "\n"+ message.getBody());
			}
			else
			{
				System.out.println("Error: Message does not exist");
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("Error: Not a valid argument");
		}
	}

	private void rename(boolean success)
	{
		if(success)
		{
			System.out.println("Success: Renamed folder " + command.getInput()[1] + " to " + command.getInput()[2]);
		}
		else
		{
			System.out.println("Error: Folder can't be renamed");
		}	
	}
	
	private void createFolder(boolean success)
	{
		if(success)
		{
			System.out.println("Success: Created folder " + command.getInput()[1]);
		}
		else if(command.getInput().length >= THREEARGUMENTS)
		{
			System.out.println("Error: Folder " +  command.getInput()[1] + " can't be made");
		}
		else
		{
			System.out.println("Error: Folder " +  command.getInput()[1] + " exists already");
		}
	}
	
	private void getNewMessage(boolean success)
	{
		 if(success)
		 {
			 System.out.println("Successfully updated");
		 }
		 else
		 {
			 System.out.println("Error: Failed to connect ");
		 }
	}
}
