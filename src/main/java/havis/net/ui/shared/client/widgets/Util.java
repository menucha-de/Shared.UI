package havis.net.ui.shared.client.widgets;

import java.io.IOException;
import java.text.ParseException;

import org.fusesource.restygwt.client.FailedResponseException;

import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Response;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;

public abstract class Util {

	public static Parser<Short> getShortParser() {
		return new Parser<Short>() {

			@Override
			public Short parse(CharSequence text) throws ParseException {
				if (text == null || text.toString().isEmpty())
					return -1;
				try {
					return Short.parseShort(text.toString().trim());
				} catch (NumberFormatException e) {
					return -1;
				}
			}
		};
	}

	public static Renderer<Short> getShortRenderer() {
		return new Renderer<Short>() {

			@Override
			public String render(Short object) {
				if (object == null || object < 0)
					return null;
				else
					return object.toString();
			}

			@Override
			public void render(Short object, Appendable appendable) throws IOException {
				if (object == null)
					appendable.append("");
				else
					appendable.append(object.toString());
			}
		};
	}

	public static Parser<Integer> getIntegerParser() {
		return new Parser<Integer>() {

			@Override
			public Integer parse(CharSequence text) throws ParseException {
				if (text == null || text.toString().isEmpty())
					return -1;
				try {
					return Integer.parseInt(text.toString().trim());
				} catch (NumberFormatException e) {
					return -1;
				}
			}
		};
	}

	public static Renderer<Integer> getIntegerRenderer() {
		return new Renderer<Integer>() {

			@Override
			public String render(Integer object) {
				if (object == null || object < 0)
					return null;
				else
					return object.toString();
			}

			@Override
			public void render(Integer object, Appendable appendable) throws IOException {
				if (object == null)
					appendable.append("");
				else
					appendable.append(object.toString());
			}
		};
	}

	/**
	 * Returns true if the given string is null or is an empty string.
	 * 
	 * @param str
	 *            a string reference to check
	 * @return true if the string is null or is an empty string
	 */
	public static boolean isNullOrEmpty(CharSequence str) {
		if (str == null || str.toString().trim().isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * @return tab inner height
	 */
	public static final native int getWindowParentInnerHeight()/*-{
		return window.parent.parent.innerHeight;
	}-*/;

	/**
	 * @return offsetTop of iframe which serves as container for the webui
	 */
	public static final native int getContentOffsetTop()/*-{
		if (window == null || window.parent == null
				|| window.parent.parent == null
				|| window.parent.parent.document == null) {
			return 0;
		}
		if (window.parent.parent.document.getElementById("content") != null) {
			return window.parent.parent.document.getElementById("content").offsetTop;
		} else {
			return 0;
		}
	}-*/;

	/**
	 * @return scrollTop position of body element.
	 */
	public static final native int getContentScrollTop()/*-{
		if (window == null || window.parent == null
				|| window.parent.parent == null
				|| window.parent.parent.document == null) {
			return 0;
		}
		if (window.parent.parent.document.body != null) {
			// Implementation for Chrome. IE and Firefox will always return 0
			var result = window.parent.parent.document.body.scrollTop;
			if (result == 0) {
				// Implementation for IE and Firefox. Chrome will always return 0
				if (window.parent.parent.document.documentElement != null) {
					result = window.parent.parent.document.documentElement.scrollTop;
				}
			}
			return result;
		} else {
			return 0;
		}
	}-*/;

	/**
	 * Returns the message of the {@code throwable}
	 * 
	 * @param throwable
	 *            The throwable
	 * @return the message
	 */
	public static String getThrowableMessage(Throwable throwable) {
		if (throwable != null) {
			String returnMessage = throwable.getMessage();

			if (FailedResponseException.class.equals(throwable.getClass())) {
				Response response = ((FailedResponseException) throwable).getResponse();
				if (response != null) {
					if (!isNullOrEmpty(response.getText())) {
						returnMessage = response.getText();
						int offset = returnMessage.indexOf("Exception: ");
						if (offset >= 0) {
							returnMessage = returnMessage.substring(offset + "Exception: ".length());
						}
					}
				}
			}

			return returnMessage;
		}
		return null;
	}

	/**
	 * Performs a click event on the given element
	 * 
	 * @param elem
	 */
	public final static native void clickElement(Element elem) /*-{
		elem.click();
	}-*/;

}
