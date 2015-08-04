import static org.junit.Assert.*;

import org.junit.*;
import java.util.Random;

public class MyStackTest {

//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
	
	Random rand;
	MyStack stack;
	@Before
	public void setUp(){
		rand = new Random();
		stack = new MyStack();
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
	public void testreturnStack(){
		assertNotNull(stack.returnStack());
	}
	
	@Test
	public void testAddToStack(){
		boolean success = true;
		for(int index = 0; index < 4; index++){
			stack.addToStack(randomString(5));
		}
		if(stack.returnStack().size() != 4){
			success = false;
		}
		assertTrue(success);
	}
	@Test
	public void testPopStack(){
		
		boolean success = true;
		for(int index = 0; index < 4; index++){
			stack.addToStack(randomString(5));
		}
		
		int size = stack.returnStack().size();
		String asdfs = stack.returnItem();
		
		if(asdfs == null && stack.returnStack().size() != size -1 ){
			success = false;
		}
		assertTrue(success);
	}
	
	

}
