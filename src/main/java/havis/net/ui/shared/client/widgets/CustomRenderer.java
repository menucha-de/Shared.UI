package havis.net.ui.shared.client.widgets;

public interface CustomRenderer<T> {
	/**
	 * Renders object as plain text. Should never throw any exceptions!
	 * 
	 * @param value
	 *            the value
	 * @return the string representation
	 */
	String render(T value);
}
