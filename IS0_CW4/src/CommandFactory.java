import java.util.HashMap;
import java.util.Map;

public class CommandFactory implements InterfaceMethod{

	private static CommandFactory instance; // singleton

	private static InterfaceClientModel model;
	private View view;
	
	static Map<String, String> listOfAlias = new HashMap<String, String>();
	static Map<String, InterfaceMethod> listOfCommand = new HashMap<String, InterfaceMethod>();

	private static final String COMMAND_CHANGE_FOLDER = "cf";
	private static final String COMMAND_LIST_DIRS = "listfolders";
	private static final String COMMAND_LIST_MESSAGES = "list";
	private static final String COMMAND_RENAME = "rename";
	private static final String COMMAND_SORT = "sort";
	private static final String COMMAND_MAKE_FOLDER = "mkf";
	private static final String COMMAND_MOVE = "move";
	private static final String COMMAND_COMPOSE = "compose";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_VIEW = "view";
	private static final String COMMAND_SEND_REC = "receive";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_QUIT = "quit";
	private static final String COMMAND_REPLY = "reply";
	private static final String COMMAND_FLAG = "flag";
	private static final String COMMAND_ALIAS = "alias";
	private static final String COMMAND_CHANGE = "cr";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";

	public static CommandFactory getInstance() {
		if (instance == null) {
			instance = new CommandFactory();
		}
		return instance;
	}

	

	private CommandFactory() {
		// singleton.
	}
	
	public static boolean addAlias(String input1,String alias)
	{
		if(listOfCommand.containsKey(input1)){
			//System.out.println("ASD1");
			model.asdfsq();
			//System.out.println("ASD2");
			listOfAlias.put(alias,input1);
			//System.out.println("ASD3");
			model.addToStack("alias " + input1 + " "+  alias);
			//System.out.println("ASD4");
			return true;
		}else if (listOfAlias.containsKey(input1)){
			model.asdfsq();
			listOfAlias.put(alias, input1);
			model.addToStack("alias " + input1 + " " + alias);
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean removeAlias(String input1){
		if(listOfAlias.containsKey(input1)){
			listOfAlias.remove(input1);
			return true;
		}
		return false;
	}
	
	
	
	public boolean checkIfAvailable(String alias)
	{
		return (listOfCommand.containsKey(alias) || listOfAlias.containsKey(alias));
	}
	
	public void addMethod()
	{
		listOfCommand.put(COMMAND_CHANGE_FOLDER, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandChangeFolder(model, commandInput);
			}
		});
		
		listOfCommand.put(COMMAND_LIST_DIRS, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandListFolders(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_LIST_MESSAGES, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandListMessages(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_RENAME, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandRename(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_SORT, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandSort(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_MAKE_FOLDER, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandMakeFolder(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_VIEW, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
					return new CommandView(model, commandInput);
			}
		});
//		
		listOfCommand.put(COMMAND_MOVE, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandMoveMessage(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_COMPOSE, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandCompose(model, view, commandInput);
			}
		});
		listOfCommand.put(COMMAND_DELETE, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput)throws BadCommandException
			{
				return new CommandDelete(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_SEND_REC, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandReceive(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_MARK, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
					return new CommandMark(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_QUIT, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
					return new CommandQuit(model, commandInput);
			}
		});
		listOfCommand.put(COMMAND_REPLY, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandReply(model,view,commandInput);
			}
		});
		listOfCommand.put(COMMAND_FLAG, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandFlag(model,commandInput);
			}
		});
		listOfCommand.put(COMMAND_ALIAS, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			{
				return new CommandAlias(model,commandInput);
			}
		});
		listOfCommand.put(COMMAND_CHANGE, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException
			
			{
				return new CommandChangeRule(model,commandInput);
			}
		});
		listOfCommand.put(COMMAND_UNDO, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException	
			{
				return new CommandUndo(model,commandInput);
			}
		});
		listOfCommand.put(COMMAND_REDO, new InterfaceMethod(){
			public AbstractCommand callMethod(String[] commandInput) throws BadCommandException	
			{
				return new CommandRedo(model,commandInput);
			}
		});
	}
	
	public void setReferences(InterfaceClientModel model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public static InterfaceMethod getMethod(String command){
		
		if(listOfCommand.containsKey(listOfAlias.get(command))){
			return listOfCommand.get(listOfAlias.get(command));
		}else if(listOfAlias.containsKey(command)){
			return getMethod(listOfAlias.get(command));
		}
		return null;
	}
	
	

	public static AbstractCommand buildCommand(String command)
			throws BadCommandException {

		String[] commandInput = command.split(" ");
		model.setCurrentCommand(commandInput[0]);
		if(listOfCommand.containsKey(commandInput[0]) ){
			
			return listOfCommand.get(commandInput[0]).callMethod(commandInput);
		}
		else if (listOfAlias.get(commandInput[0]) != null){
			//listOfCommand.get(listOfAlias.get(commandInput[0])).callMethod(commandInput); 
			return getMethod(commandInput[0]).callMethod(commandInput);
		}
		else{
			return new CommandBad(model, commandInput).setMessage("Error: Not a valid command: "
					+ commandInput[0]);
		}
	}

	@Override
	public AbstractCommand callMethod(String[] input) {
		// TODO Auto-generated method stub
		return null;
	}
}
