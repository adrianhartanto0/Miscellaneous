import static org.junit.Assert.*;

import org.junit.*;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;


public class ClientModelTest {
	
	InterfaceConnector connector;
	InterfaceClientModel model;
	CommandFactory factory;
	String foldername;
	String section;
	
	@Before
	public void setup()
	{
		factory = CommandFactory.getInstance();
		connector = StandardConnector.getInstance();
		model = new ClientModel(connector);
		foldername = "asd";
	}
	
	/*
	 * this will test whether an appropriate error message
	 * is printed when a invalid/non-existent folder is passed 
	 * when adding an incoming message rule.
	 */
	
	@Test
	public void testaddRuleFail(){
		assertEquals("Error: Folder does not exist",model.addRule(foldername, section, "nothing"));
	}
	
	/*
	 * this will test whether an appropriate error message
	 * is printed when an invalid part of a message is passed 
	 * when adding an incoming message rule.
	 */
	
	@Test 
	public void testaddRuleFail1(){
		model.createFolder("asd");
		section = "recipeient";
		assertEquals("Error: Invalid part of message",model.addRule(foldername, section, "nothing"));
	}
	
	/*
	 * the method below will test whether an appropriate  message
	 * is printed when a valid incoming rule is added
	 */
	
	
	@Test
	public void testAddRuleSuccess(){
		model.createFolder("asd");
		section = "sender";
		assertEquals("Success: Added rule and moved 0 messages",model.addRule(foldername, section, "spammer"));
	}
	
	/*
	 * the method below will test whether the move method is possible to 
	 * move message to smartfolders
	 */
	
	@Test
	public void testMoveToSmartFolder(){
		//model.changeActiveFolder("red")
//		model.checkForNewMessages();
//		model.flagMessage(0, "red");
		assertFalse(model.move(0,"red"));
	}
	
	@Test
	public void testCheckForNewMessages(){
		boolean success = model.checkForNewMessages();
		boolean qwerty = false;
		if(!success && model.getFolder("inbox").getMessages().size() > 0 ){
			qwerty = true;
		}
//		for(InterfaceMessage message:model.getFolder("inbox").getMessages()){
//			System.out.println(message.getRecipient());
//		}
		assertFalse(qwerty);
	}

	
	@Test
	public void testprintFailUndo(){
		assertEquals("Error: no command to undo", model.undo());
	}
	@Test
	public void testprintFailRedo(){
		assertEquals("Error: no command to undo", model.undo());
	}
	@Test
	public void testMove(){
		model.checkForNewMessages();
		model.createFolder("asd");
		boolean masdfs = model.move(0, "asd");
		boolean success = true;
		
		if(!masdfs){
			success = false;
		}
		for(InterfaceMessage message:model.getFolder("asd").getMessages()){
			if(message.getId() != 0 ){
				success = false;
			}
		}
		assertTrue(success);
	}
	
	@Test
	public void testFlagMessage(){
		model.checkForNewMessages();
		int messageId = 0;
		String[] color = {"red","green","blue"};
		boolean notFlagged = true;
		for(int index =0; index< color.length;index++){
			model.flagMessage(messageId, color[index]);
			if(!model.getMessage(messageId).getColor().equals(color[index])){
				notFlagged = false;
			} 
		assertTrue(notFlagged);
	}}
	
//	@Test
//	public void testDeleteFromSmartFolder(){
//		InterfaceClientModel model1 = new ClientModel(connector); 
//		model1.checkForNewMessages();
//		model1.flagMessage(0, "red");
//		model1.flagMessage(1,"red");
//		model1.changeActiveFolder("red");
//		model1.delete(0);
////		System.out.println(model1.getMessages().size());
//
//		boolean failed = false;
//		if(model.getMessage(0) != null){
//			failed = true;
//		}
////		
//		for(String name:model.getFolderNames()){
//			if(model1.getFolder(name).getMessage(0) != null){
//				failed = true;
//			}
//		}
//		assertFalse(failed);
//	}
	
	@Test
	public void testRenameFolder(){
		model.createFolder("asd");
		boolean success = true;
		String newName = "qwerty";
		if(!model.renameFolder("asd",newName)){
			success = false;
		}
		if(!model.getFolderNames().contains(newName)){
			success = false;
		}
		assertTrue(success);
	}
	
	
	
	@Test
	public void testIfInSmartFolder(){
		InterfaceClientModel model2 = new ClientModel(connector); 
		model2.checkForNewMessages();
		model2.flagMessage(0, "red");
		model2.changeActiveFolder("red");
		boolean how = true;
		
		//System.out.println(model.getMessages().size());
		if(model2.getMessages().size() != 1){
			System.out.println("ASDASD");
			how = false;
		}
		for(InterfaceMessage message:model2.getMessages()){
			if(!message.getColor().equals("red")){
				how = false;
			}
		}
		assertTrue(how);
	}
	@Test
	public void testIfInSmartFolder2(){
		model.checkForNewMessages();
		model.flagMessage(0, "red");
		model.changeActiveFolder("red");
		model.flagMessage(0, "green");
		boolean how = true;
		
		if(model.getMessages().size() != 0){
			how = false;
		}
		model.changeActiveFolder("green");
		if(model.getMessages().size() != 1){
			how = false; 
		}
		
		for(InterfaceMessage message:model.getMessages()){
			if(!message.getColor().equals("green")){
				how = false;
			}
		}
		assertTrue(how);
	}
	
	@Test
	public void testAddRuleSuccess1(){
		model.createFolder("asd");
		section = "sender";
		model.addRule(foldername, section, "spammer");
		model.checkForNewMessages();
		boolean qwerty = false;
		for(InterfaceMessage message :model.getFolder("asd").getMessages()){
			//System.out.println(message.getFrom());
			if(message.getFrom().indexOf(" spammer") == -1){
				qwerty = true;
			}
		}
		assertFalse(qwerty);
	}
//	
//	@Test
//	public void testAddRuleSuccess2(){
//		model.createFolder("asd");
//		section = "sender";
//		model.addRule(foldername, section, "spammer");
//		model.checkForNewMessages();
//		boolean qwerty = false;
//		for(InterfaceMessage message :model.getFolder("asd").getMessages()){
//			//System.out.println(message.getFrom());
//			if(message.getFrom().indexOf(" spammer") == -1){
//				qwerty = true;
//			}
//		}
//		assertFalse(qwerty);
//	}
	
	
	@Test
	public void testAddRuleSuccess3(){
		model.createFolder("asd");
		section = "recipient";
		model.addRule(foldername, section, "cs.nott.ac.uk");
		model.checkForNewMessages();
		boolean qwerty = false;

//		System.out.println("Size1 is " +  model.getFolder("asd").getMessages().size());
		if(model.getFolder("asd").getMessages().size() != 8 || model.getFolder("asd").getMessages().size() <= 0){
			qwerty = true;
		}
		
//		System.out.println("Size1 is " +  model.getFolder("inbox").getMessages().size());
//		
//		for(int index = 0; index < model.getFolder("inbox").getMessages().size();index++){
//			System.out.println("Recipient is " + model.getFolder("inbox").getMessage(index).getSubject());
//			System.out.println("Recipient is " + model.getFolder("inbox").getMessage(index).getRecipient());
//		}
		
		for(InterfaceMessage message :model.getFolder("asd").getMessages()){
			//System.out.println(message.getRecipient());
			if(message.getRecipient().indexOf("cs.nott.ac.uk") == -1){
				qwerty = true;
			}
		}
		assertFalse(qwerty);
	}
	

	

	
	
	
	
	
	
	

}
