
public interface InterfaceCommand {

	

//	
public boolean checkCommands(String command);

	public String getCommand();
	public String[] getInput();
	public boolean checkArguments();
	public boolean process(String command,String[] input);
	public void addMethod();
	public void parse(String input1);
	
}
