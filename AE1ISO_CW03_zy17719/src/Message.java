import java.util.Date;

public class Message implements InterfaceMessage {
	
	private String body;
	private Date date;
	private String sender;
	private String receiver;
	private String subject;
	private int messageiD;
	private boolean read = false;

	public Message() {
		// do not edit the arugments passed into this constructor.
	}
	/**
	 * Get the body of the message
	 * @return the body as a string
	 */
	@Override
	public String getBody()
	{
		return this.body;
	}

	/**
	 * Get the message date.
	 * @return the date
	 */
	@Override
	public Date getDate()
	{
		return this.date;
	}

	/**
	 * Get the senders address.
	 * @return the senders address
	 */
	@Override
	public String getFrom()
	{
		return this.sender;
	}

	/**
	 * Get the ID assigned by the connector for this message
	 * @return the ID
	 */
	@Override
	public int getId()
	{
		return this.messageiD;
	}

	/**
	 * Get the recipient
	 * @return the recipient
	 */
	@Override
	public String getRecipient()
	{
		return this.receiver;
	}

	/**
	 * Get the subject
	 * @return the subject
	 */
	@Override
	public String getSubject()
	{
		return this.subject;
	}

	/**
	 * Check if this message has been read.
	 * @return true if it has been read, false if not
	 */
	@Override
	public boolean isRead()
	{
		return read;
	}
	
	/**
	 * Mark this message as read or unread
	 * @param read true if it has been read, false if not
	 */
	public void markRead(boolean read)
	{
		this.read = read;
	}
	
	/**
	 * Set the body of this message.
	 * @param body
	 */
	public void setBody(String body)
	{
		this.body = body;
	}
	
	/**
	 * Set the date of this message.
	 * @param date
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	/**
	 * Set the senders address
	 * @param from
	 */
	public void setFrom(String from)
	{
		this.sender = from;
	}
	
	/**
	 * Set the ID of this message (Note, this is assigned by the connector!)
	 * @param providedId
	 */
	public void setId(int providedId)
	{
		this.messageiD = providedId;
	}
	
	/**
	 * Set the recipient
	 * @param recipient
	 */
	public void setRecipient(String recipient)
	{
		this.receiver = recipient;
	}
	
	/**
	 * Set the subject.
	 * @param subject
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
}
