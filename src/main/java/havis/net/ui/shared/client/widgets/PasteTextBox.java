package havis.net.ui.shared.client.widgets;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

import havis.net.ui.shared.client.event.PasteEvent;


public class PasteTextBox extends TextBox implements PasteEvent.HasHandlers {

	public PasteTextBox() {
		super();
		sinkEvents(Event.ONPASTE);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONPASTE:
			event.stopPropagation();
			event.preventDefault();
			elemental.events.Event ev = (elemental.events.Event) event;
			String pastedText = ev.getClipboardData().getData("text/plain");
			fireEvent(new PasteEvent(pastedText));
			break;
		}
	}

	@Override
	public HandlerRegistration addPasteEventHandler(PasteEvent.Handler handler) {
		return addHandler(handler, PasteEvent.getType());
	}
}
