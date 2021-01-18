package havis.net.ui.shared.client.table;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class CreateRowEvent extends GwtEvent<CreateRowEvent.Handler> {

	public interface Handler extends EventHandler {
		void onCreateRow(CreateRowEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addCreateRowHandler(CreateRowEvent.Handler handler);
	}
	
	private static final Type<CreateRowEvent.Handler> TYPE = new Type<>();
	
	@Override
	public Type<CreateRowEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CreateRowEvent.Handler handler) {
		handler.onCreateRow(this);
	}

	public static Type<CreateRowEvent.Handler> getType(){
		return TYPE;
	}
}
