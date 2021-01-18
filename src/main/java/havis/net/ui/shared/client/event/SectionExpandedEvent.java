package havis.net.ui.shared.client.event;

import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SectionExpandedEvent extends GwtEvent<SectionExpandedEvent.Handler> {

	public interface Handler extends EventHandler {
		void onSectionExpanded(SectionExpandedEvent event);
	}
	
	public interface HasHandlers {
		HandlerRegistration addSectionExpandedHandler(SectionExpandedEvent.Handler handler);
	}
	
	private static final Type<SectionExpandedEvent.Handler> TYPE = new Type<>();
	private ConfigurationSection currentSection;
	private Integer index;
	
	public SectionExpandedEvent(ConfigurationSection current, Integer index) {
		this.currentSection = current;
		this.index = index;
	}
	
	@Override
	public Type<SectionExpandedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SectionExpandedEvent.Handler handler) {
		handler.onSectionExpanded(this);
	}

	public static Type<SectionExpandedEvent.Handler> getType(){
		return TYPE;
	}

	public ConfigurationSection getCurrentSection() {
		return currentSection;
	}
	
	public Integer getIndex() {
		return index;
	}
}
