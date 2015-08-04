import java.util.*;


public class MyStack {
	Stack<String> stack = new Stack<String>();
	
	public void addToStack(String input){
		stack.push(input);
	}
	
	public String returnItem(){
		return stack.pop();
	}
	
	public Stack<String> returnStack(){
		return this.stack;
	}
	
}
