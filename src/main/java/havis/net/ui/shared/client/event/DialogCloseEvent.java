package havis.net.ui.shared.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class DialogCloseEvent extends GwtEvent<DialogCloseEvent.Handler> {

	public interface Handler extends EventHandler {
		void onDialogClose(DialogCloseEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addDialogCloseHandler(DialogCloseEvent.Handler handler);
	}
	
	private static final Type<DialogCloseEvent.Handler> TYPE = new Type<>();
	private boolean accept;
	
	public DialogCloseEvent(boolean accept) {
		this.accept = accept;
	}
	
	@Override
	public Type<DialogCloseEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DialogCloseEvent.Handler handler) {
		handler.onDialogClose(this);
	}

	public static Type<DialogCloseEvent.Handler> getType(){
		return TYPE;
	}

	public boolean isAccept() {
		return accept;
	}
}
