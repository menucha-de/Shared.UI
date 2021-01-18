package havis.net.ui.shared.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

import havis.net.ui.shared.resourcebundle.ResourceBundle;

public class BackButton extends Anchor {

	private ResourceBundle res = ResourceBundle.INSTANCE;

	public BackButton() {
		super();
		setStyleName(res.css().managementToggle());
		addStyleName(res.css().anchor());
		setHref("." + Window.Location.getQueryString());
	}
}