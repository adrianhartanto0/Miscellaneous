import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class Command implements InterfaceCommand,InterfaceMethod {
	
	private String[] commands = {"cf","listfolders","list","rename", "sort","mkf","move","compose","delete"
			,"view","reply","mark","receive","quit"};
	private InterfaceClientModel model;
	private static final int TWOARGUMENTS = 2;
	private static final int THREEARGUMENTS = 3;
	
	Map<String, InterfaceMethod> listOfCommand = new HashMap<String, InterfaceMethod>();
	
	private String command;
	private String arg1;
	private String arg2;
	private String[] input;
	
	
	public Command(InterfaceClientModel model)
	{
		this.model = model;
	}
	
	@Override
	public void parse(String input1)
	{
		this.input  = input1.split(" ");
		this.command = this.input[0].toLowerCase();
			if(input.length == TWOARGUMENTS)
			{
				this.arg1 = input[1];
			}
			if(input.length == THREEARGUMENTS)
			{
				this.arg1 = input[1];
				this.arg2 = input[2];
			}
	}
	
	private boolean checkArg(String[] input,int number)
	{
		return (input.length == number);
	}
	
	

	@Override
	public void addMethod()
	{
		listOfCommand.put("mkf", new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return createFolder(input);
			}
		});
		listOfCommand.put("mark",new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return mark(input);
			}
		});
		listOfCommand.put("receive",new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return getNewMessage(input);
			}
		});
		listOfCommand.put("delete",new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return delete(input);
			}
		});
		listOfCommand.put("cf",new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return change(input);
			}
		});
		listOfCommand.put("sort",new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return sort(input);
			}
		});
		listOfCommand.put("rename",new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return rename(input);
			}
		});
		listOfCommand.put("move",new InterfaceMethod(){
			public boolean callMethod(String[] input)
			{
				return move(input);
			}
		});
	}
	
	
	public boolean process(String command,String[] input)
	{
		if(listOfCommand.get(command) != null )
		{
			return listOfCommand.get(command).callMethod(input);
		}
		return false;
	}
	
	
	private boolean sort(String[] arg1)
	{
		if(arg1[1].equals("-s") && checkArg(arg1, 2))
		{
			this.model.sortBySubject(true);
			return true;
		}
		else if(arg1[1].equals("-d") && checkArg(arg1,2))
		{
			this.model.sortByDate(true);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean getNewMessage(String[] input)
	{
		if(checkArg(input,1))
		{
			return this.model.checkForNewMessages();
		}
		return false;
	}
	
	private boolean createFolder(String[] input)
	{
		if(checkArg(input,2))
		{
			return this.model.createFolder(input[1]);
		}
		return false;
	} 
	
	private boolean rename(String[] input)
	{
		if(checkArg(input, 3))
		{
			//System.out.println(this.model.renameFolder(input[1], input[2]));
//			return true;
			return (!(input[1].equals("inbox") || input[1].equals("sent") ) && this.model.renameFolder(input[1], input[2]));	
		}
		return false;
	}
	
	private boolean move(String[] arg)
	{
		try
		{
			return (this.model.move(Integer.parseInt(arg[1]),arg[2]) && Integer.parseInt(arg[1]) >= 0 );
		}
		catch(NumberFormatException e )
		{
			return false;
		}
	}
	
	private boolean change(String[] input)
	{
		if(checkArg(input,2))
		{
			return (this.model.changeActiveFolder(input[1]));
		}
		return false;
	}
	
	private boolean delete(String[] input)
	{
		try
		{
			return (this.model.delete(Integer.parseInt(input[1])) && Integer.parseInt(input[1]) >= 0);
		}
		catch(NumberFormatException e )
		{
			if(!(input[2].equals("inbox") || input[2].equals("sent")) && input[1].equals("-r"))
			{
				return (this.model.deleteFolder(input[2]));
			}
			return false;
		}
	}
	
	private boolean mark(String[] input)
	{
		try
		{
			if( (arg1.equals("-r") || arg1.equals("-u")) && Integer.parseInt(input[2]) >= 0  )
			{
				return (this.model.mark(Integer.parseInt(input[2]),input[1].equals("-r")));
			}
		}
		catch(NumberFormatException e )
		{
			return false;
		}
		return false;
	}
	
	public boolean checkCommands(String command)
	{
		return Arrays.asList(this.commands).contains(command);
	}	

	public boolean checkArguments()
	{
		return (this.input.length == 1 || this.input.length == 2 || this.input.length == 3);
	}
	
	public String getCommand()
	{
		return this.command;
	}
	
	public String[] getInput()
	{
		return this.input;
	}
	
	public boolean callMethod(String[] input)
	{
		return false;
	}

	
}
