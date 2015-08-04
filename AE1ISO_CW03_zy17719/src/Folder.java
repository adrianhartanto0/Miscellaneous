import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class Folder implements InterfaceFolder {

	private int numOfMessages = 0;
	private String folderName;
	private boolean isActive = false;

	
	private List<InterfaceMessage> messages = new ArrayList<InterfaceMessage>();
	
	public Folder(){
		// do not edit the arguments passed in to this constructor.
	}
	
	/**
	 * Add a new message
	 * @param message to add to the folder
	 */
	
	@Override
	public void addMessage(InterfaceMessage message)
	{
		this.incrementMessage();
		this.messages.add(message);
	}
	
	public List<InterfaceMessage> asdfs()
	{
		return this.messages;
	}

	/**
	 * Get a message by ID
	 * @param messageId
	 * @return the message
	 */
	@Override
	public InterfaceMessage getMessage(int messageId)
	{		
		for(InterfaceMessage m:this.messages)
		{
			if(m.getId() == messageId)
			{
				return m;
			}
		}
		return null;
	}

	/**
	 * Get the collection of messages this folder holds.
	 * @return the messages.
	 */
	@Override
	public Collection<InterfaceMessage> getMessages()
	{
		return this.messages;
	}

	/**
	 * Check if the folder is empty
	 * @return true if the folder contains no messages, else false.
	 */
	
	@Override
	public boolean isEmpty()
	{
		if(getNumOfMessages() == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * Sort this folder by date
	 * @param ascending
	 */
	@Override
	public void sortByDate(boolean ascending)
	{
		if(ascending)
		{
			Collections.sort(this.messages, Collections.reverseOrder(new Comparator<InterfaceMessage>()
				{
					@Override
					public int compare(InterfaceMessage message1, InterfaceMessage message2)
					{
						return message1.getDate().compareTo(message2.getDate()); 
					}}
			));
		}
		else
		{
			Collections.sort(this.messages, new Comparator<InterfaceMessage>()
					{
						@Override
						public int compare(InterfaceMessage message1, InterfaceMessage message2)
						{
							return message1.getDate().compareTo(message2.getDate()); 
						}}
				);
		}
	}
	
	/**
	 * Sort this folder by subject.
	 * @param ascending
	 */
	@Override
	public void sortBySubject(boolean ascending)
	{
		if(ascending)
		{
			Collections.sort(this.messages, new Comparator<InterfaceMessage>()
				{
					@Override
					public int compare(InterfaceMessage message1, InterfaceMessage message2)
					{
						return message1.getSubject().compareToIgnoreCase(message2.getSubject()); 
					}}
			);
		}
		
		else
		{
			Collections.sort(this.messages, Collections.reverseOrder(new Comparator<InterfaceMessage>()
					{
						@Override
						public int compare(InterfaceMessage message1, InterfaceMessage message2)
						{
							return message1.getSubject().compareTo(message2.getSubject());
						}
					}
			));
		}
	}

	/**
	 * Delete a message with the provided ID
	 * @param messageId
	 * @return true if successful, false if the message does not exist.
	 */
	@Override
	public boolean delete(int messageId)
	{	
		for(InterfaceMessage m:this.messages)
		{
			if(m.getId() == messageId)
			{
				this.numOfMessages--;
				this.messages.remove(m);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void incrementMessage()
	{
		this.numOfMessages++;
	}
	
	private int getNumOfMessages()
	{
		return this.numOfMessages;
	}
	
	public String getFolderName()
	{
		return this.folderName.toLowerCase();
	}
	
	@Override
	public void setFolderName(String name)
	{
		this.folderName = name;
	}
	
	@Override
	public void setIsActive(boolean isActive)
	{
		this.isActive = isActive;
	}
	@Override
	public boolean getIsActive()
	{
		return this.isActive;
	}
}
