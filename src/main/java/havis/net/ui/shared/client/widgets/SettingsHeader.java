package havis.net.ui.shared.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SettingsHeader extends Composite {

	@UiField
	HeaderButton icon;
	
	@UiField
	Label label;
	
	private static SettingsHeaderUiBinder uiBinder = GWT.create(SettingsHeaderUiBinder.class);

	interface SettingsHeaderUiBinder extends UiBinder<Widget, SettingsHeader> {
	}

	public SettingsHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setIconSrc(String iconSrc) {
		icon.setIconSrc(iconSrc);
	}
	
	public void setLabel(String text) {
		label.setText(text);
	}
	
	@UiHandler("back")
	void onBackClick(ClickEvent event) {
		Window.Location.assign(".#management:main");
	}
	
	public void setMainSection(boolean main) {
		icon.removeFromParent();
	}
}
