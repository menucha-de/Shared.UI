package havis.net.ui.shared.client.event;

import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChangeSectionEvent extends GwtEvent<ChangeSectionEvent.Handler> {

	public interface Handler extends EventHandler {
		void onChangeSection(ChangeSectionEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addChangeSectionHandler(ChangeSectionEvent.Handler handler);
	}
	
	private static final Type<ChangeSectionEvent.Handler> TYPE = new Type<>();
	private ConfigurationSection currentSection;
	
	public ChangeSectionEvent(ConfigurationSection currentSection) {
		this.currentSection = currentSection;
	}
	
	@Override
	public Type<ChangeSectionEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ChangeSectionEvent.Handler handler) {
		handler.onChangeSection(this);
	}

	public static Type<ChangeSectionEvent.Handler> getType(){
		return TYPE;
	}

	public ConfigurationSection getCurrentSection() {
		return currentSection;
	}
}
