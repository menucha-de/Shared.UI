package havis.net.ui.shared.client.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import havis.net.ui.shared.client.event.MessageEvent.MessageType;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class CustomMessageWidget extends PopupPanel {

	private Image image;

	private FocusPanel position;

	private FlowPanel container;

	private Label label;

	private SimplePanel errorDot;

	ResourceBundle res = ResourceBundle.INSTANCE;

	public CustomMessageWidget() {
		super(true);
		position = new FocusPanel();
		position.setStylePrimaryName(res.css().webuiMessagePopupPanel());

		container = new FlowPanel();

		errorDot = new SimplePanel();
		errorDot.setStyleName(res.css().webuiMessagePopupPanelErrorDot());

		image = new Image(res.errorIcon());

		label = new Label();

		container.add(errorDot);
		container.add(image);
		container.add(label);

		position.add(container);

		setWidget(position);

		Document.get().getBody().appendChild(this.getElement());
	}

	/**
	 * Shows this pop-up with the message of the specified throwable. The
	 * message type will be ERROR
	 * 
	 * @param throwable
	 *            The throwable which shall be displayed
	 */
//	public void showMessage(Throwable throwable) {
//		showMessage(throwable, MessageType.ERROR);
//	}

	/**
	 * Shows this pop-up with the message of the specified throwable.
	 * 
	 * @param throwable
	 *            The throwable which shall be displayed
	 * @param type
	 *            The type of the message
	 */
//	public void showMessage(Throwable throwable, MessageType type) {
//		showMessage(Util.getThrowableMessage(throwable), type);
//	}

	/**
	 * Shows this pop-up with the specified message. The message type will be
	 * NONE
	 * 
	 * @param message
	 *            The message which shall be displayed
	 */
	public void showMessage(String message) {
		showMessage(message, null);
	}

	/**
	 * Shows this pop-up with the specified message.
	 * 
	 * @param message
	 *            The message which shall be displayed
	 * @param type
	 *            The type of the message
	 */
	public void showMessage(String message, MessageType type) {
		if (MessageType.CLEAR.equals(type)) {
			hide();
		} else {
			label.setText(message);
			label.setTitle(message);

			if (type == null) {
				type = MessageType.INFO;
			}

			switch (type) {
			case INFO:
				image.setResource(res.infoIcon());
				break;
			case WARNING:
				image.setResource(res.warningIcon());
				break;
			case ERROR:
				image.setResource(res.errorIcon());
				break;
			case NONE:
			default:
				break;
			}

			final int scrollTop = Util.getContentScrollTop();
			final int innerHeight = Util.getWindowParentInnerHeight();
			final int offsetTop = Util.getContentOffsetTop();

			position.getElement().getStyle().setTop(scrollTop + innerHeight + offsetTop, Unit.PX);

			show();

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {

				@Override
				public void execute() {
					position.getElement().getStyle().setTop(scrollTop + innerHeight - 150 - offsetTop, Unit.PX);
				}
			});
		}
	}

	/**
	 * Shows a pop-up with the message of the specified throwable. The message
	 * type will be ERROR
	 * 
	 * @param throwable
	 *            The throwable which shall be displayed
	 * @return The displayed widget
	 */
//	public static CustomMessageWidget show(Throwable throwable) {
//		CustomMessageWidget widget = new CustomMessageWidget();
//		widget.showMessage(throwable);
//		return widget;
//	}

	/**
	 * Shows a pop-up with the message of the specified throwable.
	 * 
	 * @param throwable
	 *            The throwable which shall be displayed
	 * @param type
	 *            The type of the message
	 * @return The displayed widget
	 */
//	public static CustomMessageWidget show(Throwable throwable, MessageType type) {
//		CustomMessageWidget widget = new CustomMessageWidget();
//		widget.showMessage(throwable, type);
//		return widget;
//	}

	/**
	 * Shows a pop-up with the specified message. The message type will be NONE
	 * 
	 * @param message
	 *            The message which shall be displayed
	 * @return The displayed widget
	 */
	public static CustomMessageWidget show(String message) {
		CustomMessageWidget widget = new CustomMessageWidget();
		widget.showMessage(message);
		return widget;
	}

	/**
	 * Shows a pop-up with the specified message.
	 * 
	 * @param message
	 *            The message which shall be displayed
	 * @param type
	 *            The type of the message
	 * @return The displayed widget
	 */
	public static CustomMessageWidget show(String message, MessageType type) {
		CustomMessageWidget widget = new CustomMessageWidget();
		widget.showMessage(message, type);
		return widget;
	}

	/**
	 * @return tab inner height
	 * @deprecated Since version 1.8. Use
	 *             havis.net.ui.shared.client.widgets.Util
	 *             .getWindowParentInnerHeight () instead.
	 */
	@Deprecated
	public static final native int getWindowParentInnerHeight()/*-{
		return window.parent.parent.innerHeight;
	}-*/;

	/**
	 * @return offsetTop of iframe which serves as container for the webui
	 * @deprecated Since version 1.8. Use
	 *             havis.net.ui.shared.client.widgets.Util.getContentOffsetTop()
	 *             instead.
	 */
	@Deprecated
	public static final native int getContentOffsetTop()/*-{
		if (window.parent.parent.document.getElementById("content") != null) {
			return window.parent.parent.document.getElementById("content").offsetTop;
		} else {
			return 0;
		}
	}-*/;

	/**
	 * @return scrollTop position of body element.
	 * @deprecated Since version 1.8. Use
	 *             havis.net.ui.shared.client.widgets.Util.getContentScrollTop()
	 *             instead.
	 */
	@Deprecated
	public static final native int getContentScrollTop()/*-{
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
}