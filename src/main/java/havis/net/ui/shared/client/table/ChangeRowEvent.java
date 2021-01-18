package havis.net.ui.shared.client.table;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChangeRowEvent extends GwtEvent<ChangeRowEvent.Handler> {

	public interface Handler extends EventHandler {
		void onChangeRow(ChangeRowEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addChangeRowHandler(ChangeRowEvent.Handler handler);
	}
	
	private static final Type<ChangeRowEvent.Handler> TYPE = new Type<>();
	
	private int index;
	private CustomWidgetRow row;
	
	public ChangeRowEvent(int index, CustomWidgetRow row) {
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
	public Type<ChangeRowEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ChangeRowEvent.Handler handler) {
		handler.onChangeRow(this);
	}

	public static Type<ChangeRowEvent.Handler> getType(){
		return TYPE;
	}
}
