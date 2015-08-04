import static org.junit.Assert.*;

import org.junit.*;


import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;


public class CommandReplyTest {

	private InterfaceConnector connector;
	//private View view;
	private Controller controller;
	CommandReply reply;
	String input1 = "reply";
	String[] input = input1.split(" ");
	

	@Before
	public void setUp()
	{
		connector = StandardConnector.getInstance();
		controller = new Controller();
		InterfaceClientModel model = new ClientModel(connector);
		View view = new View(controller);
		
		try{
			reply = new CommandReply(model,view,input);
		}
		catch(BadCommandException bce)
		{
			bce.getMessage();
		}
	}
	
	@Test
	public void testValidArgument()
	{
		assertTrue(reply.validateArguments());
	}
	
//	
//	@Test 
//	void TestfailReply()
//	{
//		reply.execute();
//	}
	
	
	
	

}
