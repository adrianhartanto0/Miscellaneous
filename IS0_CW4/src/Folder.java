import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Folder implements InterfaceFolder {

	ArrayList<InterfaceMessage> messages;

	public Folder() {
		messages = new ArrayList<InterfaceMessage>();
	}

	public void addMessage(InterfaceMessage message) {
		messages.add(message);
	}

	public InterfaceMessage getMessage(int messageId) {
		for (InterfaceMessage message : messages) {
			if (message.getId() == messageId) {
				return message;
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}

	@Override
	public Collection<InterfaceMessage> getMessages() {
		return messages;
	}

	@Override
	public void sortByDate(boolean ascending) {
		Collections.sort(messages, new DateComparator());
		if (!ascending) {
			Collections.reverse(messages);
		}
	}

	@Override
	public void sortBySubject(boolean ascending) {
		Collections.sort(messages, new SubjectComparator());
		if (!ascending) {
			Collections.reverse(messages);
		}
	}

	public class DateComparator implements Comparator<InterfaceMessage> {
		@Override
		public int compare(InterfaceMessage first, InterfaceMessage second) {
			return first.getDate().compareTo(second.getDate());
		}
	}

	public class SubjectComparator implements Comparator<InterfaceMessage> {
		@Override
		public int compare(InterfaceMessage first, InterfaceMessage second) {
			return first.getSubject().compareTo(second.getSubject());
		}
	}

	@Override
	public boolean delete(int messageId) {
		return messages.remove(getMessage(messageId));
	}
}
