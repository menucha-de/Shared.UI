package havis.net.ui.shared.client.widgets;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;

public class ThreeStateSwitch extends FocusPanel implements HasValue<ThreeStateSwitch.Position>, HasEnabled {
	
	public static enum Position {
		LEFT, MIDDLE, RIGHT
	}

	private static final String BASE_STYLE = "webui-ThreeStateSwitch";

	private Position state = Position.LEFT;
	private boolean enabled = true;

	public ThreeStateSwitch() {
		this.setStylePrimaryName(BASE_STYLE);
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (enabled) {
					if (state == Position.LEFT) {
						setValue(Position.MIDDLE, true);
					} else if (state == Position.MIDDLE) {
						setValue(Position.RIGHT, true);
					} else if (state == Position.RIGHT) {
						setValue(Position.LEFT, true);
					}
				}
			}
		});
	}

	private String getStyle(Position type) {
		return type.toString().toLowerCase();
	}
	
	private void updateStyle() {
		this.removeStyleDependentName(getStyle(Position.LEFT));
		this.removeStyleDependentName(getStyle(Position.RIGHT));
		this.removeStyleDependentName(getStyle(Position.MIDDLE));
		
		this.setStyleDependentName(state.toString().toLowerCase(), true);
		this.setStyleName("disabled", !enabled);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Position> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Position getValue() {
		return state;
	}

	@Override
	public void setValue(Position value) {
		this.state = value != null ? value : Position.MIDDLE;
		updateStyle();
	}

	@Override
	public void setValue(Position value, boolean fireEvents) {
		setValue(value);
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateStyle();
	}
}
