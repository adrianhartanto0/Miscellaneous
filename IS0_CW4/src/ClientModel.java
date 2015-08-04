import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import emailConnector.InterfaceConnector;

public class ClientModel implements InterfaceClientModel {

	private static final String INBOXFOLDER = "inbox";
	private static final String SENTFOLDER = "sent";
	private static final String GREENFOLDER = "green";
	private static final String REDFOLDER = "red";
	private static final String BLUEFOLDER = "blue";
	private static final String MESSAGERECIPIENT = "recipient";
	private static final String MESSAGESENDER = "sender";
	private static final String MESSAGESUBJECT = "subject";
	private static final String MESSAGEBODY ="body";
	private static final String ERROR = "Error: Cannot undo command: ";
	private static final String ERRORUNDO = "Error: no command to undo";
	private static final String ERRORREDO = "Error: no command to redo";
	
	InterfaceMessage lastViewed;
	private String currentCommand="";
	private String currentCommand1="";
	
	InterfaceConnector connector;
	HashMap<String, InterfaceFolder> folders = new HashMap<String, InterfaceFolder>();
	HashMap<String,InterfaceFolder> smartfolders = new HashMap<String,InterfaceFolder>();
	ArrayList<String[]> rules = new ArrayList<String[]>();
	ArrayList<String> undoCommands = new ArrayList<String>();

	MyStack stack = new MyStack();
	MyStack redoStack = new MyStack();

	ArrayList<String> names = new ArrayList<String>();
	
	ArrayList<Integer> knownMessageIds;
	String activeFolderName;
	String folder1;

	public ClientModel(InterfaceConnector connector) {
		
		undoCommands.add("list");
		undoCommands.add("delete");
		undoCommands.add("compose");
		undoCommands.add("sort");
		undoCommands.add("reply");
		undoCommands.add("cr");
		undoCommands.add("receive");
		undoCommands.add("quit");
		undoCommands.add("listfolders");
		undoCommands.add("flag");

		names.add(MESSAGERECIPIENT);
		names.add(MESSAGESENDER);
		names.add(MESSAGESUBJECT);
		names.add(MESSAGEBODY);
		
		this.connector = connector;
		smartfolders.put(GREENFOLDER, new Folder());
		smartfolders.put(REDFOLDER, new Folder());
		smartfolders.put(BLUEFOLDER, new Folder());
		
		folders.put(INBOXFOLDER, new Folder());
		folders.put(SENTFOLDER, new Folder());
		activeFolderName = "inbox";
		knownMessageIds = new ArrayList<Integer>();
	}

