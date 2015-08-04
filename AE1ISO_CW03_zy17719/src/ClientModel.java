
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import emailConnector.InterfaceConnector;

public class ClientModel implements InterfaceClientModel {

	InterfaceConnector connector;
	
	ArrayList<Folder> folders = new ArrayList<Folder>();
	private int numOfMessages = 0; 
	public ClientModel(InterfaceConnector connector) {
		// Do not add or edit the arguments for this constructor
		this.connector = connector;
	}
	
	@Override
	public boolean changeActiveFolder(String folderName)
	{
		if(activeFolder() != null)
		{
			activeFolder().setIsActive(false);
		}	
		if(getFolder(folderName) != null && !getFolder(folderName).getIsActive())
		{
			getFolder(folderName).setIsActive(true);
			return true;
		}
		return false;
	}

	/**
	 * Check for new messages.
	 * @return true if successful, false if could not connect. Note, true even if no new messages received.
	 */
	
	@Override
	public boolean checkForNewMessages()
	{
		String listIDs;	
		try
		{
			listIDs = this.connector.listMessages(); 
		}
		catch(IOException e)
		{
			return false;
		}
		return addMessageFromServer(listIDs);
	}
	
	private boolean addMessageFromServer (String listIds)
	{
		String[] ids = listIds.split("\r\n");
		if(this.numOfMessages <= ids.length || this.numOfMessages > ids.length)
		{
			for(int index=this.numOfMessages;index < ids.length ;index++)
			{
				this.addMessage(parseMessage(this.connector.retrMessage(index),index), "inbox");
			}
			this.numOfMessages = ids.length;
		}
		else
		{
			return false;
		}
		return true;
	}

	/**
	 * Create a new, empty folder.
	 * @param folderName, the name of the folder to create.
	 * @return true is successful, false if failed, e.g the folder name already exists.
	 */
	@Override
	public boolean createFolder(String folderName)
	{
		if(!this.getFolderNames().contains(folderName))
		{
			Folder newFolder = new Folder();
			newFolder.setFolderName(folderName);
			this.folders.add(newFolder);
			return true;
		}
		return false;
	}

