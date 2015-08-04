import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;


public class CommandTest {

	InterfaceCommand command;
	String input ="";
	
	@Before
	public void setUp()
	{
		InterfaceConnector connector = StandardConnector.getInstance();
		InterfaceClientModel client = new ClientModel(connector);
		command = new Command(client);
		input ="rename inbox test";
	}
	
	@Test
	public void testGetCommand()
	{
		command.parse(input);
		assertNotNull(command.getCommand());
	}
	
	@Test
	public void testGetInput()
	{
		command.parse(input);
		assertNotNull(command.getInput());
	}
	@Test
	public void  testCheckArguments()
	{
		command.parse(input);
		assertTrue(command.checkArguments());
	}
	
	@Test
	public void testCheckCommand()
	{
		command.parse(input);
		assertTrue(command.checkCommands(command.getInput()[0]));
	}	
	
}