	@Override
	public boolean changeActiveFolder(String name) {
		if (this.folders.containsKey(name) || this.smartfolders.containsKey(name)) {
			asdfsq();
			stack.addToStack( "cf " + activeFolderName);
			this.activeFolderName = name;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getActiveFolderName() {
		return activeFolderName;
	}

	@Override
	public boolean createFolder(String name) {
		if (!folders.containsKey(name)) {
			asdfsq();
			folders.put(name, new Folder());
			stack.addToStack("remove " + name);
//			redoStack.addToStack("mkf " + name);
			return true;
		}
		return false;

	}

	public Set<String> getFolderNames() {
		asdfsq();
		stack.addToStack("listfolders");
		return folders.keySet();
	}

	@Override
	public boolean delete(int messageId) {
		
		if(this.smartfolders.containsKey(activeFolderName)){
			asdfsq();
			stack.addToStack("delete " + messageId);
			this.smartfolders.get(activeFolderName).delete(messageId);
			connector.markMessageForDeleting(messageId);
			for(String names : getFolderNames()){
				if(getFolder(names).delete(messageId)){
					continue;
				}
			}
			return true;	
		}
		
		if(!this.folders.get(activeFolderName).getMessage(messageId).getColor().equals("noflag")){
			this.smartfolders.get(this.folders.get(activeFolderName).getMessage(messageId).getColor()).delete(messageId);
		}
		
		
		if (this.folders.get(activeFolderName).delete(messageId)) {
			
			asdfsq();
			stack.addToStack("delete " + messageId);
			connector.markMessageForDeleting(messageId);
			return true;
		}

		return false;
	}

	@Override
	public boolean renameFolder(String oldName, String newName) {
		if(oldName.equals(INBOXFOLDER) || oldName.equals(SENTFOLDER)){
			return false;
		} else if (folders.containsKey(oldName) && !folders.containsKey(newName)) {
			asdfsq();
			stack.addToStack("rename " + newName + " " + oldName);
			folders.put(newName, folders.remove(oldName));
			return true;
		} else{
			return false;
		}
	}

	@Override
	public Collection<InterfaceMessage> getMessages() {
		
		if(smartfolders.containsKey(activeFolderName)){
			stack.addToStack("list");
			return smartfolders.get(activeFolderName).getMessages();
		}
		asdfsq();
		stack.addToStack("list");
//		redoStack.addToStack("list");
		return folders.get(activeFolderName).getMessages();
	}

	@Override
	public boolean mark(int messageId, boolean read) {
		InterfaceMessage message = folders.get(activeFolderName).getMessage(
				messageId);
		if (message != null) {
			message.markRead(read);
			asdfsq();
			String read1 = read ? "-u" : "-r";
			stack.addToStack("mark " + read1 + " " + messageId);
//			String read2 = read ? "-r" : "-u";
//			redoStack.addToStack("mark " + read2 + " " + messageId);
			return true;
		}
		return false;
	}

	@Override
	public InterfaceMessage getMessage(int messageId) {
		asdfsq();
		InterfaceMessage message = folders.get(activeFolderName).getMessage(messageId);
		if(message != null){
			lastViewed = message;
		}
		return message;
	}

	@Override
	public boolean move(int messageId, String folderName) {
		
		if(smartfolders.containsKey(activeFolderName) || smartfolders.containsKey(folderName)){
			return false;
		}

		InterfaceFolder folder = folders.get(folderName);
		InterfaceMessage message = folders.get(activeFolderName).getMessage(messageId);

		if (folder == null || message == null) {
			return false;
		}
		asdfsq();
		String qweasd = "move " + messageId + " " + activeFolderName;
		folder1 = folderName;
		stack.addToStack(qweasd);
		folder.addMessage(message);
		folders.get(activeFolderName).delete(messageId);
		return true;
	}

	@Override
	public boolean checkForNewMessages() {
		int messageId;
		String[] messages;
		try {
			messages = connector.listMessages().split("\r\n");
			for (String message : messages) {
				if (!message.isEmpty()) {
					messageId = Integer.parseInt(message);
					if (!knownMessageIds.contains(messageId)) {
						knownMessageIds.add(messageId);
						this.folders.get(INBOXFOLDER).addMessage(
								parseMessage(connector.retrMessage(messageId),
										messageId));
					}
				}
			}
//			System.out.println(currentCommand);
			addRule1();
			asdfsq();
			stack.addToStack("receive");
//			redoStack.addToStack("receive");
			
//			for(InterfaceMessage message12:folders.get(inboxFolder).getMessages())
//		    {
//		    	System.out.println("Inbox" + message12.getSubject());
//		    }
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	private InterfaceMessage parseMessage(String messageStr, int messageId) {
		Message message;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String[] messageString = messageStr.split("\r\n", 6);
		try {
			message = new Message();
			message.setId(messageId);
			message.setRecipient(messageString[0].split(":", 2)[1]);
			message.setFrom(messageString[1].split(":", 2)[1]);
			message.setDate(dateFormat.parse(messageString[2].split(":", 2)[1]));
			message.setSubject(messageString[3].split(":", 2)[1]);
			message.setBody(messageString[5]);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return message;
	}
	
	public void asdfsq(){
		if(!currentCommand.equals("redo")){
			redoStack.returnStack().removeAllElements();
		}
	}

	@Override
	public boolean sendMessage(InterfaceMessage message) {
		asdfsq();
		stack.addToStack(currentCommand1);
		folders.get("sent").addMessage(message);
		// message ID provided by the server once sent.
		String successResponse = connector.sendMessage(message.toString());
		int providedId = Integer.parseInt(successResponse.split(" ")[1]);
		message.setId(providedId);

		return true;
	}
	
	@Override
	public String undo(){
		String qwerty= "";
		String response = null;
		
		if(stack.returnStack().size() == 0){
			response = ERRORUNDO;
		}else{
			//System.out.println("Size1 is " + this.stack.returnStack().size());
			String command = stack.returnItem();
			//System.out.println("Size1 is " + stack.returnStack().size());
			String[] receive = command.split(" ");
			
			if(command.indexOf("remove") != -1){
				removeFolder(receive[1]);
				response = response(receive);
				String masdas = "mkf " + receive[1];
				redoStack.addToStack(masdas);
			}else if(command.indexOf("alias") != -1){
				redoStack.addToStack(command);
				CommandFactory.removeAlias(receive[2]);
				response = response(receive);
			}else if (command.indexOf("move") != -1){
				qwerty = activeFolderName;
				activeFolderName = folder1;
				response = processUndo(command,receive);
				activeFolderName = qwerty;
			}else{
				response = processUndo(command,receive);
			}
			//System.out.println(qwer);
		}
		return response;
	}
	
	@Override
	public String redo(){
		String response = null;
		//System.out.println("Size1 is " + redoStack.returnStack().size());
		
		if(redoStack.returnStack().size() == 0){
			response = ERRORREDO;
		}else{
			//System.out.println("Size is " + redoStack.returnStack().peek());
			//System.out.println("Size is " + redoStack.returnStack().size());
			String command = redoStack.returnItem();
			
			try{
				AbstractCommand command1 = CommandFactory.buildCommand(command);
				response = command1.execute();
			}catch(BadCommandException bce){
				response = bce.getMessage();
			}
//			if(redoStack.returnStack().size() != 0){
//				redoStack.returnItem();
//			}
			//System.out.println("Size1 is " + this.redoStack.returnStack().size());
		}
		//System.out.println("command is " + currentCommand);
		
		currentCommand = "";
		//System.out.println("command is " + currentCommand);
		return response;
	}
	
	private String processUndo(String command,String[] receive){
		String response;
		//System.out.println("Size my redo:" + redoStack.returnStack().size());
		try{
			AbstractCommand command1 = CommandFactory.buildCommand(command);
			
			if(undoCommands.contains(command1.flags[0])){
				//System.out.println("Size2 is " + this.stack.returnStack().size());
				redoStack.addToStack(command);
				response = ERROR + command1.getClass().toString();
			}else{
				command1.execute();
				redoStack.addToStack(stack.returnItem());
				response = response(receive); 
				//System.out.println("Size my redo2:" + redoStack.returnStack().size());
			}
		}catch (BadCommandException bce){
			response = bce.getMessage();
		}
		//System.out.println("Size my redo1:" + redoStack.returnStack().size());
		return response;
	}
	
	private String response(String[] input){
		if(input.length == 2){
			return "Success: tried to " + input[0] + " " + input[1];
		}else if(input[0].equals("mark")){
			String qwerty = input[1].equals("-u") ? "unread" : "read";  
			return "Success: tried to " + input[0] + " " + input[2] + " as " + qwerty;
		}
		return "Success: tried to "+ input[0] + " " + input[1] + " as " + input[2];
	}
//	
//	
//	@Override
//	public boolean addlastViewedMessage(InterfaceMessage lastViewed)
//	{
//		this.lastViewed = lastViewed;
//		return true;
//	}
//	
	@Override
	public InterfaceMessage getLastViewed()
	{
		return this.lastViewed;
	}
	
	public boolean flagMessage(int messageID,String color)
	{
		InterfaceMessage message = null;
		
		if(this.smartfolders.containsKey(color) && this.folders.get(activeFolderName) != null && this.folders.get(activeFolderName).getMessage(messageID) != null )
		{
			message = this.folders.get(activeFolderName).getMessage(messageID);
			if(!message.getColor().equals("noflag")){
				this.smartfolders.get(message.getColor()).delete(messageID);
			}
			asdfs(message, color);
			asdfsq();
			stack.addToStack("flag " + color + " " + messageID);
			return true;
		}
		else if(this.smartfolders.containsKey(color) && this.smartfolders.get(activeFolderName) != null && this.smartfolders.get(activeFolderName).getMessage(messageID) != null  )
		{
			message = this.smartfolders.get(activeFolderName).getMessage(messageID);
			asdfs(message,color);
			asdfsq();
			stack.addToStack("flag " + color + " " + messageID);
			this.smartfolders.get(activeFolderName).delete(messageID);
			return true;
		}

		return false;
	}
	
	private void asdfs(InterfaceMessage message, String color)
	{
		smartfolders.get(color).addMessage(message);
		message.setColor(color);
	}

	
	private String addRule1()
	{
		InterfaceFolder folder = getFolder(INBOXFOLDER);
		InterfaceMessage message1;
		int count = 0;
		int size = folder.getMessages().size();
		
		//System.out.println("ASDASD" + this.rules.size());
	    for(int index = 0;index < this.rules.size();index++)
	    {
	    	String[] rule = rules.get(index);
	    	//System.out.println("ASDASD " + folder.getMessages().size());	    	
//	    	while(count < folder.getMessages().size() )
			for(int index1 = 0; index1 < size;index1++)
			{
				message1 = folder.getMessage(index1);
//				System.out.println(message1.getSubject());
//				System.out.println(message1.getRecipient());
//				System.out.println("ASDASD " + index1);
				if(message1 != null && help(message1,rule))
				{	
					folder.delete(index1);
					count++;
				}
			}
	      }
		return "Success: Added rule and moved " + count + " messages";
	}
	
	private boolean help(InterfaceMessage message1,String[] rule)
	{
//		System.out.println(rule[2]);
//		System.out.println(message1.getRecipient());
//		System.out.println("Index is " + message1.getRecipient().contains(rule[2]) );
		
		
		if(rule[1].equals("recipient") && message1.getRecipient().indexOf(rule[2]) != -1){
			getFolder(rule[0]).addMessage(message1);
			return true;
		//	folder.delete(index1);
		}if(rule[1].equals("sender") && message1.getFrom().indexOf(rule[2]) != -1){
			getFolder(rule[0]).addMessage(message1);
			return true;
		//	folder.delete(index1);
		}if(rule[1].equals("subject") && message1.getSubject().indexOf(rule[2]) != -1){
			getFolder(rule[0]).addMessage(message1);
			return true;
		//	folder.delete(index1);
		}if(rule[1].equals("body") && message1.getBody().indexOf(rule[2]) != -1){
			getFolder(rule[0]).addMessage(message1);
			return true;
		//folder.delete(index1);
		}
		return false;
	}
	
	@Override
	public String addRule(String folderName, String section, String search)
	{		
		if(getFolder(folderName) == null){
			return "Error: Folder does not exist";
		}
		else if(!names.contains(section)){
			return "Error: Invalid part of message";
		}
		String[] rule = {folderName,section,search};
	    rules.add(rule);
		asdfsq();
		stack.addToStack("cr " + folderName + " " + section + " " + search);
		return addRule1();
	}
	
	@Override
	public void addToStack(String input){
		stack.addToStack(input);
		
	}

	@Override
	public void sortByDate(boolean ascending) {
		asdfsq();
		stack.addToStack("sort -d");
		folders.get(activeFolderName).sortByDate(false);
	}

	@Override
	public void sortBySubject(boolean ascending) {
		asdfsq();
		stack.addToStack("sort -s");
		folders.get(activeFolderName).sortBySubject(true);
	}

	@Override
	public InterfaceFolder getFolder(String folderName) {
		return folders.get(folderName);
	}
	
	private void removeFolder(String folderName){
		folders.remove(folderName);
	}
	public void setCurrentCommand(String command ){
		//System.out.println("The command is " + command);
		
		if(command.equals("reply") || command.equals("comppose")){
			this.currentCommand1 = command;
		}
		if(command.equals("redo")){
//			System.out.println("The command is " + command);
			this.currentCommand = command;
		}
	}

}