	/**
	 * Used to delete a message. Remember to call markForDeletion on the connector
	 * otherwise it will be received again next time calling sendrec!
	 * (The server needs to know this message is now deleted).
	 * Note - can only delete from current folder.
	 * @param messageId the message to delete.
	 * @return true if successful, false if failed (eg the message doesn't exist).
	 */
	@Override
	public boolean delete(int messageId)
	{
	
		if(activeFolder() != null && activeFolder().delete(messageId))
		{
			try
			{
				this.connector.markMessageForDeleting(messageId);
			}
			catch(IndexOutOfBoundsException e)
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the name of the current folder
	 * @return the name of the current folder.
	 */
	@Override
	public String getActiveFolderName()
	{
		for(Folder folder:this.folders)
		{
			if(folder.getIsActive())
			{
				return folder.getFolderName();
			}
		}
		return null;
	}

	/**
	 * Gets the folder
	 * @param The name of the folder to get
	 * @return The folder
	 */
	public InterfaceFolder getFolder(String folderName)
	{	
		for(Folder t: this.folders)
		{
			if(t.getFolderName().equals(folderName))
			{
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Get a collection of the folder names available
	 * @return a collection holding the folder names.
	 */
	public Collection<String> getFolderNames()
	{
		ArrayList<String> folderNames = new ArrayList<String>();
		for(Folder folder:this.folders)
		{
			folderNames.add(folder.getFolderName());
		}
		return folderNames;
	}

	/**
	 * Get a message by ID
	 * @param messageId the message ID
	 * @return the message.
	 */
	@Override
	public InterfaceMessage getMessage(int messageId)
	{
		if(this.activeFolder() != null)
		{
			return this.activeFolder().getMessage(messageId);
		}
		return null;
	}
	
	private InterfaceMessage parseMessage(String message,int messageId)
	{
		String[] messages = message.split("\r\n");	
		SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		InterfaceMessage newMessage = new Message();
		newMessage.setRecipient(messages[0].substring(messages[0].indexOf(" ")+1,messages[0].length()));
		newMessage.setFrom(messages[1].substring(messages[1].indexOf(" ")+1,messages[1].length()));
		newMessage.setSubject(messages[3].substring(messages[3].indexOf(" ")+1,messages[3].length()));
		newMessage.setBody(messages[5]);
		newMessage.setId(messageId);
		try
		{
			newMessage.setDate(date.parse(messages[2].substring(messages[2].indexOf(" ")+1,messages[2].length())));
		}
		catch(ParseException e)
		{
			System.out.println("Error: No Date Available");
		}
		return newMessage;
	}
	
	private void addMessage(InterfaceMessage message,String folderName)
	{
		if(getFolder(folderName) != null)
		{
			getFolder(folderName).addMessage(message);
		}
	}
	

	/**
	 * Retrieves a collection of messages in the current folder.
	 * @return a collection of messages in the current folder.
	 */
	
	@Override
	public Collection<InterfaceMessage> getMessages()
	{	
		if(activeFolder() != null)
		{
			return activeFolder().getMessages();
		}
		return null;
	}

	/**
	 * Mark a message as read or unread.
	 * @param messageId the messaage ID to mark
	 * @param read (true if read, false if unread)
	 * @return true if successful (false if not, e.g message does not exist).
	 */
	@Override
	public boolean mark(int messageId, boolean read)
	{
		if(activeFolder().getMessage(messageId) != null && activeFolder() != null)
		{
			activeFolder().getMessage(messageId).markRead(read);	
			return true;
		}
		return false;
	}
	
	private InterfaceFolder activeFolder()
	{
		return this.getFolder(this.getActiveFolderName());
	}

	/**
	 * Move a message from the current folder to another.
	 * @param messageId the message to move
	 * @param destination the name of the folder to move to.
	 * @return true if successful, false if not (e.g message or folder do not exist).
	 */
	@Override
	public boolean move(int messageId, String destination)
	{
		if(getFolder(destination) != null && this.activeFolder() != null && this.activeFolder().getMessage(messageId) != null)
		{
			this.getFolder(destination).addMessage(this.activeFolder().getMessage(messageId));
			this.activeFolder().delete(messageId);
			return true;
		}		
		return false;
	}

	/**
	 * Rename a folder, keeping it's current state and contents.
	 * @param oldName The old name of the folder
	 * @param newName The new name of the folder
	 * @return True if successful, false if failed (e.g folder doesn't exist, or new name already existed).
	 */
	@Override
	public boolean renameFolder(String oldName, String newName)
	{	
		if(getFolder(oldName) != null && !this.getFolderNames().contains(newName))
		{
			getFolder(oldName).setFolderName(newName);
			return true;
		}
		return false;
	}

	/**
	 * Send a message.
	 * Hint: The response from the server contains the assigned ID!
	 * @param The message to send. 
	 * @return True if successful.
	 */
	@Override
	public boolean sendMessage(InterfaceMessage msg)
	{	
		String message = msg.getRecipient() + "\r\n" + msg.getFrom() + "\r\n" + msg.getDate() + "\r\n" + msg.getSubject() + "\r\n" + msg.getBody();
		String[] listOfids = this.connector.sendMessage(message).split("\\s+");
		
		if(listOfids.length != 0)
		{
			msg.setId(Integer.parseInt(listOfids[1]));
			getFolder("sent").addMessage(msg);
			return true;
		}
		return false;
	}

	/**
	 * Sort by date
	 * @param ascending
	 */
	public void sortByDate(boolean ascending)
	{
		if(activeFolder() != null)
		{
			activeFolder().sortByDate(ascending);
		}
	}
	/**
	 * Sort by subject.
	 * @param ascending
	 */
	public void sortBySubject(boolean ascending)
	{
		if(activeFolder() != null)
		{
			activeFolder().sortBySubject(ascending);
		}
	}
	
	public boolean deleteFolder(String folderName)
	{
		for(Folder t: this.folders)
		{
			if(t.getFolderName().equals(folderName))
			{
				//System.out.println(this.folders.remove(t));
				return this.folders.remove(t);
			}
		}
		return false;
	}
	
}
