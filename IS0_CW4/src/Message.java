import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements InterfaceMessage {
	boolean read;
	int identifier;
	String subject;
	Date date;
	String recipient;
	String from;
	String body;
	String color = "noflag";
	
	
	public Message() {
		read = false;
	}

	@Override
	public Date getDate() {
		return date;
	}

	public String getRecipient() {
		return recipient;
	}


	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public int getId() {
		return identifier;
	}

	@Override
	public void markRead(boolean read) {
		this.read = read;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setColor(String color){
		this.color = color;
	}

	public void setId(int identifier) {
		this.identifier = identifier;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setBody(String body) {
		this.body = body;
	}


	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("To: " + recipient + "\r\n");
		stringBuilder.append("From: " + from + " \r\n");
		stringBuilder.append("Date: " + dateFormat.format(date) + "\r\n");
		stringBuilder.append("Subject: " + subject + " \r\n\r\n");
		stringBuilder.append(body);
		return stringBuilder.toString();
	}

	@Override
	public boolean isRead() {
		return read;
	}
	
	@Override 
	public String getColor(){
		return this.color;
	}
	

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public String getFrom() {
		return from;
	}


}
