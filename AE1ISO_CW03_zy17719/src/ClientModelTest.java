import static org.junit.Assert.*;

import org.junit.*;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;


public class ClientModelTest {
	
	InterfaceConnector connector = StandardConnector.getInstance();
	ClientModel client = new ClientModel(connector);
	
	boolean testCreateFolder;
	boolean changeActive;
	boolean newMessage;
	
	@Before
	public void setUp()
	{
		testCreateFolder = client.createFolder("inbox");
		boolean testFolder = client.createFolder("sent");
		newMessage = client.checkForNewMessages();
		changeActive = client.changeActiveFolder("inbox");
		boolean q = client.createFolder("test");
	}
	
	//boolean rename = client.renameFolder("inbox", "asdfs");
	
	
	// Test whether the method could make duplicate 
	// folder. It will return true if success else false.
	
	@Test
	public void testDuplicateFolder()
	{
		assertFalse(client.createFolder("sent"));
	}
	
	// Test whether the method returns True after 
	// a unique folder is created else returns False;
	
	@Test
	public void testAddFolder()
	{
		assertTrue(testCreateFolder);
	}
	
	// Test whether the method return True after
	// the folder is changed into an active folder
	
	@Test
	public void testSetActiveFolder()
	{
		System.out.println(client.getActiveFolderName());
		assertTrue(changeActive);
	}
	
	// Test whether the method return True after
	// the clientmodel successfully retrieved all message from server.
	
	@Test
	public void testGetMessges()
	{
		assertTrue(newMessage);
	}
	
	// Test whether the method will return true after 
	// the folder is renamed.

	@Test
	public void testReplaceNames()
	{
		assertTrue(client.renameFolder("test", "asdfs"));	
	}
	
	// Test whether the method could obtain a created
	// folder from the client model
	
	@Test
	public void testGetFolder()
	{
		assertNotNull(client.getFolder("inbox"));
	}
	
	// Test whether the method returns the list
	// of folder names or null 
	
	@Test
	public void testGetFolderNames()
	{
		assertNotNull(client.getFolderNames());	
	}
	
	@Test
	public void testGetMessage()
	{
		assertNotNull(client.getMessage(0));
	}

	@Test
	public void testmoveMessage()
	{
//		boolean a = client.move(0, "test");
		assertTrue(client.move(0, "sent"));
	}
	

	
	@Test 
	public void testDeleteFolder()
	{
		assertTrue(client.deleteFolder("sent"));
	}
	
	
	@Test
	public void testChange()
	{
		assertTrue(client.changeActiveFolder("sent"));
	}


}
