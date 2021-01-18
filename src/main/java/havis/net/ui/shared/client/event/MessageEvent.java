package havis.net.ui.shared.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class MessageEvent extends GwtEvent<MessageEvent.Handler> {

	public enum MessageType {
		CLEAR, NONE, INFO, WARNING, ERROR;
	}

	private String message;
	private MessageType messageType;

	public interface Handler extends EventHandler {
		void onMessage(MessageEvent event);

		void onClear(MessageEvent event);
	}

	public interface HasHandlers {
		HandlerRegistration addMessageEventHandler(MessageEvent.Handler handler);
	}

	private static final Type<MessageEvent.Handler> TYPE = new Type<>();

	public MessageEvent(MessageType messageType, String message) {
		this.message = message;
		this.messageType = messageType;
	}

	public String getMessage() {
		return message;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public static Type<Handler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		switch (messageType) {
		case NONE:
		case INFO:
		case WARNING:
		case ERROR:
			handler.onMessage(this);
			break;
		case CLEAR:
			handler.onClear(this);
		default:
			break;
		}
	}
}
