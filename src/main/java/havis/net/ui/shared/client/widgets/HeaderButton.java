package havis.net.ui.shared.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class HeaderButton extends Composite implements HasClickHandlers {

	@UiField
	ImageElement icon;
	
	@UiField
	FocusPanel outer;

	private static CoreButtonUiBinder uiBinder = GWT.create(CoreButtonUiBinder.class);

	interface CoreButtonUiBinder extends UiBinder<Widget, HeaderButton> {
	}

	public HeaderButton() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setIconSrc(String iconSrc) {
		icon.setSrc(iconSrc);
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return outer.addClickHandler(handler);
	}
}
