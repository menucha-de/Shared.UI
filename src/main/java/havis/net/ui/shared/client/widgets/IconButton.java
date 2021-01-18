package havis.net.ui.shared.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class IconButton extends Composite implements HasClickHandlers {
	
	@UiField
	Button button;

	private static IconButtonUiBinder uiBinder = GWT.create(IconButtonUiBinder.class);

	interface IconButtonUiBinder extends UiBinder<Widget, IconButton> {
	}

	public IconButton() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setImageUrl(String url) {
		button.getElement().getStyle().setBackgroundImage("url(" + url + ")");
	}
	
	public void setText(String text) {
		button.setText(text);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return button.addClickHandler(handler);
	}
}
