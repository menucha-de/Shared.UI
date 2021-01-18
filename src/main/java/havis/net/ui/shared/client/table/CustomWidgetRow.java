package havis.net.ui.shared.client.table;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class CustomWidgetRow extends Widget implements Iterable<Widget>, HasClickHandlers, HasEnabled {
	
	ArrayList<Widget> widgets = new ArrayList<>();
	private boolean enabled = true;
	
	protected void addColumn(Widget w) {
		SimplePanel panel = new SimplePanel();
		panel.add(w);
		widgets.add(panel);
	}
	
	@Override
	public Iterator<Widget> iterator() {
		return widgets.iterator();
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		if (!widgets.isEmpty()) {
			SimplePanel panel = (SimplePanel) widgets.get(0);
			Widget w = panel.getWidget();
			if (w instanceof HasClickHandlers) {
				return ((HasClickHandlers) w).addClickHandler(handler);
			}
		}
		return null;
	}
	
	private Widget getColumnWidget(int index) {
		if (index >= 0 && index < widgets.size()) {
			SimplePanel p = (SimplePanel) widgets.get(index);
			return p.getWidget();
		}
		return null;
	}
	
	protected int getWidgetCount() {
		return widgets.size();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		for (int i = 0; i < widgets.size(); ++i) {
			Widget w = getColumnWidget(i);
			if (w instanceof HasEnabled) {
				((HasEnabled) w).setEnabled(enabled);
			}
		}
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
