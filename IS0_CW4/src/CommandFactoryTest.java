import static org.junit.Assert.*;

import org.junit.*;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;

public class CommandFactoryTest {

	
	CommandFactory factory;
	InterfaceClientModel model;
	View view;
	Controller controller;
	InterfaceConnector connector;
	@Before
	public void setup(){
		connector = StandardConnector.getInstance();
		factory = CommandFactory.getInstance();
		controller = new Controller();
		view = new View(controller);
		model = new ClientModel(connector);
		factory.setReferences(model, view);
	}
	
	
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
	
/*
 * tHis method will test whether all the methods are in the hashmap
 * 	after the addMethod method is called. This test first check whether 
 *  the size of the hashmap is the same as the number of the commands. Then
 *  it checks whether all the keys are the available commands. 
 */

	@Test
	public void testAddMethod(){
		
		factory.addMethod();
		boolean success = true;
		if(CommandFactory.listOfCommand.size() != 19){
			success = false;
		}
		String[] commands = {"cf","listfolders","list","rename","sort","mkf", "move","compose","delete","view","receive","mark","quit","reply","flag","cr","undo","redo"};
		for(int index=0; index <commands.length;index++){
			if(!CommandFactory.listOfCommand.containsKey(commands[index]) ){
				success = false;
			}
		}
		assertTrue(success);
	}
	
/*
 * this method will test whether an invalid alias can be 
 * added. This method will first check if the addAlias method
 * 	is successful and then check whether an alias is added. 
 */
	
	
	@Test
	public void testAddAliasFail(){
		factory.addMethod();
		boolean success = true;
		boolean qwerty = CommandFactory.addAlias("qwerty", "asdfs");
		
		if(!qwerty || !CommandFactory.listOfAlias.containsKey("asdfs")){
			success = false;
		}
		assertFalse(success);
	}

/*
 * This method will test if a valid alias could be added. this  
 * method will first check if the addAlias method is successful(return true)
 * Then, it will check whether the alias is a key of the ListOfAlias hashmap.
 * Lastly it will check whether the alias maps to a valid command 
*/

	
	@Test
	public void testAddAliasSuccess(){
		factory.addMethod();
		boolean success = true;
		boolean qwerty = CommandFactory.addAlias("move", "mov");
		String asdfs = CommandFactory.listOfAlias.get("mov");
		
		if(!qwerty || !CommandFactory.listOfAlias.containsKey("mov") || !asdfs.equals("move")){
			success = false;
		}
		assertTrue(success);
	}
	
/*
 * This method will test whether alias to alias can be added.
 * It first check whether the addAlias method returns successful 
 * Then it will check whether the alias and alias to alias is 
 * present as a key in the listOfAlias hashmap.
 * 	
 */
	
	
	@Test
	public void testAddAlias2AliasSuccess(){
		factory.addMethod();
		boolean success = true;
		boolean qwerty = CommandFactory.addAlias("move", "mov");
		boolean qqwert1 = CommandFactory.addAlias("mov","m");
		String asdzxc = CommandFactory.listOfAlias.get("m");
		
		if(!qwerty && !qqwert1){
			success = false;
		}
		if(!CommandFactory.listOfAlias.containsKey("mov") && !CommandFactory.listOfAlias.containsKey("m") && !asdzxc.equals("mov")){
			success = false;
		}
		
		assertTrue(success);
	}
	
/*
 * This method checks whether an invalid alias could be 
 * removed. the removeAlias method will return false if the remove 
 * is not successful
 */
	
	@Test
	public void testRemoveAliasFail(){
		factory.addMethod();
	//	boolean success = true;
		//boolean qwerty = CommandFactory.addAlias("move", "mov");
		boolean asdfs = CommandFactory.removeAlias("asdq1");
		assertFalse(asdfs);
	}
	
/*
 * This method will check whether an added valid alias can be 
 * removed from the listOfAlias method. This test will check 
 * first that the valid alias is added and then check whether the 
 * removing of alias is successful(removeAlias returns true)
 */
	
	@Test
	public void testRemoveAliasSuccess(){
		factory.addMethod();
		boolean success = true;
		boolean qwerty = CommandFactory.addAlias("move", "mov");
		boolean asdfs = CommandFactory.removeAlias("mov");
		if(!qwerty || CommandFactory.listOfAlias.containsKey("mov") || !asdfs ){
			success = false;
		}
		assertTrue(success);
	}
	
/*
 * This method will check whether it's possible to 
 * utilise the alias as a replacement for the original method.
 * this method will first see whether the getMethod returns a non-null
 * 	object and then it will check that it's the desired method.
 */

	
	@Test
	public void testGetMethod(){
		factory.addMethod();
	//	boolean success = true;
		boolean qwerty = CommandFactory.addAlias("move", "mov");
		InterfaceMethod method1 = CommandFactory.getMethod("mov");
		AbstractCommand command;
		String response = "";
		String error ="Error: Check message 0 exists and that test is a folder, and that neither the " + "\n" + "currect folder or target folder are smart folder";
		boolean asdfs = true;
		
		String[] move = {"mov","0","test"};
		try{
			command = method1.callMethod(move);
			response = command.execute();
		}
		catch(BadCommandException bce){
			response = bce.getMessage();
		}
		
		if(method1 == null || !response.equals(error) ){
			asdfs = false;
		}
		assertTrue(asdfs);
	}
	
	@Test
	public void testGetMethodFail(){
		factory.addMethod();
	//	boolean success = true;
		boolean qwerty = CommandFactory.addAlias("move", "mov");
		InterfaceMethod method1 = CommandFactory.getMethod("qwerty");
		AbstractCommand command;
		String response = "";
		String error ="Error: Not a valid command: ";
		boolean asdfs = true;
		
		String[] move = {"move","0","test"};
		
		if(method1 == null || !response.equals(error) ){
			asdfs = false;
		}else{
			try{
				command = method1.callMethod(move);
				response = command.execute();
			}
			catch(BadCommandException bce){
				response = bce.getMessage();
			}
		}

		assertFalse(asdfs);
	}
	
/* This method will check whether or not it is possible 
 * 	to interact with the mail client by entering an invalid command.
 *  this test checks whether appropriate error message is emitted to 
 *  the user.
 */
	
	@Test
	public void testBuildNoCommand(){
		factory.addMethod();
		String command = "qwerty 0 asdas";
		AbstractCommand command1;
		String response ="";
		try{
			command1 = CommandFactory.buildCommand(command);
			response = command1.execute();
		}
		catch(BadCommandException bce){
			response = bce.getMessage();
		}
		assertEquals("Error: Not a valid command: qwerty",response);
	}
		
	

}
