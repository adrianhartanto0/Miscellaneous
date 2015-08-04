import static org.junit.Assert.*;

import org.junit.*;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;


public class MessageTest {

	private InterfaceMessage message;
	
	@Before
	public void setUp()
	{
		InterfaceConnector connector = StandardConnector.getInstance();
		ClientModel client = new ClientModel(connector);
		boolean m = client.createFolder("inbox");
		boolean q = client.checkForNewMessages();
		boolean zxczxc = client.changeActiveFolder("inbox");
		message = client.getMessage(0);
	}
	
	@Test
	public void testGetDate()
	{
		assertNotNull(message.getDate());
	}
	
	@Test
	public void testGetFrom()
	{
		assertNotNull(message.getFrom());
	}
	
	@Test
	public void testGetSubject()
	{
		assertNotNull(message);
	}
	
	@Test
	public void testGetBody()
	{
		assertNotNull(message.getBody());
	}
	
	public void testGetRecepient()
	{
		assertNotNull(message.getRecipient());
	}
	
	

}
