package havis.net.ui.shared.client;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;

public class SectionToggle extends Label implements HasValue<Boolean>, HasValueChangeHandlers<Boolean>, HasEnabled {

	private boolean expanded = false;
	private boolean enabled = true;

	private ResourceBundle res = ResourceBundle.INSTANCE;

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
		setStyleName(res.css().open(), expanded);
	}

	public SectionToggle() {
		setStyleName(res.css().sectionToggle());
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (enabled) {
					setValue(!expanded, true);
				}
			}
		});
	}

	public SectionToggle(Element elem) {
		super(elem);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Boolean getValue() {
		return isExpanded();
	}

	@Override
	public void setValue(Boolean value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Boolean value, boolean fireEvents) {
		if (value == null) {
			value = Boolean.FALSE;
		}

		boolean oldValue = fireEvents ? isExpanded() : false;
		setExpanded(value);
		if (fireEvents) {
			ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;

	}

	@Override
	public void setEnabled(boolean enabled) {
		this.setStyleName(res.css().disabled(), !enabled);
		this.enabled = enabled;
	}

}
