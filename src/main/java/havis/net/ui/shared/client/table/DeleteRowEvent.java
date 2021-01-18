package havis.net.ui.shared.client.table;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class DeleteRowEvent extends GwtEvent<DeleteRowEvent.Handler> {

	public interface Handler extends EventHandler {
		void onDeleteRow(DeleteRowEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addDeleteRowHandler(DeleteRowEvent.Handler handler);
	}
	
	private static final Type<DeleteRowEvent.Handler> TYPE = new Type<>();
	
	private int index;
	private CustomWidgetRow row;
	
	public DeleteRowEvent(int index, CustomWidgetRow row) {
		this.index = index;
		this.row = row;
	}
	
	public int getIndex() {
		return index;
	}
	
	public CustomWidgetRow getRow() {
		return row;
	}
	
	@Override
	public Type<DeleteRowEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DeleteRowEvent.Handler handler) {
		handler.onDeleteRow(this);
	}

	public static Type<DeleteRowEvent.Handler> getType(){
		return TYPE;
	}
}
