import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;
import java.util.Random;
import java.util.Date;


public class FolderTest {
//	Folder folder = new Folder();
	
	InterfaceFolder folder;
	InterfaceClientModel client;
	Random rand = new Random();
	@Before
	
	public void setUp()
	{
		
		InterfaceConnector connector = StandardConnector.getInstance();
		client = new ClientModel(connector);
		boolean m = this.client.createFolder("inbox");
		boolean j = this.client.changeActiveFolder("inbox");
		boolean q = this.client.checkForNewMessages();
		Random rand = new Random();
		
		folder = new Folder();
		
		for(int index =0; index< 5 ;index++ )
		{
			InterfaceMessage message = new Message();
			message.setId(index);
			message.setSubject(randomString(10));
			long ms = -946771200000L+  (Math.abs(rand.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
			message.setDate(new Date(ms));

			folder.addMessage(message);
		}
		
		
		
		
	}

String randomString(final int length) {
	
	char[] alphabets = {'a','b','c','d','e','f','g','h','i','j','k','l','n','p','q','r','s','t','u'}; 
	 String x = "";
	    for (int i = 0; i < length; i++) 
	    {
	    	int m = rand.nextInt(alphabets.length);
	    	x = x + (alphabets[m] );
	    }
	 return x;
}
	
	
	
	@Test
	public void testAddedMessage()
	{
		assert(client.getFolder("inbox").getMessages().size() > 0);
	}

	// Test if the method returns an InterfaceMessage Object 
	// after a message is added
	
	@Test
	   public void test_getMessage() {
	     // Folder folder = new Folder() ;
	      assertNotNull(client.getFolder("inbox").getMessage(0) ) ;
	   }
	
	// Test if the method return the collection of messages
	
	@Test
	   public void test_getMessages() {
	      assertNotNull(client.getFolder("inbox").getMessages()) ;
	   }
	
	
	// Test if the method could get the name of
	// the folder assigned to it.
	
	@Test
	public void test_getFolderName() {
	      Folder folder = new Folder() ;
		  folder.setFolderName("asdfs");	
	      assertNotNull(folder.getFolderName()) ;
	   }
	
	// Test if the method could delete the message from the 
	// collection given the ID
	
	@Test
	public void testdeleteMessage()
	{
		assertTrue(client.getFolder("inbox").delete(0));
	}
	
	// Test if the message dates are sorted from the newest
	// by checking that the compareTo is less than 0. The date
	// are generated by random in the setup function above.
	
	@Test
	public void testSortByDate()
	{
		boolean notSorted = false;
		
		System.out.println(folder.asdfs());
		
		folder.sortByDate(true);
		
		System.out.println(folder.asdfs());
		
		for(int index=0; index < folder.getMessages().size();index++)
		{
			for(int index1= index+1 ; index1 < folder.getMessages().size();index1++)
			{
				if(folder.asdfs().get(index).getDate().compareTo(folder.asdfs().get(index1).getDate()) < 0)
				{
					notSorted = true;
					break;
				}
			}
		}
		
		assertFalse(notSorted);
	}

	// Test if the message subjects are sorted alphabetically
	// The subject strings are created by random using the function 
	// randomString above
 
	@Test
	public void testSortBySubject() 
	{
		
		boolean notSorted = false;
		
		folder.sortBySubject(true);
		
		for(int index=0; index < folder.getMessages().size();index++)
		{
			for(int index1= index+1 ; index1 < folder.getMessages().size();index1++)
			{
				if(folder.asdfs().get(index).getSubject().compareTo(folder.asdfs().get(index1).getSubject()) > 0)
				{
					notSorted = true;
					break;
				}
			}
		}
		
		assertFalse(notSorted);
		
	}
	
	// Return true if the folder is empty
	// else false
	
	@Test
	public void testIsEmpty()
	{  
		assertFalse(client.getFolder("inbox").isEmpty());
	}

	@Test
	public void testGetIsActive()
	{
		assertTrue(client.getFolder("inbox").getIsActive());
	}
	
	
}
